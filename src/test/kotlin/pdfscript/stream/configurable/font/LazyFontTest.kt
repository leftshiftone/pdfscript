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

package pdfscript.stream.configurable.font

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pdfscript.PdfScript.Companion.dinA4
import pdfscript.interceptor.RawCommandsInterceptor
import java.util.function.Supplier

class LazyFontTest {

    @Test
    fun textWithCustomFont() {
        val document = PDDocument()
        val font1 = loadFont(document, "/font/NotoSans-Regular.ttf")
        val font2 = loadFont(document, "/font/NotoSans-Bold.ttf")

        val fontProvider = FontProvider()
        fontProvider.addFont(font1)
        fontProvider.addFont(font2)

        val interceptor = RawCommandsInterceptor()
        dinA4({ font(Supplier { font1 }) }, fontProvider) {
            paragraph {
                text("A")
                bold("B")
                text("C")
            }
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

    private fun loadFont(document: PDDocument, path: String): PDFont {
        val fontStream = this::class.java.getResourceAsStream(path)
        return PDType0Font.load(document, fontStream)
    }

}
