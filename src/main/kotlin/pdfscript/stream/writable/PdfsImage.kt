package pdfscript.stream.writable

import pdfscript.stream.Evaluation
import java.net.URL

class PdfsImage(val url: URL, val width: Float, val height: Float) : AbstractWritable() {

    override fun evaluate(context: Context): List<Evaluation> {
        // val mmWidth = width / (1 / (10 * 2.54f) * 72)
        // val mmHeight = height / (1 / (10 * 2.54f) * 72)

        return listOf(Evaluation({width}, {height}) { stream, coordinates ->
            coordinates.moveY(-height)
            stream.drawImage(url, width.toInt(), height.toInt(), coordinates.x, coordinates.y)
            coordinates.moveY(height)

            coordinates.moveX(width)
        })
    }

}
