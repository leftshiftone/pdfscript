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
import java.util.regex.Pattern

class Text(private val text: String, private val config: Context.() -> Unit) : AbstractWritable() {

    override fun evaluate(context: Context): List<Evaluation> {
        val style = context.copy().apply(config)
        val parts = text.split(Pattern.compile("[ ]")).map { "$it " }

        return parts.map { toEvaluation(style, context, it) }
    }

    private fun toEvaluation(styler: Context, context: Context, text: String): Evaluation {
        return TextEvaluation({ styler.lineWidth(mask(text)) }, { styler.boxHeight() }) { stream, coordinates ->
            if (styler.foreground().isPresent)
                stream.setNonStrokingColor(styler.foreground().get())

            stream.setFont(styler.font()(text), styler.fontSize())
            stream.beginText()
            stream.newLineAtOffset(coordinates.x, coordinates.y - (styler.capHeight() + ((styler.boxHeight() - styler.capHeight()) / 2)))

            val resolved = resolve(text, stream)
            stream.showText(resolved)

            stream.endText()
            stream.setFont(context.font()(text), context.fontSize())

            coordinates.moveX(styler.lineWidth(resolved))
        }
    }

    class TextEvaluation(width: (EvaluationBase) -> Float,
                         height: (EvaluationBase) -> Float,
                         executionGraph: (PdfScriptStream, Coordinates) -> Unit) : Evaluation(width, height, executionGraph)

    private fun mask(str:String) = if (str.equals("{{page}} ") || str.equals("{{pages}} ")) "00 " else str

    private fun resolve(str:String, stream:PdfScriptStream):String {
        if (str.equals("{{page}} ")) return "${stream.page()} "
        else if (str.equals("{{pages}} ")) return "${stream.pages()} "
        else return str
    }

}
