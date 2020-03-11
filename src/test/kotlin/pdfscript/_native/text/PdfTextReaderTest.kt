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

package pdfscript._native.text

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileOutputStream


class PdfTextReaderTest {

    @Test
    fun test() {
        val stream = this::class.java.getResourceAsStream("/pdf/result.pdf")
        val incoming = PdfTextReader().read(stream)
        val outgoing = PdfTextWriter().write(incoming)

        Assertions.assertNotNull(outgoing)
        Assertions.assertTrue(outgoing.isNotEmpty())
    }

    @Test
    fun multipage() {
        val stream = this::class.java.getResourceAsStream("/pdf/result.pdf")
        val incoming = PdfTextReader().read(stream)
        val outgoing = PdfTextWriter().write(incoming)

        val fos = FileOutputStream(File.createTempFile("multipage", ".pdf"))
        fos.write(outgoing)

        Assertions.assertNotNull(outgoing)
        Assertions.assertTrue(outgoing.isNotEmpty())
    }

}
