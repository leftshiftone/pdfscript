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
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.junit.jupiter.api.Test
import pdfscript.PdfScript.Companion.dinA4
import pdfscript.interceptor.RawCommandsInterceptor

class TextTest {

    @Test
    fun textWithUmlaute() {
        val interceptor = RawCommandsInterceptor()
        val bytes = dinA4 {
            text("äöü")
        }.execute(interceptor)

        // val fos = FileOutputStream(File("D:/tmp/result.pdf"))
        // fos.write(bytes)
        // fos.close()
    }

    @Test
    fun textWithHatschek() {
        val document = PDDocument()

        val fontStream = TextTest::class.java.getResourceAsStream("/font/ArialUnicodeMS.ttf")
        val font = PDType0Font.load(document, fontStream)

        val interceptor = RawCommandsInterceptor()
        val bytes = dinA4({fontName(font)}) {
            text("č")
        }.execute(interceptor, document)

        // val fos = FileOutputStream(File("D:/tmp/result.pdf"))
        // fos.write(bytes)
        // fos.close()
    }

}
