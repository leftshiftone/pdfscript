package pdfscript.stream.writable

import pdfscript.stream.Evaluation

class LineBreak : AbstractWritable() {

    override fun evaluate(context: Context): List<Evaluation> {
        return listOf(Evaluation({0f}, {context.boxHeight()}) { stream, coordinates ->
            coordinates.x = coordinates.xInit
            coordinates.moveY(-context.boxHeight())
        })
    }

}
