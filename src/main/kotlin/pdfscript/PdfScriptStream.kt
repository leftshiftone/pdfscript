package pdfscript

import net.coobird.thumbnailator.Thumbnailator
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory
import pdfscript.interceptor.PdfsInterceptor
import pdfscript.model.PageFormat
import java.awt.Color
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import javax.imageio.ImageIO


class PdfScriptStream(val document: PDDocument, val format: PageFormat, val interceptor: PdfsInterceptor) {

    private val currentFontName = AtomicReference<PDFont>()
    private val currentFontSize = AtomicReference<Float>()

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
        pngTranscoder.addTranscodingHint( PNGTranscoder.KEY_WIDTH, width.toFloat() )
        pngTranscoder.addTranscodingHint( PNGTranscoder.KEY_HEIGHT, height.toFloat() )

        val os = ByteArrayOutputStream()
        pngTranscoder.transcode(TranscoderInput(stream), TranscoderOutput(os))
        val `is` = ByteArrayInputStream(os.toByteArray())

        val bim = ImageIO.read(`is`)
        val pdImage = LosslessFactory.createFromImage(document, bim)

        contentStream.get().drawImage(pdImage, x, y)
    }

    fun drawImage(stream: InputStream, width: Int, height: Int, x: Float, y: Float) {
        val timestamp = System.currentTimeMillis()
        val baos = ByteArrayOutputStream()
        Thumbnailator.createThumbnail(stream, baos, "png", width, height)

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

    fun close() {
        contentStream.get().close()
    }

}
