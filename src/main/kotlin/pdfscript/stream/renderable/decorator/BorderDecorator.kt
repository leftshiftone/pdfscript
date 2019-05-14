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

class BorderDecorator(val context: Context) {

    fun evaluate(stream: PdfScriptStream, coordinates: Coordinates) {
        if (context.border().isPresent && !context.border().get().equals("none")) {
            // top
            if (context.borderTop().isPresent)
                stream.setStrokingColor(context.borderTop().get())
            drawTopBorder(coordinates, stream)
            if (context.borderTop().isPresent)
                stream.setStrokingColor("black")

            // left
            if (context.borderLeft().isPresent)
                stream.setStrokingColor(context.borderLeft().get())
            drawLeftBorder(coordinates, stream)
            if (context.borderLeft().isPresent)
                stream.setStrokingColor("black")

            // right
            if (context.borderRight().isPresent)
                stream.setStrokingColor(context.borderRight().get())
            drawRightBorder(coordinates, stream)
            if (context.borderRight().isPresent)
                stream.setStrokingColor("black")

            // bottom
            if (context.borderBottom().isPresent)
                stream.setStrokingColor(context.borderBottom().get())
            drawBottomBorder(coordinates, stream)
            if (context.borderBottom().isPresent)
                stream.setStrokingColor("black")
        }
    }

    private fun drawTopBorder(coordinates: Coordinates, stream: PdfScriptStream) {
        val x1 = coordinates.x
        val x2 = x1 + coordinates.width
        val y1 = coordinates.y
        val y2 = coordinates.y - coordinates.height

        if (stream.checkAndAdd(x1, y1, x2, y1))
            stream.drawLine(x1 - 0.5f, y1, x2 + 0.5f, y1)
    }

    private fun drawLeftBorder(coordinates: Coordinates, stream: PdfScriptStream) {
        val x1 = coordinates.x
        val x2 = x1 + coordinates.width
        val y1 = coordinates.y
        val y2 = coordinates.y - coordinates.height

        if (stream.checkAndAdd(x1, y1, x1, y2))
            stream.drawLine(x1, y1 + 0.5f, x1, y2 - 0.5f)
    }

    private fun drawRightBorder(coordinates: Coordinates, stream: PdfScriptStream) {
        val x1 = coordinates.x
        val x2 = x1 + coordinates.width
        val y1 = coordinates.y
        val y2 = coordinates.y - coordinates.height

        if (stream.checkAndAdd(x2, y1, x2, y2))
            stream.drawLine(x2, y1 + 0.5f, x2, y2 + 0.5f)
    }

    private fun drawBottomBorder(coordinates: Coordinates, stream: PdfScriptStream) {
        val x1 = coordinates.x
        val x2 = x1 + coordinates.width
        val y1 = coordinates.y
        val y2 = coordinates.y - coordinates.height

        if (stream.checkAndAdd(x1, y2, x2, y2))
            stream.drawLine(x1 - 0.5f, y2, x2 + 0.5f, y2)
    }

}
