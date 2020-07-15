/*
 * Copyright 2019 Leftshift One
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pdfscript

import de.rototor.pdfbox.graphics2d.PdfBoxGraphics2D
import net.coobird.thumbnailator.Thumbnails
import org.apache.batik.anim.dom.SAXSVGDocumentFactory
import org.apache.batik.bridge.BridgeContext
import org.apache.batik.bridge.DocumentLoader
import org.apache.batik.bridge.GVTBuilder
import org.apache.batik.bridge.UserAgentAdapter
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import org.apache.batik.util.XMLResourceDescriptor
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory
import org.slf4j.LoggerFactory
import pdfscript.interceptor.Interceptor
import pdfscript.model.PageFormat
import pdfscript.stream.configurable.font.FontProvider
import java.awt.Color
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.InputStream
import java.util.*
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import javax.imageio.ImageIO
import kotlin.math.floor
import kotlin.math.round


class PdfScriptStream(val document: PDDocument,
                      val format: PageFormat,
                      val interceptor: Interceptor,
                      val fontProvider: FontProvider,
                      pages: Int) : Closeable {

    private val log = LoggerFactory.getLogger(this::class.java)

    private val page = AtomicInteger(1)
    private val pages = AtomicInteger(pages)

    private val currentFont = AtomicReference<PDFont>()
    private val currentFontSize = AtomicReference<Float>()

    private val lineRegistry = CopyOnWriteArraySet<String>()

    val colors: Properties = Properties().apply {
        load(PdfScriptStream::class.java.getResourceAsStream("/color.properties"))
    }

    val contentStream: AtomicReference<PDPageContentStream>

    init {
        val page = format.page()
        this.document.addPage(page)
        this.contentStream = AtomicReference(PDPageContentStream(document, page))
    }

    fun newPage(format: PageFormat = this.format) {
        val page = format.page()
        document.addPage(page)
        this.contentStream.get().close()
        this.contentStream.set(PDPageContentStream(document, page))
        this.page.incrementAndGet()
        this.currentFont.set(null)
        this.currentFontSize.set(null)
        this.lineRegistry.clear()
    }

    fun beginText() {
        interceptor.beginText()
        contentStream.get().beginText()
    }

    fun endText() {
        interceptor.endText()
        contentStream.get().endText()
    }

    fun showText(text: String) {
        val sanitizedText = fontProvider.sanitize(currentFont.get(), text)
        interceptor.showText(sanitizedText)
        contentStream.get().showText(sanitizedText)
    }

    fun setFont(font: PDFont, size: Float) {
        requireNotNull(font) { "font must not be null" }
        requireNotNull(size) { "size must not be null" }

        if (this.currentFont.get() != font || this.currentFontSize.get() != size) {
            interceptor.setFont(font, size)
            contentStream.get().setFont(font, size)
        }
        this.currentFont.set(font)
        this.currentFontSize.set(size)
    }

    fun newLineAtOffset(x: Float, y: Float) {
        interceptor.newLineAtOffset(x, y)
        contentStream.get().newLineAtOffset(x, y)
    }

    fun drawSvg(stream: InputStream, width: Int, height: Int, x: Float, y: Float) {
        val pngTranscoder = PNGTranscoder()
        pngTranscoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, width.toFloat())
        pngTranscoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, height.toFloat())

        val os = ByteArrayOutputStream()
        pngTranscoder.transcode(TranscoderInput(stream), TranscoderOutput(os))
        val `is` = ByteArrayInputStream(os.toByteArray())

        val bim = ImageIO.read(`is`)
        val pdImage = JPEGFactory.createFromImage(document, bim)

        interceptor.drawImage(pdImage, x, y)
        contentStream.get().drawImage(pdImage, x, y)
    }

    fun drawSvg(stream: InputStream, x: Float, y: Float, scale: Float) {
        // create the document
        val parser = XMLResourceDescriptor.getXMLParserClassName();
        val f = SAXSVGDocumentFactory(parser);
        val document = f.createDocument("test", stream);

        // create the GVT
        val userAgent = UserAgentAdapter()
        val loader = DocumentLoader(userAgent)
        val bctx = BridgeContext(userAgent, loader)
        bctx.setDynamicState(BridgeContext.STATIC)

        val builder = GVTBuilder()
        val gvtRoot = builder.build(bctx, document)

        val pdfBoxGraphics2D = PdfBoxGraphics2D(this.document, 400f, 400f)

        pdfBoxGraphics2D.scale(scale.toDouble(), scale.toDouble())
        gvtRoot.paint(pdfBoxGraphics2D);

        pdfBoxGraphics2D.dispose()

        val appearanceStream = pdfBoxGraphics2D.getXFormObject()
        val transform = AffineTransform.getTranslateInstance(x.toDouble(), y.toDouble() - (400 * scale))
        transform.scale(scale.toDouble(), scale.toDouble())
        appearanceStream.setMatrix(transform)

        interceptor.drawSvg(x, y, scale)
        contentStream.get().drawForm(appearanceStream)
    }


    fun drawImage(stream: InputStream, width: Int, height: Int, x: Float, y: Float) {
        val timestamp = System.currentTimeMillis()
        val bufImageIO: BufferedImage? = ImageIO.read(ByteArrayInputStream(stream.readBytes()))
        if (bufImageIO == null) {
            log.error("Image buffer is corrupt; cannot draw image")
            return
        }

        val dimensions = Pair(bufImageIO.width, bufImageIO.height)
        val baos = ByteArrayOutputStream()

        val scale = if (dimensions.first > dimensions.second) width.toDouble().div(dimensions.first.toDouble())
        else height.toDouble().div(dimensions.second.toDouble())
        Thumbnails.of(bufImageIO).scale(scale).outputQuality(1.0).outputFormat("png").toOutputStream(baos)

        val bim = ImageIO.read(ByteArrayInputStream(baos.toByteArray()))
        val image = LosslessFactory.createFromImage(document, bim)

        interceptor.drawImage(image, x, y)
        contentStream.get().drawImage(image, x, y)
        log.debug("Drew image in ${System.currentTimeMillis() - timestamp}ms")
    }

    fun setStrokingColor(colorStr: String) {
        interceptor.setStrokingColor(colorStr)
        val color = if (colorStr.startsWith("#")) Color.decode(colorStr) else Color.decode(colors.get(colorStr).toString())
        contentStream.get().setStrokingColor(color)
    }

    fun setNonStrokingColor(colorStr: String) {
        interceptor.setNonStrokingColor(colorStr)
        val color = if (colorStr.startsWith("#")) Color.decode(colorStr) else Color.decode(colors.get(colorStr).toString())
        contentStream.get().setNonStrokingColor(color)
    }

    fun setLineWidth(width: Number) {
        contentStream.get().setLineWidth(width.toFloat())
    }

    fun drawLine(x1: Float, y1: Float, x2: Float, y2: Float) {
        interceptor.drawLine(x1, y1, x2, y2)
        contentStream.get().moveTo(x1, y1)
        contentStream.get().lineTo(x2, y2)
        contentStream.get().stroke()
    }

    fun drawDashedLine(x1: Float, y1: Float, x2: Float, y2: Float, dashPattern: Optional<FloatArray> = Optional.empty()) {
        interceptor.drawLine(x1, y1, x2, y2)
        if (dashPattern.isPresent)
            contentStream.get().setLineDashPattern (dashPattern.get(), 0f);
        contentStream.get().moveTo(x1, y1)
        contentStream.get().lineTo(x2, y2)
        contentStream.get().stroke()
    }

    fun drawCircle(x: Float, y: Float, r: Float) {
        interceptor.drawCircle(x, y, r)
        // Bezier curves circle
        // draw a circle by 4 points, (4/3)*tan(pi/8) = 4*(sqrt(2)-1)/3 = 0.552284749831
        val k = 0.552284749831f

        contentStream.get().moveTo(x - r, y);
        contentStream.get().curveTo(x - r, y + k * r, x - k * r, y + r, x, y + r);
        contentStream.get().curveTo(x + k * r, y + r, x + r, y + k * r, x + r, y);
        contentStream.get().curveTo(x + r, y - k * r, x + k * r, y - r, x, y - r);
        contentStream.get().curveTo(x - k * r, y - r, x - r, y - k * r, x - r, y);
        contentStream.get().fill();
    }

    fun addRect(x: Float, y: Float, width: Float, height: Float) {
        interceptor.drawRect(x, y, width, height)
        contentStream.get().addRect(x + 0.5f, y - 0.5f, width, height)
        contentStream.get().fill()
    }

    override fun close() {
        contentStream.get().close()
    }

    fun pages() = pages.get()
    fun page() = page.get()

    fun checkAndAdd(x1: Float, y1: Float, x2: Float, y2: Float): Boolean {
        lineRegistry.add("${page()}@${floor(x1 + 1)}:${floor(y1)}-${floor(x2 + 1)}:${floor(y2)}")
        lineRegistry.add("${page()}@${floor(x1 - 1)}:${floor(y1)}-${floor(x2 - 1)}:${floor(y2)}")
        lineRegistry.add("${page()}@${floor(x1)}:${floor(y1 + 1)}-${floor(x2)}:${floor(y2 + 1)}")
        lineRegistry.add("${page()}@${floor(x1)}:${floor(y1 - 1)}-${floor(x2)}:${floor(y2 - 1)}")
        return lineRegistry.add("${page()}@${round(floor(x1))}:${round(floor(y1))}-${round(floor(x2))}:${round(floor(y2))}")
    }
}
