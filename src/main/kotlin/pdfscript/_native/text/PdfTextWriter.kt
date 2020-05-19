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

class PdfTextWriter(private val fontProvider: FontProvider = FontProvider()) {

    fun write(list: List<PdfText>): ByteArray {
        PDDocument().use {
            val pdHeight = AtomicReference<Float>()
            val currPage = AtomicReference(0)
            val contents = AtomicReference<PDPageContentStream>()
            val xPointer = AtomicReference<Float>()
            val yPointer = AtomicReference<Float>()
            list.forEach { e ->
                if (currPage.get() < e.page) {
                    // init page
                    val page = PDPage()
                    it.addPage(page)
                    if (contents.get() != null) contents.get().close()
                    currPage.set(e.page)
                    contents.set(PDPageContentStream(it, page))
                    pdHeight.set(page.mediaBox.height)
                    xPointer.set(page.mediaBox.height)
                    yPointer.set(page.mediaBox.width)
                }

                if (e.text.isNotBlank()) {
                    contents.get().beginText()

                    val font = when (fontProvider.hasFont(e.font.name)) {
                        true -> e.font
                        else -> fontProvider.getFont("helvetica")
                    }

                    contents.get().setFont(font, e.size)
                    contents.get().newLineAtOffset(e.x1, pdHeight.get() - e.y1)
                    contents.get().showText(this.fontProvider.sanitize(font, e.text))
                    contents.get().endText()
                }
                xPointer.set(e.x1)
                yPointer.set(e.y1)
            }

            contents.get().close()

            val bos = ByteArrayOutputStream()
            it.save(bos)

            return bos.toByteArray()
        }
    }

}
