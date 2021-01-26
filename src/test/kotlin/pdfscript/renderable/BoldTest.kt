/*
 * Copyright 2019 Leftshift One
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pdfscript.renderable

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pdfscript.PdfScript.Companion.dinA4
import pdfscript.interceptor.RawCommandsInterceptor
import pdfscript.stream.configurable.font.FontProvider

class BoldTest {

    @Test
    fun boldText() {
        val document = PDDocument()

        val interceptor = RawCommandsInterceptor()
        dinA4 {
            text("A")
            bold("B")
            text("C")
        }.execute(interceptor, document)

        val expected = listOf(
                "setFont[Helvetica, 12.0]",
                "beginText:[]",
                "newLineAtOffset:[70.86614, 758.7796]",
                "showText:[A ]",
                "endText:[]",
                "setFont[Helvetica-Bold, 12.0]",
                "beginText:[]",
                "newLineAtOffset:[82.20615, 758.5756]",
                "showText:[B ]",
                "endText:[]",
                "setFont[Helvetica, 12.0]",
                "beginText:[]",
                "newLineAtOffset:[94.20615, 758.7796]",
                "showText:[C ]",
                "endText:[]"
        )

        Assertions.assertEquals(expected, interceptor.commands)
    }

    @Test
    fun textWithCustomFont() {
        val document = PDDocument()
        val font1 = loadFont(document, "/font/NotoSans-Regular.ttf")
        val font2 = loadFont(document, "/font/NotoSans-Bold.ttf")

        val fontProvider = FontProvider()
        fontProvider.addFont(font1)
        fontProvider.addFont(font2)

        val interceptor = RawCommandsInterceptor()
        dinA4({ font(font1) }, fontProvider) {
            text("A")
            bold("B")
            text("C")
        }.execute(interceptor, document)

        val expected = listOf(
                "setFont[NotoSans-Regular, 12.0]",
                "beginText:[]",
                "newLineAtOffset:[70.86614, 757.0036]",
                "showText:[A ]",
                "endText:[]",
                "setFont[NotoSans-Bold, 12.0]",
                "beginText:[]",
                "newLineAtOffset:[81.654144, 756.99164]",
                "showText:[B ]",
                "endText:[]",
                "setFont[NotoSans-Regular, 12.0]",
                "beginText:[]",
                "newLineAtOffset:[92.83814, 757.0036]",
                "showText:[C ]",
                "endText:[]"
        )

        Assertions.assertEquals(expected, interceptor.commands)
    }

    @Test
    fun textWithUnderline() {
        val interceptor = RawCommandsInterceptor()
        dinA4 {
            bold({underline(true)}, "Bold Text")
        }.execute(interceptor)

        Assertions.assertEquals(listOf("setFont[Helvetica-Bold, 12.0]",
                "beginText:[]",
                "newLineAtOffset:[70.86614, 758.5756]",
                "showText:[Bold ]",
                "endText:[]",
                "setFont[Helvetica, 12.0]",
                "drawLine(70.86614, 755.7436, 100.86614, 755.7436)",
                "setFont[Helvetica-Bold, 12.0]",
                "beginText:[]",
                "newLineAtOffset:[100.86614, 758.5756]",
                "showText:[Text ]",
                "endText:[]",
                "setFont[Helvetica, 12.0]",
                "drawLine(100.86614, 755.7436, 128.87415, 755.7436)"), interceptor.commands)
    }

    private fun loadFont(document: PDDocument, path: String): PDFont {
        val fontStream = this::class.java.getResourceAsStream(path)
        return PDType0Font.load(document, fontStream)
    }

}
