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

class DrawLineTest {

    @Test
    fun drawLines() {
        val document = PDDocument()

        val interceptor = RawCommandsInterceptor()
        PdfScript.dinA4 {
            withCanvas {
                drawLine(0, -10, 10, -10)
                drawLine(10, -20, 20, -20)
                drawLine(20, -30, 30, -30)
            }
        }.execute(interceptor, document)

        val expected = listOf(
                "drawLine(0.0, 831.8898, 10.0, 831.8898)",
                "drawLine(10.0, 821.8898, 20.0, 821.8898)",
                "drawLine(20.0, 811.8898, 30.0, 811.8898)"
        )

        Assertions.assertEquals(expected, interceptor.commands)
    }

}
