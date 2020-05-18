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
import pdfscript.model.PageFormat
import pdfscript.model.PageMargin
import pdfscript.stream.configurable.map.FontsMap
import java.util.*
import java.util.function.Supplier

class Context(val format: PageFormat, val margin: PageMargin) {

    val properties = HashMap<String, Any>()

    init {
        properties.put("font", PDType1Font.HELVETICA)
        properties.put("fontSize", 12f)
        properties.put("border", "black")
    }

    fun font(): PDFont = resolve(properties.get("font")) as PDFont
    fun font(font: PDFont) = properties.set("font", font)
    fun font(font: String) = properties.set("font", FontsMap.resolve(font))
    fun font(font: Supplier<PDFont>) = properties.set("font", font)

    fun fontSize(): Float = properties.get("fontSize") as Float
    fun fontSize(fontSize: Number) = properties.set("fontSize", fontSize.toFloat())

    fun foreground(): Optional<String> = Optional.ofNullable(properties.get("fill")?.toString())
    fun foreground(color: String) = properties.set("fill", color)

    fun background(): Optional<String> = Optional.ofNullable(properties.get("background")?.toString())
    fun background(color: String) = properties.set("background", color)

    fun border(): Optional<String> = Optional.ofNullable(properties.get("border")).map { it as String }
    fun border(border: String) = properties.set("border", border)

    fun borderBottom(): Optional<String> = Optional.ofNullable(properties.get("borderBottom")?.toString())
    fun borderBottom(color: String) = properties.set("borderBottom", color)

    fun borderTop(): Optional<String> = Optional.ofNullable(properties.get("borderTop")?.toString())
    fun borderTop(color: String) = properties.set("borderTop", color)

    fun borderLeft(): Optional<String> = Optional.ofNullable(properties.get("borderLeft")?.toString())
    fun borderLeft(color: String) = properties.set("borderLeft", color)

    fun borderRight(): Optional<String> = Optional.ofNullable(properties.get("borderRight")?.toString())
    fun borderRight(color: String) = properties.set("borderRight", color)

    fun paddingTop(): Optional<Float> = Optional.ofNullable(properties.get("paddingTop")).map { it as Float }
    fun paddingTop(paddingTop: Number) = properties.set("paddingTop", paddingTop.toFloat())

    fun paddingBottom(): Optional<Float> = Optional.ofNullable(properties.get("paddingBottom")).map { it as Float }
    fun paddingBottom(paddingBottom: Number) = properties.set("paddingBottom", paddingBottom.toFloat())

    fun align(): Optional<String> = Optional.ofNullable(properties.get("align")).map { it.toString() }
    fun align(align: String) = properties.set("align", align)

    fun ratio(): Optional<List<Float>> = Optional.ofNullable(properties.get("ratio")).map {
        @Suppress("UNCHECKED_CAST")
        it as List<Float>
    }

    fun ratio(vararg ratio: Number) = properties.set("ratio", ratio.asList().map { it.toFloat() })

    fun copy(): Context {
        val newContext = Context(format, margin)
        newContext.properties.putAll(properties)
        return newContext
    }

    fun lineHeight() = boxHeight()
    fun lineWidth(text: String, size: Float = fontSize()) = (font().getStringWidth(text) / 1000) * size

    fun capHeight() = (font().fontDescriptor.capHeight / 1000) * fontSize()
    fun boxHeight() = (font().boundingBox.height / 1000) * fontSize()

    fun isAlignCenter() = align().isPresent && align().get().equals("center")
    fun isAlignRight() = align().isPresent && align().get().equals("right")

    private fun resolve(obj:Any?):Any? {
        return when (obj) {
            is Supplier<*> -> obj.get()
            else -> obj
        }
    }

}
