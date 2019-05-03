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
