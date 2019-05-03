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

import java.awt.Color
import java.util.*
import kotlin.collections.HashMap

class ColorMap {

    companion object {
        val colorMap = HashMap<String, Color>()

        init {
            val props = Properties().apply { load(ColorMap::class.java.getResourceAsStream("/color.properties")) }
            props.forEach { k, v -> colorMap.put(k.toString(), Color.decode(v.toString()))}
        }

        fun register(key:String, value: Color) {
            if (!colorMap.containsKey(key)) {
                colorMap.put(key, value)
            }
            else {
                throw IllegalArgumentException("color key $key is already registered")
            }
        }

        fun resolve(key:String):Color {
            if (colorMap.containsKey(key))
                return colorMap.get(key)!!
            throw IllegalArgumentException("color key $key is not registered")
        }
    }

}
