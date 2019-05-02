package pdfscript.stream.renderable

import pdfscript.stream.Evaluation
import pdfscript.stream.configurable.Context
import java.util.*

class Tabulator(val width:Optional<Float> = Optional.empty()) : AbstractWritable() {

    override fun evaluate(context: Context): List<Evaluation> {
        val tabSize = width.orElse(100f)

        val width = {base:Evaluation.EvaluationBase ->
            tabSize - (base.accumulated % tabSize)
        }
        return listOf(Text.TextEvaluation(width, {context.boxHeight()}) { stream, coordinates ->
            //println("tabulator: $tabSize --> ${tabSize - ((coordinates.x - coordinates.xInit) % tabSize)}")
            //println("${coordinates.x} - ${coordinates.xInit}")
            //
            //stream.drawLine(coordinates.x, coordinates.y, coordinates.x + tabSize - ((coordinates.x - coordinates.xInit) % tabSize), coordinates.y)
            coordinates.moveX(tabSize - ((coordinates.x - coordinates.xInit) % tabSize))
        })
    }

}
