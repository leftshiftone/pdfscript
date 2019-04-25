package pdfscript.renderable

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pdfscript.PdfScript.Companion.dinA4
import pdfscript.interceptor.RawCommandsInterceptor

class SubscriptTest {

    @Test
    fun `create a subscript text`() {
        val interceptor = RawCommandsInterceptor()
        dinA4 {
            text("text")
            subscript("subscript")
        }.execute(interceptor)

        val expected = listOf(
                "setFont[Helvetica, 12.0]",
                "beginText:[]",
                "newLineAtOffset:[70.86614, 758.7796]",
                "showText:[text ]",
                "endText:[]",
                "setFont[Helvetica, 7.2000003]",
                "beginText:[]",
                "newLineAtOffset:[93.54614, 775.76764]",
                "showText:[subscript]",
                "endText:[]",
                "setFont[Helvetica, 12.0]"
        )

        assertEquals(expected, interceptor.commands)
    }
}

