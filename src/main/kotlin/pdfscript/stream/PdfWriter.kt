package pdfscript.stream

import org.apache.pdfbox.pdmodel.font.PDFont
import pdfscript.stream.writable.*
import java.net.URL
import java.util.*

class PdfWriter(val context: Context) {

    val evaluations = ArrayList<Evaluation>()

    fun paragraph(style: Context.() -> Unit, config: PdfWriter.() -> Unit) {
        evaluations.addAll(Paragraph(config, style).evaluate(context))
    }

    fun paragraph(config: PdfWriter.() -> Unit) {
        evaluations.addAll(Paragraph(config, {}).evaluate(context))
    }

    fun table(config: Table.TableWriter.() -> Unit) {
        evaluations.addAll(Table(config, {}).evaluate(context))
    }

    fun table(style: Context.() -> Unit, config: Table.TableWriter.() -> Unit) {
        evaluations.addAll(Table(config, style).evaluate(context))
    }

    fun table(style: Context.() -> Unit, ratio:List<Number>, config: Table.TableWriter.() -> Unit) {
        evaluations.addAll(Table(config, style).evaluate(context))
    }

    fun text(text: String) = evaluations.addAll(Text(text, {}).evaluate(context))
    fun text(style: Context.() -> Unit = {}, text: String) = evaluations.addAll(Text(text, style).evaluate(context))

    fun linebreak() = evaluations.addAll(LineBreak().evaluate(context))

    fun tab() {
        evaluations.addAll(Tabulator().evaluate(context))
    }

    fun tab(tabSize:Number) {
        evaluations.addAll(Tabulator(Optional.ofNullable(tabSize.toFloat())).evaluate(context))
    }

    fun setFont(font: PDFont, size: Number) {
        context.fontName(font)
        context.fontSize(size.toFloat())
        evaluations.addAll(PdfsFont(font, size.toFloat()).evaluate(context))
    }

    fun addImage(image: String, width: Number, height: Number) {
        evaluations.addAll(PdfsImage(URL(image), width.toFloat(), height.toFloat()).evaluate(context))
    }

    fun superscript(text: String) = evaluations.addAll(Superscript(text, {}).evaluate(context))
    fun superscript(style: Context.() -> Unit = {}, text: String) = evaluations.addAll(Superscript(text, style).evaluate(context))

}
