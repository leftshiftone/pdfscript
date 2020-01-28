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

        assertTrue(interceptor.commands.contains("drawLine(70.36614, 770.0236, 524.9095, 770.0236)"))
        assertTrue(interceptor.commands.contains("drawLine(70.86614, 770.5236, 70.86614, 686.2916)"))
        assertTrue(interceptor.commands.contains("drawLine(524.4095, 770.5236, 524.4095, 687.2916)"))
        assertTrue(interceptor.commands.contains("drawLine(70.36614, 686.7916, 524.9095, 686.7916)"))
    }

    @Test
    fun tooLongTable() {
        val interceptor = RawCommandsInterceptor()
        dinA4 {
            withHeader { table { row { col { text("header") } } } }
            withFooter { table { row { col { text("footer") } } } }
            table {
                row {
                    col {
                        text("Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquid ex ea commodi consequat. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                        text("Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquid ex ea commodi consequat. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                    }
                }
                row {
                    col {
                        text("Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquid ex ea commodi consequat. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                        text("Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquid ex ea commodi consequat. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                    }
                }
                row {
                    col {
                        text("Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquid ex ea commodi consequat. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                        text("Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquid ex ea commodi consequat. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                    }
                }
                row {
                    col {
                        text("Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquid ex ea commodi consequat. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                        text("Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquid ex ea commodi consequat. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                    }
                }
                row {
                    col {
                        text("Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquid ex ea commodi consequat. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                        text("Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquid ex ea commodi consequat. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                    }
                }
            }
        }.execute(interceptor)

        assertTrue(interceptor.commands.contains("drawLine(70.36614, 806.4567, 524.9095, 806.4567)"))
        assertTrue(interceptor.commands.contains("drawLine(70.86614, 806.9567, 70.86614, 792.0847)"))
        assertTrue(interceptor.commands.contains("drawLine(524.4095, 806.9567, 524.4095, 793.0847)"))
        assertTrue(interceptor.commands.contains("drawLine(70.36614, 792.5847, 524.9095, 792.5847)"))
        assertTrue(interceptor.commands.contains("drawLine(70.36614, 70.86614, 524.9095, 70.86614)"))
        assertTrue(interceptor.commands.contains("drawLine(70.86614, 71.36614, 70.86614, 56.49414)"))
        assertTrue(interceptor.commands.contains("drawLine(524.4095, 71.36614, 524.4095, 57.49414)"))
        assertTrue(interceptor.commands.contains("drawLine(70.36614, 56.99414, 524.9095, 56.99414)"))
        assertTrue(interceptor.commands.contains("drawLine(70.36614, 770.0236, 524.9095, 770.0236)"))
        assertTrue(interceptor.commands.contains("drawLine(70.86614, 770.5236, 70.86614, 616.93164)"))
        assertTrue(interceptor.commands.contains("drawLine(524.4095, 770.5236, 524.4095, 617.93164)"))
        assertTrue(interceptor.commands.contains("drawLine(70.36614, 617.43164, 524.9095, 617.43164)"))
        assertTrue(interceptor.commands.contains("drawLine(70.86614, 617.68164, 70.86614, 464.08966)"))
        assertTrue(interceptor.commands.contains("drawLine(524.4095, 617.68164, 524.4095, 465.08966)"))
        assertTrue(interceptor.commands.contains("drawLine(70.36614, 464.58966, 524.9095, 464.58966)"))
        assertTrue(interceptor.commands.contains("drawLine(70.86614, 465.08966, 70.86614, 311.49768)"))
        assertTrue(interceptor.commands.contains("drawLine(524.4095, 465.08966, 524.4095, 312.49768)"))
        assertTrue(interceptor.commands.contains("drawLine(70.36614, 311.99768, 524.9095, 311.99768)"))
        assertTrue(interceptor.commands.contains("drawLine(70.86614, 312.24768, 70.86614, 158.65569)"))
        assertTrue(interceptor.commands.contains("drawLine(524.4095, 312.24768, 524.4095, 159.65569)"))
        assertTrue(interceptor.commands.contains("drawLine(70.36614, 159.15569, 524.9095, 159.15569)"))
        assertTrue(interceptor.commands.contains("drawLine(70.36614, 806.4567, 524.9095, 806.4567)"))
        assertTrue(interceptor.commands.contains("drawLine(70.86614, 806.9567, 70.86614, 792.0847)"))
        assertTrue(interceptor.commands.contains("drawLine(524.4095, 806.9567, 524.4095, 793.0847)"))
        assertTrue(interceptor.commands.contains("drawLine(70.36614, 792.5847, 524.9095, 792.5847)"))
        assertTrue(interceptor.commands.contains("drawLine(70.36614, 70.86614, 524.9095, 70.86614)"))
        assertTrue(interceptor.commands.contains("drawLine(70.86614, 71.36614, 70.86614, 56.49414)"))
        assertTrue(interceptor.commands.contains("drawLine(524.4095, 71.36614, 524.4095, 57.49414)"))
        assertTrue(interceptor.commands.contains("drawLine(70.36614, 56.99414, 524.9095, 56.99414)"))
        assertTrue(interceptor.commands.contains("drawLine(70.36614, 770.0236, 524.9095, 770.0236)"))
        assertTrue(interceptor.commands.contains("drawLine(70.86614, 770.5236, 70.86614, 616.93164)"))
        assertTrue(interceptor.commands.contains("drawLine(524.4095, 770.5236, 524.4095, 617.93164)"))
        assertTrue(interceptor.commands.contains("drawLine(70.36614, 617.43164, 524.9095, 617.43164)"))
    }

}
