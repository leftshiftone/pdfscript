package pdfscript

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import pdfscript.extension.sumOrDefault
import pdfscript.interceptor.Interceptor
import pdfscript.model.PageFormat
import pdfscript.model.PageMargin
import pdfscript.model.PageMargin.Companion.standard
import pdfscript.stream.Coordinates
import pdfscript.stream.Evaluation
import pdfscript.stream.PdfWriter
import pdfscript.stream.renderable.Context
import pdfscript.stream.renderable.Table
import java.io.ByteArrayOutputStream
import java.io.InputStream

class PdfScript(private val format: PageFormat, private val margin: PageMargin) {

    val headerWriter = PdfWriter(Context(format, margin))
    val footerWriter = PdfWriter(Context(format, margin))
    val centerWriter = PdfWriter(Context(format, margin))

    companion object {
        @JvmStatic
        @JvmOverloads
        fun dinA4(margin: PageMargin = standard(), config: PdfScript.() -> Unit): PdfScript {
            return PdfScript(PageFormat.dinA4(), margin).apply(config)
        }
    }

    fun paragraph(style: Context.() -> Unit, config: PdfWriter.() -> Unit) = centerWriter.paragraph(style, config)
    fun paragraph(config: PdfWriter.() -> Unit) = centerWriter.paragraph(config)
    fun table(config: Table.TableWriter.() -> Unit) = centerWriter.table(config)
    fun table(style: Context.() -> Unit, config: Table.TableWriter.() -> Unit) = centerWriter.table(style, config)
    @Deprecated("use the Context")
    fun font(font: PDFont, size: Float = 10f) = centerWriter.setFont(font, size)

    fun text(text: String) = centerWriter.text(text)
    fun text(style: Context.() -> Unit, text: String) = centerWriter.text(style, text)

    fun superscript(text: String) = centerWriter.superscript({}, text)
    fun superscript(style: Context.() -> Unit, text: String) = centerWriter.superscript(style, text)
    fun subscript(text: String) = centerWriter.subscript({}, text)
    fun subscript(style: Context.() -> Unit, text: String) = centerWriter.subscript(style, text)

    fun image(image: String, width: Number, height: Number) = centerWriter.image(image, width, height)
    fun image(image: () -> InputStream, width: Number, height: Number) = centerWriter.image(image, width, height)
    fun image(image: ByteArray, width: Number, height: Number) = centerWriter.image(image, width, height)

    fun svg(image: String, width: Number, height: Number) = centerWriter.svg(image, width, height)
    fun svg(image: () -> InputStream, width: Number, height: Number) = centerWriter.svg(image, width, height)
    fun svg(image: ByteArray, width: Number, height: Number) = centerWriter.svg(image, width, height)

    fun withHeader(config: PdfWriter.() -> Unit) = headerWriter.apply(config)
    fun withFooter(config: PdfWriter.() -> Unit) = footerWriter.apply(config)

    @JvmOverloads
    fun execute(interceptor: Interceptor = Interceptor()): ByteArray {
        val document = PDDocument()

        // evaluate renderables
        // ********************
        val headerHeight = headerWriter.evaluations.map { it.height(Evaluation.EvaluationBase(format.width(), 0f)) }.sumOrDefault(0f)
        val footerHeight = footerWriter.evaluations.map { it.height(Evaluation.EvaluationBase(format.width(), 0f)) }.sumOrDefault(0f)
        val centerHeight = centerWriter.evaluations.map { it.height(Evaluation.EvaluationBase(format.width(), 0f)) }.sumOrDefault(0f)

        val availableCenterHeight = format.height() -
                Math.max(margin.top, headerHeight + margin.header) -
                Math.max(margin.bottom, footerHeight + margin.bottom)

        val pageCount = Math.ceil(centerHeight.toDouble() / availableCenterHeight.toDouble()).toInt()
        val stream = PdfScriptStream(document, this.format, interceptor, pageCount)

        // execute renderables
        // *******************
        val headerCoordinates = Coordinates(margin.left, format.height() - margin.header)
        val footerCoordinates = Coordinates(margin.left, margin.bottom, format.width(), format.height())
        val centerCoordinates = Coordinates(margin.left, format.height() - margin.top - 1)

        if (headerHeight > margin.header)
            centerCoordinates.moveY(margin.header - headerHeight)
        if (footerHeight > margin.bottom - margin.footer)
            footerCoordinates.moveY(footerHeight - (margin.bottom - margin.footer))

        headerWriter.evaluations.forEach { write(stream, it, headerCoordinates, false) }
        footerWriter.evaluations.forEach { write(stream, it, footerCoordinates, false) }
        centerWriter.evaluations.forEach { write(stream, it, centerCoordinates, true) }

        stream.close()
        val baos = ByteArrayOutputStream()
        document.save(baos)

        document.close()
        return baos.toByteArray()
    }

    private fun write(stream: PdfScriptStream, evaluation: Evaluation, coordinates: Coordinates, newPage: Boolean) {
        val footerHeight = footerWriter.evaluations.map { it.height(Evaluation.EvaluationBase(format.width(), 0f)) }.sumOrDefault(0f)
        val calcHeight = margin.bottom + footerHeight // margin.bottom + footerHeight - (margin.bottom - margin.footer)

        if (newPage && (coordinates.y - evaluation.height(Evaluation.EvaluationBase(format.width(), 0f))) < calcHeight) {
            stream.newPage(format)
            coordinates.x = coordinates.xInit
            coordinates.y = coordinates.yInit

            val headerCoordinates = Coordinates(margin.left, format.height() - margin.header)
            val footerCoordinates = Coordinates(margin.left, margin.bottom, format.width(), format.height())

            val headerHeight = headerWriter.evaluations.map { it.height(Evaluation.EvaluationBase(format.width(), 0f)) }.sumOrDefault(0f)

            if (headerHeight > margin.header)
                coordinates.moveY(margin.header - headerHeight)

            if (footerHeight > margin.bottom - margin.footer)
                footerCoordinates.moveY(footerHeight - (margin.bottom - margin.footer))

            headerWriter.evaluations.forEach { write(stream, it, headerCoordinates, false) }
            footerWriter.evaluations.forEach { write(stream, it, footerCoordinates, false) }
        }

        val availableWidth = format.width() - coordinates.x - margin.right
        if (availableWidth < evaluation.width(Evaluation.EvaluationBase(format.width(), 0f))) {
            coordinates.moveY(-10f)
            coordinates.x = coordinates.xInit
        }
        evaluation.execute(stream, coordinates)
    }

}
