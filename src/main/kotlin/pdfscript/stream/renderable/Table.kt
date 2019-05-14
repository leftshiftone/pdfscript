/*
 * Copyright 2019 Leftshift One
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pdfscript.stream.renderable

import pdfscript.PdfScriptStream
import pdfscript.extension.maxOrDefault
import pdfscript.stream.Coordinates
import pdfscript.stream.Evaluation
import pdfscript.stream.Evaluation.EvaluationBase
import pdfscript.stream.PdfWriter
import pdfscript.stream.configurable.Context
import pdfscript.stream.renderable.decorator.BackgroundDecorator
import pdfscript.stream.renderable.decorator.BorderDecorator
import kotlin.math.min

class Table(private val config: TableWriter.() -> Unit, private val style: Context.() -> Unit) : AbstractWritable() {

    override fun evaluate(context: Context): List<Evaluation> {
        val style = context.copy().apply(style).apply {paddingBottom(context.paddingBottom().orElse(0f))}
        val writer = TableWriter(style).apply(config)

        if (writer.evaluations.isEmpty())
            return listOf(Evaluation({ 0f }, { 0f }, { _, _ -> }))

        return writer.evaluations.mapIndexed { index, e ->
            val width = e.width(EvaluationBase(context.format.width(), 0f))
            val height = e.height(EvaluationBase(context.format.width(), 0f))

            return@mapIndexed Evaluation({ width }, { height }) { stream, coordinates ->
                e.execute(stream, Coordinates(coordinates, width, height))
                val tmp: Float = if (index % 2 == 0) 0.25f else 0f
                coordinates.moveY(-tmp) // 1 stroke of table
            }
        }
    }

    class TableWriter(private val context: Context) {
        val evaluations = ArrayList<Evaluation>()

        fun row(config: TableColWriter.() -> Unit) = row({}, config)

        fun row(style: Context.() -> Unit = {}, config: TableColWriter.() -> Unit) {
            val styler = context.copy().apply(style)
            val writer = TableColWriter(styler).apply(config)

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

    class TableColWriter(private val context: Context) {
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
                    val value = (calcWidth / base.available).toDouble()
                    val extra = if (value % 1 > 0.5) Math.ceil(value).toFloat() else Math.floor(value).toFloat()
                    (extra * calcHeight) + context.lineHeight()
                } else
                    calcHeight
            }

            evaluations.add(Evaluation({ it.available }, height) { stream, coordinates ->
                BackgroundDecorator(styler).evaluate(stream, coordinates)
                BorderDecorator(styler).evaluate(stream, coordinates)
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

        protected fun write(stream: PdfScriptStream, evaluation: Evaluation, coordinates: Coordinates, context: Context) {
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
