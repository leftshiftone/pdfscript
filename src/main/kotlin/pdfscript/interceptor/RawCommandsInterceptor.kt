package pdfscript.interceptor

import org.apache.pdfbox.pdmodel.font.PDFont
import java.net.URL

class RawCommandsInterceptor : PdfsInterceptor() {

    val commands = ArrayList<String>()

    override fun beginText() {
        commands.add("beginText:[]")
    }

    override fun endText() {
        commands.add("endText:[]")
    }

    override fun showText(text: String) {
        commands.add("showText:[$text]")
    }

    override fun setFont(font: PDFont, size: Float) {
        commands.add("setFont[${font.fontDescriptor.fontName}, $size]")
    }

    override fun newLineAtOffset(x: Float, y: Float) {
        commands.add("newLineAtOffset:[$x, $y]")
    }

    override fun drawImage(url: URL, x:Float, y:Float) {
        commands.add("drawImage(${url.path}, $x, $y]")
    }

    override fun setStrokingColor(colorStr: String) {
        commands.add("setStrokingColor($colorStr)")
    }

    override fun setNonStrokingColor(colorStr: String) {
        commands.add("setNonStrokingColor($colorStr)")
    }

    override open fun drawLine(x1:Float, y1:Float, x2:Float, y2:Float) {
        commands.add("drawLine($x1, $y1, $x2, $y2)")
    }
}
