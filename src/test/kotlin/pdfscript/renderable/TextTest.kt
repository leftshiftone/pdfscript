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
import org.junit.jupiter.api.Test
import pdfscript.PdfScript.Companion.dinA4
import pdfscript.interceptor.RawCommandsInterceptor
import pdfscript.stream.configurable.font.FontProvider

class TextTest {

    @Test
    fun textWithUmlaute() {
        val interceptor = RawCommandsInterceptor()
        val bytes = dinA4 {
            text("Förrest Gümp")
        }.execute(interceptor)
    }

    @Test
    fun textWithHatschek() {
        val document = PDDocument()
        val font = loadFont(document, "/font/NotoSans-Regular.ttf")

        val interceptor = RawCommandsInterceptor()
        val bytes = dinA4({ font(font) }) {
            text("Fórrest Gûmp")
        }.execute(interceptor, document)
    }

    @Test
    fun textWithArabicCharacter() {
        val document = PDDocument()
        val font = loadFont(document, "/font/NotoSansArabic-Regular.ttf")
        val fontProvider = FontProvider()
        fontProvider.addFont(font, '؟')

        val interceptor = RawCommandsInterceptor()
        val bytes = dinA4({ font(font) }, fontProvider) {
            text("Fórrest اڵڶڷ Gûmp")
        }.execute(interceptor, document)
    }

    @Test
    fun textWithHebrewCharacter() {
        val document = PDDocument()
        val font = loadFont(document, "/font/NotoSansHebrew-Regular.ttf")
        val fontProvider = FontProvider()
        fontProvider.addFont(font, '₪')

        val interceptor = RawCommandsInterceptor()
        val bytes = dinA4({ font(font) }, fontProvider) {
            text("Fórrest לם Gûmp")
        }.execute(interceptor, document)
    }

    private fun loadFont(document: PDDocument, path: String): PDFont {
        val fontStream = this::class.java.getResourceAsStream(path)
        return PDType0Font.load(document, fontStream)
    }
}
