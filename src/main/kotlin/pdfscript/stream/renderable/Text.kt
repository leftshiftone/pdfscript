package pdfscript.stream.renderable

import pdfscript.PdfScriptStream
import pdfscript.stream.Coordinates
import pdfscript.stream.Evaluation
import java.util.regex.Pattern

class Text(val text: String, val config: Context.() -> Unit) : AbstractWritable() {

    override fun evaluate(context: Context): List<Evaluation> {
        val style = context.copy().apply(config)
        val parts = text.split(Pattern.compile("[ ]")).map { "$it " }

        return parts.map { toEvaluation(style, context, it) }
    }

    private fun toEvaluation(styler: Context, context:Context, text: String): Evaluation {
        return TextEvaluation({ styler.lineWidth(mask(text)) }, { styler.boxHeight() }) { stream, coordinates ->
            if (styler.foreground().isPresent)
                stream.setNonStrokingColor(styler.foreground().get())

            stream.setFont(styler.fontName(), styler.fontSize())
            stream.beginText()
            stream.newLineAtOffset(coordinates.x, coordinates.y - (styler.capHeight() + ((styler.boxHeight() - styler.capHeight()) / 2)))

            val resolved = resolve(text, stream)
            stream.showText(resolved)

            stream.endText()
            stream.setFont(context.fontName(), context.fontSize())

            coordinates.moveX(styler.lineWidth(resolved))
        }
    }

    class TextEvaluation(width: (EvaluationBase) -> Float,
                         height: (EvaluationBase) -> Float,
                         executionGraph: (PdfScriptStream, Coordinates) -> Unit) : Evaluation(width, height, executionGraph)

    private fun mask(str:String) = if (str.equals("{{page}} ") || str.equals("{{pages}} ")) "00 " else str

    private fun resolve(str:String, stream:PdfScriptStream):String {
        if (str.equals("{{page}} ")) return "${stream.page()} "
        else if (str.equals("{{pages}} ")) return "${stream.pages()} "
        else return str
    }

}
