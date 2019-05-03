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

package pdfscript.stream

import pdfscript.PdfScriptStream

open class Evaluation(val width: (EvaluationBase) -> Float,
                      val height:(EvaluationBase) -> Float,
                      val executionGraph:(PdfScriptStream, Coordinates) -> Unit) {

    fun execute(stream: PdfScriptStream, coordinates: Coordinates) = this.executionGraph(stream, coordinates)

    data class EvaluationBase(val available:Float, val accumulated:Float)

}
