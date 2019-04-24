package pdfscript.model

import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.common.PDRectangle.*
import pdfscript.model.PageMargin.Companion.standard

class PageFormat(val rectangle:PDRectangle) {

    fun page():PDPage = PDPage(rectangle)

    companion object {
        @JvmStatic
        @JvmOverloads
        fun dinA0(margin: PageMargin = standard()) = PageFormat(A0)
        @JvmStatic
        @JvmOverloads
        fun dinA1(margin: PageMargin = standard()) = PageFormat(A1)
        @JvmStatic
        @JvmOverloads
        fun dinA2(margin: PageMargin = standard()) = PageFormat(A2)
        @JvmStatic
        @JvmOverloads
        fun dinA3(margin: PageMargin = standard()) = PageFormat(A3)
        @JvmStatic
        @JvmOverloads
        fun dinA4(margin: PageMargin = standard()) = PageFormat(A4)
        @JvmStatic
        @JvmOverloads
        fun dinA5(margin: PageMargin = standard()) = PageFormat(A5)
        @JvmStatic
        @JvmOverloads
        fun dinA6(margin: PageMargin = standard()) = PageFormat(A6)
    }

    fun height() = rectangle.height
    fun width() = rectangle.width

}
