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

package pdfscript.renderable.canvas

import org.apache.pdfbox.pdmodel.PDDocument
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pdfscript.PdfScript
import pdfscript.interceptor.RawCommandsInterceptor

class DrawTextTest {

    @Test
    fun drawTexts() {
        val document = PDDocument()

        val interceptor = RawCommandsInterceptor()
        PdfScript.dinA4 {
            withCanvas {
                drawText("Text A", 0, -20)
                drawText("Text B", 10, -40)
                drawText("Text C", 20, -60)
            }
        }.execute(interceptor, document)

        val expected = listOf(
                "setFont[Helvetica, 12.0]",
                "beginText:[]",
                "newLineAtOffset:[0.0, 821.8898]",
                "showText:[Text A]",
                "endText:[]",
                "beginText:[]",
                "newLineAtOffset:[10.0, 801.8898]",
                "showText:[Text B]",
                "endText:[]",
                "beginText:[]",
                "newLineAtOffset:[20.0, 781.8898]",
                "showText:[Text C]",
                "endText:[]"
        )

        Assertions.assertEquals(expected, interceptor.commands)
    }

}
