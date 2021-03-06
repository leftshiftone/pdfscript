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

class DrawRect(private val x: Float,
               private val y: Float,
               private val w: Float,
               private val h: Float,
               private val b: Brush) : AbstractWritable() {

    override fun evaluate(context: Context, fontProvider: FontProvider): List<Evaluation> {
        return listOf(Evaluation({ 0f }, { 0f }) { stream, _ ->
            if (b.fill().isPresent)
                stream.setNonStrokingColor(b.fill().get())
            if (b.stroke().isPresent)
                stream.setStrokingColor(b.stroke().get())

            stream.addRect(x, y, w, h)

            if (b.fill().isPresent)
                stream.setNonStrokingColor("black")
            if (b.stroke().isPresent)
                stream.setNonStrokingColor("black")
        })
    }

}
