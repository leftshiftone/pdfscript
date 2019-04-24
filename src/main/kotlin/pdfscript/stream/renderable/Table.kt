package pdfscript.stream.renderable

import pdfscript.PdfScriptStream
import pdfscript.extension.maxOrDefault
import pdfscript.stream.Coordinates
import pdfscript.stream.Evaluation
import pdfscript.stream.Evaluation.EvaluationBase
import pdfscript.stream.PdfWriter
import pdfscript.stream.renderable.decorator.BackgroundDecorator
import pdfscript.stream.renderable.decorator.BorderDecorator
import java.util.concurrent.CopyOnWriteArraySet
import kotlin.math.min

class Table(val config: TableWriter.() -> Unit, val style: Context.() -> Unit) : AbstractWritable() {

    override fun evaluate(context: Context): List<Evaluation> {
        val style = context.copy().apply(style)
        val set = CopyOnWriteArraySet<String>()
        val writer = TableWriter(style, set).apply(config)

        if (writer.evaluations.isEmpty())
            return listOf(Evaluation({ 0f }, { 0f }, { _, _ -> }))

        val width = writer.evaluations.map { it.width(EvaluationBase(context.format.width(), 0f)) }.sum()
        val height = writer.evaluations.map { it.height(EvaluationBase(context.format.width(), 0f)) }.sum()

        return listOf(Evaluation({ width }, { height }) { stream, coordinates ->
            writer.evaluations.forEachIndexed() { index, it ->
                val tmp: Float = if (index % 2 == 0) 0.25f else 0f
                it.execute(stream, Coordinates(coordinates, width, height))
                // coordinates.moveY(-(context.lineHeight()) - tmp) // 1 stroke of table
                coordinates.moveY(-tmp) // 1 stroke of table
            }
            // coordinates.moveY(-(height - ((writer.evaluations.size) * context.lineHeight())))
            coordinates.x = coordinates.xInit
        })
    }

    class TableWriter(val context: Context, val lineRegistry: CopyOnWriteArraySet<String>) {
        val evaluations = ArrayList<Evaluation>()

        fun row(config: TableColWriter.() -> Unit) = row({}, config)

        fun row(style: Context.() -> Unit = {}, config: TableColWriter.() -> Unit) {
            val styler = context.copy().apply(style)
            val writer = TableColWriter(styler, lineRegistry).apply(config)

            val availableWidth = (context.format.width() - context.margin.left - context.margin.right)

            val padding = styler.paddingTop().orElse(0f) + styler.paddingBottom().orElse(0f)

            val width = writer.evaluations.mapIndexed { index, e ->
                if (styler.ratio().isPresent) {
                    val ratioSum = styler.ratio().get().sum()
                    val ratio = styler.ratio().get().get(index)

                    e.width(EvaluationBase((ratio / ratioSum) * availableWidth, 0f))
                } else {
                    e.width(EvaluationBase(availableWidth / writer.evaluations.size, 0f))
                }
            }.maxOrDefault(0f)

            val height = writer.evaluations.mapIndexed { index, e ->
                if (styler.ratio().isPresent) {
                    val ratioSum = styler.ratio().get().sum()
                    val ratio = styler.ratio().get().get(index)

                    e.height(EvaluationBase((ratio / ratioSum) * availableWidth, 0f))
                } else {
                    e.height(EvaluationBase(availableWidth / writer.evaluations.size, 0f))
                }
            }.maxOrDefault(0f)

            evaluations.add(Evaluation({ width }, { height }) { stream, coordinates ->
                writer.evaluations.forEachIndexed { index, e ->
                    if (styler.ratio().isPresent) {
                        val ratioSum = styler.ratio().get().sum()
                        val ratio = styler.ratio().get().get(index)

                        e.execute(stream, Coordinates(coordinates.x, coordinates.y, (ratio / ratioSum) * availableWidth, height + padding))
                        coordinates.moveX((ratio / ratioSum) * availableWidth)
                    } else {
                        e.execute(stream, Coordinates(coordinates.x, coordinates.y, availableWidth / writer.evaluations.size, height + padding))
                        coordinates.moveX(availableWidth / writer.evaluations.size)
                    }
                }
                coordinates.origin.ifPresent { it.moveY(-height) }
                coordinates.origin.ifPresent { it.moveY(-padding) }
            })
        }
    }

    class TableColWriter(val context: Context, val lineRegistry: CopyOnWriteArraySet<String>) {
        val evaluations = ArrayList<Evaluation>()

        fun col(config: PdfWriter.() -> Unit) = col({}, config)

        fun col(style: Context.() -> Unit = {}, config: PdfWriter.() -> Unit) {
            val styler = context.copy().apply(style)
            val writer = PdfWriter(styler).apply(config)

            val getWidth = { base: EvaluationBase -> min(base.available, writer.evaluations.map { it.width(base) }.sum()) }
            val height = { base: EvaluationBase ->
                // val calcWidth = writer.evaluations.map { it.width(base) }.maxOrDefault(0f)
                val calcWidth = calcWidth(writer.evaluations, base)
                val calcHeight = AbstractWritable.calcSumHeight(writer.evaluations, base)

                if (base.available < calcWidth) {
                    // calcHeight + context.lineHeight()
                       ((Math.floor((calcWidth / base.available).toDouble()) * calcHeight) + context.lineHeight()).toFloat()
                } else
                    calcHeight
            }

            evaluations.add(Evaluation({it.available}, height) { stream, coordinates ->
                BackgroundDecorator(styler).evaluate(stream, coordinates)
                BorderDecorator(styler).evaluate(stream, lineRegistry, coordinates)
                if (styler.isAlignCenter()) {
                    coordinates.moveX((coordinates.width - getWidth(EvaluationBase(coordinates.width, 0f))) / 2)
                }
                if (styler.isAlignRight()) {
                    coordinates.moveX((coordinates.width - getWidth(EvaluationBase(coordinates.width, 0f))) - 10)
                }

                val yInit = coordinates.y
                coordinates.moveX(5f)
                writer.evaluations.forEach {
                    coordinates.moveY(-styler.paddingTop().orElse(0f))
                    // it.execute(stream, coordinates)
                    write(stream, it, coordinates, context)
                    coordinates.moveY(styler.paddingTop().orElse(0f))
                }
                coordinates.y = yInit
            })
        }

        protected fun write(stream: PdfScriptStream, evaluation:Evaluation, coordinates: Coordinates, context:Context) {
            val availableWidth = coordinates.width - (coordinates.x - coordinates.xInit)
            val calcWidth = evaluation.width(EvaluationBase(availableWidth, 0f))
            if (availableWidth < calcWidth) {
                coordinates.moveY(-context.boxHeight())
                coordinates.x = coordinates.xInit + 5
            }
            evaluation.execute(stream, coordinates)
        }
    }

}
