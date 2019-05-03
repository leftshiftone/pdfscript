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

package pdfscript.stream.configurable.map

import org.apache.pdfbox.pdmodel.font.PDFont
import java.util.*
import kotlin.collections.HashMap

class FontsMap {

    companion object {
        val fontsMap = HashMap<String, PDFont>()

        init {
            val props = Properties().apply { load(FontsMap::class.java.getResourceAsStream("/fonts.properties")) }
            props.forEach { k, v ->
                val className = v.toString().substringBefore(":")
                val fieldName = v.toString().substringAfter(":")

                val cls = Class.forName(className)
                val fld = cls.getDeclaredField(fieldName)

                fontsMap.put(k.toString(), fld.get(cls) as PDFont)
            }
        }

        fun register(key:String, value: PDFont) {
            if (!fontsMap.containsKey(key)) {
                fontsMap.put(key, value)
            }
            else {
                throw IllegalArgumentException("font key $key is already registered")
            }
        }

        fun resolve(key:String):PDFont {
            if (fontsMap.containsKey(key))
                return fontsMap.get(key)!!
            throw IllegalArgumentException("font key $key is not registered")
        }
    }

}
