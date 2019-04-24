package pdfscript.stream.renderable

import pdfscript.PdfScriptStream
import pdfscript.stream.Coordinates
import pdfscript.stream.Evaluation
import java.util.regex.Pattern

class Text(val text: String, val config: Context.() -> Unit) : AbstractWritable() {

    override fun evaluate(context: Context): List<Evaluation> {
        val style = context.copy().apply(config)
        val parts = text.split(Pattern.compile("[ ]")).map { it + " " }

        return parts.map {
            return@map toEvaluation(style, context, it)
        }
    }

    private fun toEvaluation(styler: Context, context:Context, text: String): Evaluation {
        return TextEvaluation({ styler.lineWidth(text) }, { styler.boxHeight() }) { stream, coordinates ->
            if (styler.foregroundColor().isPresent())
                stream.setNonStrokingColor(styler.foregroundColor().get())

            // if (styler.border().isPresent && styler.border().get())
            //     stream.drawLine(coordinates.x, coordinates.y - styler.boxHeight(), coordinates.x + 200, coordinates.y - styler.boxHeight())

            // if (!styler.fontName().equals(context.fontName()) || !styler.fontSize().equals(context.fontSize()))
                stream.setFont(styler.fontName(), styler.fontSize())

            stream.beginText()
            stream.newLineAtOffset(coordinates.x, coordinates.y - (styler.capHeight() + ((styler.boxHeight() - styler.capHeight()) / 2)))
            stream.showText(text)
            stream.endText()

            // if (!styler.fontName().equals(context.fontName()) || !styler.fontSize().equals(context.fontSize()))
                stream.setFont(context.fontName(), context.fontSize())

            coordinates.moveX(styler.lineWidth(text))
        }
    }

    class TextEvaluation(width: (EvaluationBase) -> Float,
                         height: (EvaluationBase) -> Float,
                         executionGraph: (PdfScriptStream, Coordinates) -> Unit) : Evaluation(width, height, executionGraph)

}
