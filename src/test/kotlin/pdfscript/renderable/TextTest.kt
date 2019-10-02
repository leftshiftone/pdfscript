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
import pdfscript.stream.configurable.font.PDFontResolver

class TextTest {

    @Test
    fun textWithUmlaute() {
        val interceptor = RawCommandsInterceptor()
        val bytes = dinA4 {
            text("äöü")
        }.execute(interceptor)
    }

    @Test
    fun textWithHatschek() {
        val document = PDDocument()

        val font = loadFont(document, "/font/OpenSans-Regular.ttf")

        val interceptor = RawCommandsInterceptor()
        val bytes = dinA4({ font(font) }) {
            text("č")
        }.execute(interceptor, document)
    }

    @Test
    fun textWithArabicCharacter() {
        val document = PDDocument()
        val font1 = loadFont(document, "/font/OpenSans-Regular.ttf")
        val font2 = loadFont(document, "/font/NotoSansArabic-Regular.ttf")

        val interceptor = RawCommandsInterceptor()
        val bytes = dinA4({ font(PDFontResolver(font1, font2)) }) {
            text("\u0627")
        }.execute(interceptor, document)
    }

    private fun loadFont(document: PDDocument, path:String): PDFont {
        val fontStream = TextTest::class.java.getResourceAsStream(path)
        return PDType0Font.load(document, fontStream)
    }

}
