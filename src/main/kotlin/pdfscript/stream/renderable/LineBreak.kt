package pdfscript.stream.renderable

import pdfscript.stream.Evaluation

@Deprecated("use Paragraph instead")
class LineBreak : AbstractWritable() {

    override fun evaluate(context: Context): List<Evaluation> {
        return listOf(Evaluation({0f}, {context.boxHeight()}) { stream, coordinates ->
            coordinates.x = coordinates.xInit
            coordinates.moveY(-context.boxHeight())
        })
    }

}
