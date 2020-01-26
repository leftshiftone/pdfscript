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
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.text.TextPosition
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.io.OutputStreamWriter

/**
 * Parser implementation which extracts each text in the pdf as well as their bounding boxes.
 */
class PdfTextReader() {

    fun read(path:String) = read(File(path).inputStream())

    fun read(stream:InputStream):List<PdfText> {
        PDDocument.load(stream).use {
            val stripper = CustomPDFTextStripper()
            stripper.sortByPosition = true
            stripper.startPage = 0
            stripper.endPage = it.numberOfPages
            val dummy = OutputStreamWriter(ByteArrayOutputStream())
            stripper.writeText(it, dummy)

            return stripper.list
        }
    }

    private class CustomPDFTextStripper : PDFTextStripper() {

        val list = ArrayList<PdfText>()

        override fun writeString(string: String, textPositions: List<TextPosition>) {
            val _1st = textPositions.first()
            val _nth = textPositions.last()

            list.add(PdfText(string,
                    _1st.x,
                    _1st.y,
                    _nth.x + _nth.width,
                    _nth.y + _nth.height,
                    _1st.fontSize,
                    _1st.font))
        }
    }

}
