package pdfscript.model

class PageMargin(val top:Float, val bottom:Float, val left:Float, val right:Float, val header:Float, val footer:Float) {

    companion object {
        /** user space units per inch  */
        private val POINTS_PER_INCH = 72f
        /** user space units per millimeter  */
        private val POINTS_PER_MM = 1 / (10 * 2.54f) * POINTS_PER_INCH

        @JvmStatic
        fun standard() = PageMargin(25 * POINTS_PER_MM,
                25 * POINTS_PER_MM,
                25 * POINTS_PER_MM,
                25 * POINTS_PER_MM,
                12.5f * POINTS_PER_MM,
                12.5f * POINTS_PER_MM)
    }

}
