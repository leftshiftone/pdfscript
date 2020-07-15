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

package pdfscript.stream.renderable.canvas

import pdfscript.stream.Evaluation
import pdfscript.stream.configurable.Brush
import pdfscript.stream.configurable.Context
import pdfscript.stream.configurable.font.FontProvider
import pdfscript.stream.renderable.AbstractWritable

class DrawLine(private val x1: Float,
               private val y1: Float,
               private val x2: Float,
               private val y2: Float,
               private val br: Brush) : AbstractWritable() {

    override fun evaluate(context: Context, fontProvider: FontProvider): List<Evaluation> {
        return listOf(Evaluation({ 0f }, { 0f }) { stream, _ ->
            if (br.fill().isPresent)
                stream.setNonStrokingColor(br.fill().get())
            if (br.stroke().isPresent)
                stream.setStrokingColor(br.stroke().get())
            if (br.lineWidth().isPresent)
                stream.setLineWidth(br.lineWidth().get())

            stream.drawDashedLine(x1, y1, x2, y2, br.lineDashPattern())

            if (br.fill().isPresent)
                stream.setNonStrokingColor("black")
            if (br.stroke().isPresent)
                stream.setStrokingColor("black")
            if (br.lineWidth().isPresent)
                stream.setLineWidth(1)
        })
    }

}
