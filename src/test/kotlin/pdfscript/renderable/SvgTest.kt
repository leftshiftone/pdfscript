package pdfscript.renderable

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import pdfscript.PdfScript.Companion.dinA4
import pdfscript.interceptor.RawCommandsInterceptor
import java.io.File
import java.io.FileOutputStream

class SvgTest {

    @Test
    fun `create a pdf with a classpath svg image`() {
        val interceptor = RawCommandsInterceptor()
        val document = dinA4 {
            svg({SvgTest::class.java.getResourceAsStream("/image.svg")}, 100, 100)
        }.execute(interceptor)
        interceptor.commands.forEach { println(it) }

        val result = File("D:/tmp/result.pdf")
        FileOutputStream(result).write(document)

        assertTrue(result.exists())
        assertTrue(document.size > 32)
    }
}

