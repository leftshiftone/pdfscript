package pdfscript.stream.renderable

import pdfscript.stream.Evaluation
import pdfscript.stream.configurable.Context
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.URL

class Svg (val supplier: () -> InputStream, val width: Float, val height: Float) : AbstractWritable() {

    constructor(url: URL, width: Float, height: Float) : this(url::openStream, width, height)
    constructor(bytes: ByteArray, width: Float, height: Float) : this({ByteArrayInputStream(bytes)}, width, height)

    override fun evaluate(context: Context): List<Evaluation> {
        return listOf(Evaluation({width}, {height}) { stream, coordinates ->
            coordinates.moveY(-height)
            stream.drawSvg(this.supplier(), width.toInt(), height.toInt(), coordinates.x, coordinates.y)
            coordinates.moveY(height)

            coordinates.moveX(width)
        })
    }

}
