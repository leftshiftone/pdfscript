package pdfscript

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pdfscript.PdfScript.Companion.dinA4
import pdfscript.interceptor.RawCommandsInterceptor

class FontQualifierTest {

    @Test
    fun `set a font by a font qualifier`() {
        val interceptor = RawCommandsInterceptor()
        dinA4 {
            text({fontName("courier")},"text")
        }.execute(interceptor)

        val expected = listOf(
                "setFont[Courier, 12.0]",
                "beginText:[]",
                "newLineAtOffset:[70.86614, 760.3216]",
                "showText:[text ]",
                "endText:[]",
                "setFont[Helvetica, 12.0]"
        )

        assertEquals(expected, interceptor.commands)
    }
}

