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

package pdfscript.stream.configurable

import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType1Font
import pdfscript.stream.configurable.map.FontsMap
import java.util.*
import java.util.function.Supplier

class Brush {

    private val properties = HashMap<String, Any>()

    init {
        properties["font"] = PDType1Font.HELVETICA
        properties["fontSize"] = 12f
    }

    fun font(): PDFont = resolve(properties.get("font")) as PDFont
    fun font(font: PDFont) = properties.set("font", font)
    fun font(font: String) = properties.set("font", FontsMap.resolve(font))
    fun font(font: Supplier<PDFont>) = properties.set("font", font)

    fun fontSize(): Float = properties.get("fontSize") as Float
    fun fontSize(fontSize: Number) = properties.set("fontSize", fontSize.toFloat())

    fun fill(): Optional<String> = Optional.ofNullable(properties.get("fill")?.toString())
    fun fill(color: String) = properties.set("fill", color)

    fun copy(): Brush {
        val newBrush = Brush()
        newBrush.properties.putAll(properties)
        return newBrush
    }

    private fun resolve(obj: Any?): Any? {
        return when (obj) {
            is Supplier<*> -> obj.get()
            else -> obj
        }
    }

}
