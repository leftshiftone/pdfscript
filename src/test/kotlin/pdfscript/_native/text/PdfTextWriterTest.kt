package pdfscript._native.text

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pdfscript.stream.configurable.font.FontProvider

class PdfTextWriterTest {

    @Test
    fun test() {
        val map = listOf(PdfText("a\u000A", 1f,1f,1f,1f, 12f, FontProvider().getFont("Helvetica"), 1 ))
        val outgoing = PdfTextWriter().write(map)

        Assertions.assertNotNull(outgoing)
        Assertions.assertTrue(outgoing.isNotEmpty())
    }
}