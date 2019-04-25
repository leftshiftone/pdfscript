package pdfscript.model

import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.common.PDRectangle.*

class PageFormat(val rectangle:PDRectangle) {

    fun page():PDPage = PDPage(rectangle)

    companion object {
        @JvmStatic
        fun dinA0() = PageFormat(A0)
        @JvmStatic
        fun dinA1() = PageFormat(A1)
        @JvmStatic
        fun dinA2() = PageFormat(A2)
        @JvmStatic
        fun dinA3() = PageFormat(A3)
        @JvmStatic
        fun dinA4() = PageFormat(A4)
        @JvmStatic
        fun dinA5() = PageFormat(A5)
        @JvmStatic
        fun dinA6() = PageFormat(A6)
    }

    fun height() = rectangle.height
    fun width() = rectangle.width

}
