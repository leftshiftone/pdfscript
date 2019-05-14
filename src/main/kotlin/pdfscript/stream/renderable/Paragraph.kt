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

import pdfscript.extension.maxOrDefault
import pdfscript.stream.Evaluation
import pdfscript.stream.Evaluation.EvaluationBase
import pdfscript.stream.PdfWriter
import pdfscript.stream.configurable.Context
import kotlin.math.min

class Paragraph(private val config: PdfWriter.() -> Unit, private val style: Context.() -> Unit) : AbstractWritable() {

    override fun evaluate(context: Context): List<Evaluation> {
        val style = context.copy().apply(style)

        val padding = style.paddingTop().orElse(0f) + style.paddingBottom().orElse(style.boxHeight() / 2);

        val writer = PdfWriter(style).apply(config)

        if (writer.evaluations.isEmpty()) {
            val height = padding + context.lineHeight()
            return listOf(Evaluation({ 0f }, { height }, { _, coords -> coords.moveY(-height)}))
        }

        val width = { base: EvaluationBase -> min(base.available, widthSum(writer.evaluations, base.available)) }
        val height = { base: EvaluationBase ->
            val calcWidth = widthSum(writer.evaluations, base.available)
            if (base.available < calcWidth) {
                val addHeight = Math.floor((calcWidth / base.available).toDouble()) * context.lineHeight()
                ((writer.evaluations.map { it.height(base) }.maxOrDefault(0f) + addHeight).toFloat()) + padding
            } else {
                writer.evaluations.map { it.height(base) }.maxOrDefault(0f) + padding
            }
        }

        return listOf(Evaluation(width, height) { stream, coordinates ->
            val curWidth = width(EvaluationBase(coordinates.width, 0f))

            val xInit = coordinates.x
            if (style.foreground().isPresent)
                stream.setNonStrokingColor(style.foreground().get())
            if (style.align().isPresent && style.align().get().equals("center"))
                coordinates.moveX((context.format.width() - context.margin.left - context.margin.right - curWidth)  / 2)

            writer.evaluations.forEach { write(stream, it, coordinates, style) }
            coordinates.moveY(-(context.boxHeight() + padding))
            coordinates.x = xInit

            if (style.foreground().isPresent)
                stream.setNonStrokingColor("black")
        })
    }

}
