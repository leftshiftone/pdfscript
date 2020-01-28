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
import java.io.FileOutputStream

class TextTest {

    @Test
    fun textWithUmlaute() {
        val interceptor = RawCommandsInterceptor()
        dinA4 {
            text("Förrest Gümp")
        }.execute(interceptor)

        Assertions.assertEquals(listOf(
                "setFont[Helvetica, 12.0]",
                "beginText:[]",
                "newLineAtOffset:[70.86614, 758.7796]",
                "showText:[Förrest ]",
                "endText:[]",
                "beginText:[]",
                "newLineAtOffset:[112.20615, 758.7796]",
                "showText:[Gümp ]",
                "endText:[]"
        ), interceptor.commands)
    }

    @Test
    fun textWithHatschek() {
        val document = PDDocument()
        val font = loadFont(document, "/font/NotoSans-Regular.ttf")

        val interceptor = RawCommandsInterceptor()
        dinA4({ font(font) }) {
            text("Fórrest Gûmp")
        }.execute(interceptor, document)

        Assertions.assertEquals(listOf(
                "setFont[NotoSans-Regular, 12.0]",
                "beginText:[]",
                "newLineAtOffset:[70.86614, 757.0036]",
                "showText:[Fórrest ]",
                "endText:[]",
                "beginText:[]",
                "newLineAtOffset:[114.234146, 757.0036]",
                "showText:[Gûmp ]",
                "endText:[]"
        ), interceptor.commands)
    }

    @Test
    fun textWithArabicCharacter() {
        val document = PDDocument()
        val font = loadFont(document, "/font/NotoSansArabic-Regular.ttf")
        val fontProvider = FontProvider()
        fontProvider.addFont(font, '؟')

        val interceptor = RawCommandsInterceptor()
        dinA4({ font(font) }, fontProvider) {
            text("Fórrest اڵڶڷ Gûmp")
        }.execute(interceptor, document)

        Assertions.assertEquals(listOf(
                "setFont[NotoSansArabic-Regular, 12.0]",
                "beginText:[]",
                "newLineAtOffset:[70.86614, 757.40564]",
                "showText:[؟؟؟؟؟؟؟ ]",
                "endText:[]",
                "beginText:[]",
                "newLineAtOffset:[110.44214, 757.40564]",
                "showText:[اڵڶڷ ]",
                "endText:[]",
                "beginText:[]",
                "newLineAtOffset:[141.43814, 757.40564]",
                "showText:[؟؟؟؟ ]",
                "endText:[]"
        ), interceptor.commands)
    }

    @Test
    fun textWithHebrewCharacter() {
        val document = PDDocument()
        val font = loadFont(document, "/font/NotoSansHebrew-Regular.ttf")
        val fontProvider = FontProvider()
        fontProvider.addFont(font, '₪')

        val interceptor = RawCommandsInterceptor()
        dinA4({ font(font) }, fontProvider) {
            text("Fórrest לם Gûmp")
        }.execute(interceptor, document)

        Assertions.assertEquals(listOf(
                "setFont[NotoSansHebrew-Regular, 12.0]",
                "beginText:[]",
                "newLineAtOffset:[70.86614, 759.0136]",
                "showText:[₪₪₪₪₪₪₪ ]",
                "endText:[]",
                "beginText:[]",
                "newLineAtOffset:[140.34613, 759.0136]",
                "showText:[לם ]",
                "endText:[]",
                "beginText:[]",
                "newLineAtOffset:[157.41013, 759.0136]",
                "showText:[₪₪₪₪ ]",
                "endText:[]"
        ), interceptor.commands)
    }

    @Test
    fun textWithDifferentFontSizes() {
        val interceptor = RawCommandsInterceptor()
        val bytes = dinA4 {
            text("A")
            text({ fontSize(10) }, "B")
            text({ fontSize(8) }, "C")
            text({ fontSize(6) }, "D")
            text({ fontSize(8) }, "E")
            text({ fontSize(10) }, "F")
            text({ fontSize(12) }, "G")
        }.execute(interceptor)

        FileOutputStream("D:/tmp/result.pdf").write(bytes)

        interceptor.commands.forEach { println(it) }

        Assertions.assertEquals(listOf(
                "setFont[Helvetica, 12.0]",
                "beginText:[]",
                "newLineAtOffset:[70.86614, 758.7796]",
                "showText:[A ]",
                "endText:[]",
                "setFont[Helvetica, 10.0]",
                "beginText:[]",
                "newLineAtOffset:[82.20615, 758.7796]",
                "showText:[B ]",
                "endText:[]",
                "setFont[Helvetica, 12.0]",
                "setFont[Helvetica, 8.0]",
                "beginText:[]",
                "newLineAtOffset:[91.65614, 758.77966]",
                "showText:[C ]",
                "endText:[]",
                "setFont[Helvetica, 12.0]",
                "setFont[Helvetica, 6.0]",
                "beginText:[]",
                "newLineAtOffset:[99.65614, 758.7796]",
                "showText:[D ]",
                "endText:[]",
                "setFont[Helvetica, 12.0]",
                "setFont[Helvetica, 8.0]",
                "beginText:[]",
                "newLineAtOffset:[105.65614, 758.77966]",
                "showText:[E ]",
                "endText:[]",
                "setFont[Helvetica, 12.0]",
                "setFont[Helvetica, 10.0]",
                "beginText:[]",
                "newLineAtOffset:[113.21614, 758.7796]",
                "showText:[F ]",
                "endText:[]",
                "setFont[Helvetica, 12.0]",
                "beginText:[]",
                "newLineAtOffset:[122.10614, 758.7796]",
                "showText:[G ]",
                "endText:[]"
                ), interceptor.commands)
    }

    private fun loadFont(document: PDDocument, path: String): PDFont {
        val fontStream = this::class.java.getResourceAsStream(path)
        return PDType0Font.load(document, fontStream)
    }
}
