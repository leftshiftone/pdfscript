package pdfscript.stream.renderable

import pdfscript.extension.maxOrDefault
import pdfscript.stream.Evaluation
import pdfscript.stream.Evaluation.EvaluationBase
import pdfscript.stream.PdfWriter
import kotlin.math.min

class Paragraph(val config: PdfWriter.() -> Unit, val style: Context.() -> Unit) : AbstractWritable() {

    override fun evaluate(context: Context): List<Evaluation> {
        val style = context.copy().apply(style)

        val writer = PdfWriter(style).apply(config)

        if (writer.evaluations.isEmpty())
            return listOf(Evaluation({0f}, {0f}, { _, _ -> }))

        val width = { base: EvaluationBase -> min(base.available, widthSum(writer.evaluations, base.available)) }
        val height = { base: EvaluationBase ->
            val calcWidth = widthSum(writer.evaluations, base.available)
            if (base.available < calcWidth) {
                val addHeight = Math.floor((calcWidth / base.available).toDouble()) * context.lineHeight()
                (writer.evaluations.map { it.height(base) }.maxOrDefault(0f) + addHeight).toFloat()
            } else {
                writer.evaluations.map { it.height(base) }.maxOrDefault(0f)
            }
        }

        return listOf(Evaluation(width, height) { stream, coordinates ->
            // if (style.backgroundColor().isPresent) {
            //     val calcWidth = width(EvaluationBase(coordinates.width, 0f))
            //     val calcHeight = height(EvaluationBase(coordinates.width, 0f))
            //     stream.addRect(coordinates.x, coordinates.y, calcWidth, -calcHeight)
            // }

            val curWidth = width(EvaluationBase(coordinates.width, 0f))

            val xInit = coordinates.x
            if (style.foregroundColor().isPresent)
                stream.setNonStrokingColor(style.foregroundColor().get())
            if (style.align().isPresent && style.align().get().equals("center"))
                coordinates.moveX((context.format.width() - context.margin.left - context.margin.right - curWidth)  / 2)

            writer.evaluations.forEach { write(stream, it, coordinates, style) }
            coordinates.moveY(-context.boxHeight())
            coordinates.x = xInit

            if (style.foregroundColor().isPresent)
                stream.setNonStrokingColor("black")
        })
    }

}
