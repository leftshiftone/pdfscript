package pdfscript.stream.renderable

import org.apache.pdfbox.pdmodel.font.PDFont
import pdfscript.stream.Evaluation

@Deprecated("use the context configuration attributes instead")
class PdfsFont(val font: PDFont, val size: Float) : AbstractWritable() {

    override fun evaluate(context: Context): List<Evaluation> {
        return listOf(Evaluation({0f}, {0f}) { stream, coordinates ->
            stream.setFont(font, size)
        })
    }

}
