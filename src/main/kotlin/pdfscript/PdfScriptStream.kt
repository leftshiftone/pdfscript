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

import net.coobird.thumbnailator.Thumbnails
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory
import pdfscript.interceptor.Interceptor
import pdfscript.model.PageFormat
import java.awt.Color
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


class PdfScriptStream(val document: PDDocument, val format: PageFormat, val interceptor: Interceptor, pages: Int) : Closeable {

    private val page = AtomicInteger(1)
    private val pages = AtomicInteger(pages)

    private val currentFontName = AtomicReference<PDFont>()
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
        this.currentFontName.set(null)
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
        interceptor.showText(text)
        contentStream.get().showText(text)
    }

    fun setFont(font: PDFont, size: Float) {
        requireNotNull(font) { "font must not be null" }
        requireNotNull(size) { "size must not be null" }

        if (this.currentFontName.get() != font || this.currentFontSize.get() != size) {
            interceptor.setFont(font, size)
            contentStream.get().setFont(font, size)
        }
        this.currentFontName.set(font)
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
        val pdImage = LosslessFactory.createFromImage(document, bim)

        contentStream.get().drawImage(pdImage, x, y)
    }

    fun drawImage(stream: InputStream, width: Int, height: Int, x: Float, y: Float) {
        val timestamp = System.currentTimeMillis()
        val bufImageIO = ImageIO.read(ByteArrayInputStream(stream.readBytes()))
        val dimensions = Pair(bufImageIO.getWidth(), bufImageIO.getHeight())
        val baos = ByteArrayOutputStream()

        val scale = if (dimensions.first > dimensions.second) width.toDouble().div(dimensions.first.toDouble())
        else height.toDouble().div(dimensions.second.toDouble())
        Thumbnails.of(bufImageIO).scale(scale).outputQuality(1.0).outputFormat("png").toOutputStream(baos)

        val bim = ImageIO.read(ByteArrayInputStream(baos.toByteArray()))
        val image = LosslessFactory.createFromImage(document, bim)
        contentStream.get().drawImage(image, x, y)
        println(System.currentTimeMillis() - timestamp)
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

    fun drawLine(x1: Float, y1: Float, x2: Float, y2: Float) {
        interceptor.drawLine(x1, y1, x2, y2)
        contentStream.get().moveTo(x1, y1)
        contentStream.get().lineTo(x2, y2)
        contentStream.get().stroke()
    }

    fun addRect(x: Float, y: Float, width: Float, height: Float) {
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
