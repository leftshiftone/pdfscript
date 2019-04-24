package pdfscript.stream.renderable.decorator

import pdfscript.PdfScriptStream
import pdfscript.stream.Coordinates
import pdfscript.stream.renderable.Context
import java.util.concurrent.CopyOnWriteArraySet
import kotlin.math.floor
import kotlin.math.round

class BorderDecorator(val context: Context) {

    fun evaluate(stream: PdfScriptStream, registry: CopyOnWriteArraySet<String>, coordinates: Coordinates) {
        if (context.border().isPresent && context.border().get()) {
            // top
            if (context.borderTopColor().isPresent)
                stream.setStrokingColor(context.borderTopColor().get())
            drawTopBorder(registry, coordinates, stream)
            if (context.borderTopColor().isPresent)
                stream.setStrokingColor("black")

            // left
            if (context.borderLeftColor().isPresent)
                stream.setStrokingColor(context.borderLeftColor().get())
            drawLeftBorder(registry, coordinates, stream)
            if (context.borderLeftColor().isPresent)
                stream.setStrokingColor("black")

            // right
            if (context.borderRightColor().isPresent)
                stream.setStrokingColor(context.borderRightColor().get())
            drawRightBorder(registry, coordinates, stream)
            if (context.borderRightColor().isPresent)
                stream.setStrokingColor("black")

            // bottom
            if (context.borderBottomColor().isPresent)
                stream.setStrokingColor(context.borderBottomColor().get())
            drawBottomBorder(registry, coordinates, stream)
            if (context.borderBottomColor().isPresent)
                stream.setStrokingColor("black")
        }
    }

    private fun drawTopBorder(registry: CopyOnWriteArraySet<String>, coordinates: Coordinates, stream: PdfScriptStream) {
        val x1 = coordinates.x
        val x2 = x1 + coordinates.width
        val y1 = coordinates.y
        val y2 = coordinates.y - coordinates.height

        if (checkAndAdd(registry, x1, y1, x2, y1))
            stream.drawLine(x1 - 0.5f, y1, x2 + 0.5f, y1)
    }

    private fun drawLeftBorder(registry: CopyOnWriteArraySet<String>, coordinates: Coordinates, stream: PdfScriptStream) {
        val x1 = coordinates.x
        val x2 = x1 + coordinates.width
        val y1 = coordinates.y
        val y2 = coordinates.y - coordinates.height

        if (checkAndAdd(registry, x1, y1, x1, y2))
            stream.drawLine(x1, y1 + 0.5f, x1, y2 - 0.5f)
    }

    private fun drawRightBorder(registry: CopyOnWriteArraySet<String>, coordinates: Coordinates, stream: PdfScriptStream) {
        val x1 = coordinates.x
        val x2 = x1 + coordinates.width
        val y1 = coordinates.y
        val y2 = coordinates.y - coordinates.height

        if (checkAndAdd(registry, x2, y1, x2, y2))
            stream.drawLine(x2, y1 + 0.5f, x2, y2 + 0.5f)
    }

    private fun drawBottomBorder(registry: CopyOnWriteArraySet<String>, coordinates: Coordinates, stream: PdfScriptStream) {
        val x1 = coordinates.x
        val x2 = x1 + coordinates.width
        val y1 = coordinates.y
        val y2 = coordinates.y - coordinates.height

        if (checkAndAdd(registry, x1, y2, x2, y2))
            stream.drawLine(x1 - 0.5f, y2, x2 + 0.5f, y2)
    }

    fun checkAndAdd(registry: CopyOnWriteArraySet<String>, x1: Float, y1: Float, x2: Float, y2: Float): Boolean {
        registry.add("${floor(x1 + 1)}:${floor(y1)}-${floor(x2 + 1)}:${floor(y2)}")
        registry.add("${floor(x1 - 1)}:${floor(y1)}-${floor(x2 - 1)}:${floor(y2)}")
        registry.add("${floor(x1)}:${floor(y1 + 1)}-${floor(x2)}:${floor(y2 + 1)}")
        registry.add("${floor(x1)}:${floor(y1 - 1)}-${floor(x2)}:${floor(y2 - 1)}")
        return registry.add("${round(floor(x1))}:${round(floor(y1))}-${round(floor(x2))}:${round(floor(y2))}")
    }

}
