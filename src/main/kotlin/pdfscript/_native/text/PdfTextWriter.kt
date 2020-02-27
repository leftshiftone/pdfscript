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

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import pdfscript.stream.configurable.font.FontProvider
import java.io.ByteArrayOutputStream
import java.util.concurrent.atomic.AtomicReference

// TODO: add multi-page support
class PdfTextWriter(private val fontProvider:FontProvider = FontProvider()) {

    fun write(list:List<PdfText>):ByteArray {
        PDDocument().use {
            val page = PDPage()
            it.addPage(page)

            val contents = PDPageContentStream(it, page)
            val xPointer = AtomicReference<Float>(page.mediaBox.height)
            val yPointer = AtomicReference<Float>(page.mediaBox.width)
            list.forEach {
                contents.beginText()
                contents.setFont(it.font, it.size)
                contents.newLineAtOffset(it.x1, page.mediaBox.height - it.y1)
                contents.showText(it.text)
                contents.endText()
                xPointer.set(it.x1)
                yPointer.set(it.y1)
            }

            contents.close()

            val bos = ByteArrayOutputStream()
            it.save(bos)

            return bos.toByteArray()
        }
    }

}
