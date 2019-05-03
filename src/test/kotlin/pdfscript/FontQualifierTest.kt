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

package pdfscript

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pdfscript.PdfScript.Companion.dinA4
import pdfscript.interceptor.RawCommandsInterceptor

class FontQualifierTest {

    @Test
    fun `set a font by a font qualifier`() {
        val interceptor = RawCommandsInterceptor()
        dinA4 {
            text({fontName("courier")},"text")
        }.execute(interceptor)

        val expected = listOf(
                "setFont[Courier, 12.0]",
                "beginText:[]",
                "newLineAtOffset:[70.86614, 760.3216]",
                "showText:[text ]",
                "endText:[]",
                "setFont[Helvetica, 12.0]"
        )

        assertEquals(expected, interceptor.commands)
    }
}

