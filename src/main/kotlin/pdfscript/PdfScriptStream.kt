package pdfscript

import net.coobird.thumbnailator.Thumbnailator
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import pdfscript.interceptor.PdfsInterceptor
import pdfscript.model.PageFormat
import java.awt.Color
import java.io.ByteArrayOutputStream
import java.net.URL
import java.util.*
import java.util.concurrent.atomic.AtomicReference

class PdfScriptStream(val document: PDDocument, val format: PageFormat, val interceptor: PdfsInterceptor) {

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
        interceptor.setFont(font, size)
        contentStream.get().setFont(font, size)
    }

    fun newLineAtOffset(x: Float, y: Float) {
        interceptor.newLineAtOffset(x, y)
        contentStream.get().newLineAtOffset(x, y)
    }

    fun drawImage(url: URL, width: Int, height: Int, x: Float, y: Float) {
        interceptor.drawImage(url, x, y)

        val baos = ByteArrayOutputStream()
        Thumbnailator.createThumbnail(url.openStream(), baos, "png", width, height)

        val image = PDImageXObject.createFromByteArray(document, baos.toByteArray(), "asdf")
        contentStream.get().drawImage(image, x, y)
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
