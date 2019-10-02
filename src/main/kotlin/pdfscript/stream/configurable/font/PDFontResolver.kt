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

package pdfscript.stream.configurable.font

import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDVectorFont
import java.lang.Character.UnicodeScript.COMMON
import java.lang.Character.UnicodeScript.LATIN
import java.util.*

class PDFontResolver(private val fonts:List<PDFont>) {

    constructor(vararg fonts:PDFont) : this(fonts.toList())

    fun resolve(text:String):PDFont {
        val optional = Optional.ofNullable(fonts.find { it.isSupported(text) })
        return optional.orElseThrow {RuntimeException("unsupported text '$text' detected")}
    }

    private fun PDFont.isSupported(text:String):Boolean {
        return text.codePoints().allMatch {
            if (this is PDVectorFont)
                return@allMatch this.hasGlyph(it)
            return@allMatch Character.UnicodeScript.of(it) in listOf(COMMON, LATIN)
        }
    }

}
