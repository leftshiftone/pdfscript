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

    @Test
    fun tableWithBoldText() {
        val interceptor = RawCommandsInterceptor()
        dinA4 {
            table({ border("none");background("#E0E0E0") }) {
                row({ fontSize(8) }) {
                    col { text("die zeile hat richtiges spacing") }
                    col({ align("right") }) { text("die zeile hat richtiges spacing") }
                }
                row({ fontSize(8) }) {
                    col { bold("die zeile sollte gleich hoch sein wie die obere") }
                    col({ align("right") }) { bold("test") }
                }
            }
        }.execute(interceptor)

        val expected = listOf(
                "setNonStrokingColor(#E0E0E0)",
                "drawRect(70.86614, 760.77563, 226.77167, 9.248)",
                "setNonStrokingColor(black)",
                "setFont[Helvetica, 8.0]",
                "beginText:[]",
                "newLineAtOffset:[75.86614, 762.52765]",
                "showText:[die ]",
                "endText:[]",
                "beginText:[]",
                "newLineAtOffset:[88.762146, 762.52765]",
                "showText:[zeile ]",
                "endText:[]",
                "beginText:[]",
                "newLineAtOffset:[107.43414, 762.52765]",
                "showText:[hat ]",
                "endText:[]",
                "beginText:[]",
                "newLineAtOffset:[120.778145, 762.52765]",
                "showText:[richtiges ]",
                "endText:[]",
                "beginText:[]",
                "newLineAtOffset:[152.78615, 762.52765]",
                "showText:[spacing ]",
                "endText:[]",
                "setNonStrokingColor(#E0E0E0)",
                "drawRect(297.63782, 760.77563, 226.77167, 9.248)",
                "setNonStrokingColor(black)",
                "beginText:[]",
                "newLineAtOffset:[412.69748, 762.52765]",
                "showText:[die ]",
                "endText:[]",
                "beginText:[]",
                "newLineAtOffset:[425.59348, 762.52765]",
                "showText:[zeile ]",
                "endText:[]",
                "beginText:[]",
                "newLineAtOffset:[444.26547, 762.52765]",
                "showText:[hat ]",
                "endText:[]",
                "beginText:[]",
                "newLineAtOffset:[457.60947, 762.52765]",
                "showText:[richtiges ]",
                "endText:[]",
                "beginText:[]",
                "newLineAtOffset:[489.61746, 762.52765]",
                "showText:[spacing ]",
                "endText:[]",
                "setNonStrokingColor(#E0E0E0)",
                "drawRect(70.86614, 751.0056, 226.77167, 9.52)",
                "setNonStrokingColor(black)",
                "setFont[Helvetica-Bold, 8.0]",
                "beginText:[]",
                "newLineAtOffset:[75.86614, 752.8936]",
                "showText:[die ]",
                "endText:[]",
                "setFont[Helvetica, 8.0]",
                "setFont[Helvetica-Bold, 8.0]",
                "beginText:[]",
                "newLineAtOffset:[89.65015, 752.8936]",
                "showText:[zeile ]",
                "endText:[]",
                "setFont[Helvetica, 8.0]",
                "setFont[Helvetica-Bold, 8.0]",
                "beginText:[]",
                "newLineAtOffset:[109.21815, 752.8936]",
                "showText:[sollte ]",
                "endText:[]",
                "setFont[Helvetica, 8.0]",
                "setFont[Helvetica-Bold, 8.0]",
                "beginText:[]",
                "newLineAtOffset:[132.33815, 752.8936]",
                "showText:[gleich ]",
                "endText:[]",
                "setFont[Helvetica, 8.0]",
                "setFont[Helvetica-Bold, 8.0]",
                "beginText:[]",
                "newLineAtOffset:[157.68214, 752.8936]",
                "showText:[hoch ]",
                "endText:[]",
                "setFont[Helvetica, 8.0]",
                "setFont[Helvetica-Bold, 8.0]",
                "beginText:[]",
                "newLineAtOffset:[179.01814, 752.8936]",
                "showText:[sein ]",
                "endText:[]",
                "setFont[Helvetica, 8.0]",
                "setFont[Helvetica-Bold, 8.0]",
                "beginText:[]",
                "newLineAtOffset:[197.25014, 752.8936]",
                "showText:[wie ]",
                "endText:[]",
                "setFont[Helvetica, 8.0]",
                "setFont[Helvetica-Bold, 8.0]",
                "beginText:[]",
                "newLineAtOffset:[212.37013, 752.8936]",
                "showText:[die ]",
                "endText:[]",
                "setFont[Helvetica, 8.0]",
                "setFont[Helvetica-Bold, 8.0]",
                "beginText:[]",
                "newLineAtOffset:[226.15413, 752.8936]",
                "showText:[obere ]",
                "endText:[]",
                "setFont[Helvetica, 8.0]",
                "setNonStrokingColor(#E0E0E0)",
                "drawRect(297.63782, 751.0056, 226.77167, 9.52)",
                "setNonStrokingColor(black)",
                "setFont[Helvetica-Bold, 8.0]",
                "beginText:[]",
                "newLineAtOffset:[502.9615, 752.8936]",
                "showText:[test ]",
                "endText:[]",
                "setFont[Helvetica, 8.0]"
        )
        Assertions.assertEquals(expected, interceptor.commands)
    }

}
