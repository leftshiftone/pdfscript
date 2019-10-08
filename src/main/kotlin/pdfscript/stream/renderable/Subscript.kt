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
import pdfscript.stream.Coordinates
import pdfscript.stream.Evaluation
import pdfscript.stream.configurable.Context
import pdfscript.stream.configurable.font.FontProvider

class Subscript(private val text: String, private val config: Context.() -> Unit) : AbstractWritable() {

    override fun evaluate(context: Context, fontProvider: FontProvider): List<Evaluation> {
        val style = context.copy().apply(config)
        return listOf(toEvaluation(style, context, fontProvider.sanitize(style.font(), text)))
    }

    private fun toEvaluation(styler: Context, context: Context, text: String): Evaluation {
        return TextEvaluation({ styler.lineWidth(text, styler.fontSize() / 5 * 3) }, { styler.boxHeight() }) { stream, coordinates ->
            if (styler.foreground().isPresent())
                stream.setNonStrokingColor(styler.foreground().get())

            stream.setFont(styler.font(), styler.fontSize() / 5 * 3)
            stream.beginText()
            stream.newLineAtOffset(coordinates.x, coordinates.y + (styler.capHeight() / 1.5f))
            stream.showText(text)
            stream.endText()

            stream.setFont(context.font(), context.fontSize())

            coordinates.moveX(styler.lineWidth(text))
        }
    }

    class TextEvaluation(width: (EvaluationBase) -> Float,
                         height: (EvaluationBase) -> Float,
                         executionGraph: (PdfScriptStream, Coordinates) -> Unit) : Evaluation(width, height, executionGraph)

}
