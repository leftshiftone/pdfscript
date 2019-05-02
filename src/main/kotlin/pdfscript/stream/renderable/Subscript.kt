package pdfscript.stream.renderable

import pdfscript.PdfScriptStream
import pdfscript.stream.Coordinates
import pdfscript.stream.Evaluation
import pdfscript.stream.configurable.Context

class Subscript(val text: String, val config: Context.() -> Unit) : AbstractWritable() {

    override fun evaluate(context: Context): List<Evaluation> {
        val style = context.copy().apply(config)
        return listOf(toEvaluation(style, context, text))
    }

    private fun toEvaluation(styler: Context, context: Context, text: String): Evaluation {
        return TextEvaluation({ styler.lineWidth(text, styler.fontSize() / 5 * 3) }, { styler.boxHeight() }) { stream, coordinates ->
            if (styler.foreground().isPresent())
                stream.setNonStrokingColor(styler.foreground().get())

            stream.setFont(styler.fontName(), styler.fontSize() / 5 * 3)
            stream.beginText()
            stream.newLineAtOffset(coordinates.x, coordinates.y + (styler.capHeight() / 1.5f))
            stream.showText(text)
            stream.endText()

            stream.setFont(context.fontName(), context.fontSize())

            coordinates.moveX(styler.lineWidth(text))
        }
    }

    class TextEvaluation(width: (EvaluationBase) -> Float,
                         height: (EvaluationBase) -> Float,
                         executionGraph: (PdfScriptStream, Coordinates) -> Unit) : Evaluation(width, height, executionGraph)

}
