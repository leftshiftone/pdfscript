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

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import pdfscript.PdfScript.Companion.dinA4
import pdfscript.interceptor.RawCommandsInterceptor
import java.io.File
import java.io.FileOutputStream

class SvgTest {

    @Test
    fun `create a pdf with a classpath svg image`() {
        val interceptor = RawCommandsInterceptor()
        val document = dinA4 {
            svg({SvgTest::class.java.getResourceAsStream("/image.svg")}, 100, 100)
        }.execute(interceptor)
        interceptor.commands.forEach { println(it) }

        //val result = File("D:/tmp/result.pdf")
        //FileOutputStream(result).write(document)

        assertTrue(document.size > 32)
    }
}

