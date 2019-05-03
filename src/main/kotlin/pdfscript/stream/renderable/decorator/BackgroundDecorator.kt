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

package pdfscript.stream.renderable.decorator

import pdfscript.PdfScriptStream
import pdfscript.stream.Coordinates
import pdfscript.stream.configurable.Context

class BackgroundDecorator(val context: Context) {

    fun evaluate(stream: PdfScriptStream, coordinates: Coordinates) {
        if (context.background().isPresent) {
            stream.setNonStrokingColor(context.background().get())
            stream.addRect(coordinates.x, coordinates.y - coordinates.height, coordinates.width, coordinates.height)
            stream.setNonStrokingColor("black")
        }
    }

}
