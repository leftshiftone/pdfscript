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

import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import pdfscript.PdfScript.Companion.dinA4
import pdfscript.interceptor.RawCommandsInterceptor
import java.io.File
import java.io.FileOutputStream

/**
 * @author Michael Mair
 */
class PdfScriptTest {

    @Test
    fun `pdf is generated from DSL`() {
        val interceptor = RawCommandsInterceptor()
        val document = dinA4({fontSize(9)}) {
            withHeader {
                table({ border("none") }) {
                    row {
                        col {
                            image("https://upload.wikimedia.org/wikipedia/en/6/67/Forrest_Gump_poster.jpg", 150, 200)
                        }
                        col({ align("right") }) {
                            image("https://upload.wikimedia.org/wikipedia/commons/6/6d/ForrestGump-Jenny-Boat-2055.jpg", 302, 200)
                        }
                    }
                }
            }
            withFooter({fontSize(7)}) {
                table({ border("none");background("#E0E0E0") }) {
                    row({ fontName(PDType1Font.HELVETICA_BOLD); fontSize(8) }) {
                        col { text("Lorem ipsum") }
                        col({ align("right") }) { text({ align("right") }, "Page x of x") }
                    }
                }
                paragraph { tab() }
                paragraph({ foreground("gray");align("center") }) {
                    text("Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquid ex ea commodi consequat. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                }
            }

            paragraph { tab() }
            paragraph { tab() }
            paragraph { tab() }
            paragraph { tab() }

            table({ border("none") }) {
                row {
                    col {
                        paragraph { text("Bubba Gump Shrimps") }
                        paragraph { text("Forrest Gump") }
                        paragraph { text("301 Santa Monica Pier, Building 9 ") }
                        paragraph { text("Santa Monica, CA 90401") }
                    }
                }
            }

            paragraph { tab() }
            paragraph { tab() }

            paragraph {
                text(({ fontSize(12); fontName(PDType1Font.HELVETICA_BOLD) }), "Subject")
                linebreak()
                linebreak()
                text("Dear Mr. Gump")
                linebreak()
                linebreak()
                text("Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquid ex ea commodi consequat. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.")
                linebreak()
                linebreak()
                linebreak()
                text("Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquid ex ea commodi consequat. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
            }

            paragraph { tab() }
            paragraph { tab() }
            paragraph { tab() }
            paragraph { tab() }

            table({ align("center");borderLeft("white");borderRight("white");borderTop("white") }) {
                row({ fontName(PDType1Font.HELVETICA_BOLD); borderBottom("#C00000");paddingBottom(5) }) {
                    col { text("One") }
                    col { text("Two") }
                    col { text("Three") }
                }
                row({ paddingTop(5);paddingBottom(5) }) {
                    col({ borderBottom("#E0E0E0") }) { text("Foo") }
                    col({ background("#E0E0E0");borderBottom("white") }) { text("Foo") }
                    col({ background("#E0E0E0");borderBottom("white") }) { text("Foo") }
                }
                row({ paddingTop(5);paddingBottom(5) }) {
                    col({ borderBottom("#E0E0E0") }) { text("Bar") }
                    col({ background("#E0E0E0");borderBottom("white") }) { text("") }
                    col({ background("#E0E0E0");borderBottom("white") }) { text("Bar") }
                }
            }
            text(({ fontSize(8) }), "Source: Wikipedia")

        }.execute(interceptor)
        interceptor.commands.forEach { println(it) }

        val result = File("result.pdf")
        FileOutputStream(result).write(document)

        assertTrue(result.exists())
        assertTrue(document.size > 32)
    }
}

