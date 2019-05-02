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
