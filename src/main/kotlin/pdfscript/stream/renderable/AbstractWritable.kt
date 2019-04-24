package pdfscript.stream.renderable

import pdfscript.PdfScriptStream
import pdfscript.extension.maxOrDefault
import pdfscript.stream.Coordinates
import pdfscript.stream.Evaluation
import pdfscript.stream.Evaluation.EvaluationBase
import kotlin.math.max

abstract class AbstractWritable {

    companion object {
        fun calcSumHeight(evaluations: List<Evaluation>, base: EvaluationBase):Float {
            val heights = ArrayList<Float>()
            var isText = false
            evaluations.forEach {
                if (isText && it is Text.TextEvaluation)
                    heights.set(heights.lastIndex, max(heights.last(), it.height(base)))
                else
                    heights.add(it.height(base))
                isText = it is Text.TextEvaluation
            }
            return heights.sum()
        }

        fun calcWidth(evaluations: List<Evaluation>, base: EvaluationBase):Float {
            val widths = ArrayList<Float>()
            var isText = false
            evaluations.forEach {
                if (isText && it is Text.TextEvaluation) {
                    if (widths.isEmpty()) {
                        widths.add(it.width(base))
                    }
                    else {
                        widths.set(widths.lastIndex, widths.get(widths.lastIndex) + it.width(base))
                    }
                }
                else
                    widths.add(it.width(base))
                isText = it is Text.TextEvaluation
            }
            return widths.maxOrDefault(0f)
        }

    }

    abstract fun evaluate(context:Context):List<Evaluation>;

    protected fun widthSum(evaluations:List<Evaluation>, availableWidth:Float):Float {
        var sum = 0f
        evaluations.forEach {
            sum += it.width(EvaluationBase(availableWidth, sum))
        }
        return sum
    }

    protected fun widthMax(evaluations:List<Evaluation>, availableWidth:Float):Float {
        return evaluations.map { it.width(EvaluationBase(availableWidth, 0f)) }.max()!!
    }

    protected fun write(stream: PdfScriptStream, evaluation: Evaluation, coordinates: Coordinates, context:Context) {
        val availableWidth = context.format.width() - coordinates.x - context.margin.right
        if (availableWidth < evaluation.width(EvaluationBase(context.format.width(), context.format.width() - coordinates.x))) {
            coordinates.moveY(-(context.fontName().boundingBox.height / 1000) * context.fontSize())
            coordinates.x = coordinates.xInit
        }
        evaluation.execute(stream, coordinates)
    }

}
