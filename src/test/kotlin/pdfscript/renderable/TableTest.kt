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

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pdfscript.PdfScript.Companion.dinA4
import pdfscript.interceptor.RawCommandsInterceptor

class TableTest {

    @Test
    fun tooLongColumn() {
        val interceptor = RawCommandsInterceptor()
        dinA4 {
            table {
                row {
                    col {
                        text("Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquid ex ea commodi consequat. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                    }
                }
            }
        }.execute(interceptor)

        Assertions.assertTrue(interceptor.commands.contains("drawLine(70.36614, 770.0236, 524.9095, 770.0236)"))
        Assertions.assertTrue(interceptor.commands.contains("drawLine(70.86614, 770.5236, 70.86614, 700.16364)"))
        Assertions.assertTrue(interceptor.commands.contains("drawLine(524.4095, 770.5236, 524.4095, 701.16364)"))
        Assertions.assertTrue(interceptor.commands.contains("drawLine(70.36614, 700.66364, 524.9095, 700.66364)"))
    }

}
