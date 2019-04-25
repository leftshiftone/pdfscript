package pdfscript.renderable

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pdfscript.PdfScript.Companion.dinA4
import pdfscript.interceptor.RawCommandsInterceptor

class SuperscriptTest {

    @Test
    fun `create a superscript text`() {
        val interceptor = RawCommandsInterceptor()
        dinA4 {
            text("text")
            superscript("superscript")
        }.execute(interceptor)

        val expected = listOf(
                "setFont[Helvetica, 12.0]",
                "beginText:[]",
                "newLineAtOffset:[70.86614, 758.7796]",
                "showText:[text ]",
                "endText:[]",
                "setFont[Helvetica, 7.2000003]",
                "beginText:[]",
                "newLineAtOffset:[93.54614, 764.2796]",
                "showText:[superscript]",
                "endText:[]",
                "setFont[Helvetica, 12.0]"
        )

        assertEquals(expected, interceptor.commands)
    }
}

