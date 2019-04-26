package pdfscript.interceptor

import org.apache.pdfbox.pdmodel.font.PDFont
import java.net.URL

open class Interceptor {

    open fun beginText() {
        // do nothing
    }

    open fun endText() {
        // do nothing
    }

    open fun showText(text:String) {
        // do nothing
    }

    open fun setFont(font: PDFont, size:Float) {
        // do nothing
    }

    open fun newLineAtOffset(x:Float, y:Float) {
        // do nothing
    }

    open fun drawImage(url: URL, x:Float, y:Float) {
        // do nothing
    }

    open fun close() {
        // do nothing
    }

    open fun setStrokingColor(colorStr: String) {
        // do nothing
    }

    open fun setNonStrokingColor(colorStr: String) {
        // do nothing
    }

    open fun drawLine(x1:Float, y1:Float, x2:Float, y2:Float) {
        // do nothing
    }

}
