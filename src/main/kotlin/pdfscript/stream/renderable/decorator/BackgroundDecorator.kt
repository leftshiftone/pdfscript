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
