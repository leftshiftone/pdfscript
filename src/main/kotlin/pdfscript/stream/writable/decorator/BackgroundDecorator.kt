package pdfscript.stream.writable.decorator

import pdfscript.PdfScriptStream
import pdfscript.stream.Coordinates
import pdfscript.stream.writable.Context

class BackgroundDecorator(val context: Context) {

    fun evaluate(stream: PdfScriptStream, coordinates: Coordinates) {
        if (context.backgroundColor().isPresent) {
            stream.setNonStrokingColor(context.backgroundColor().get())
            stream.addRect(coordinates.x, coordinates.y - coordinates.height, coordinates.width, coordinates.height)
            stream.setNonStrokingColor("black")
        }
    }

}
