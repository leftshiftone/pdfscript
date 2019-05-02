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
