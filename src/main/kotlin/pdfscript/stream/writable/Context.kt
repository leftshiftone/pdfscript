package pdfscript.stream.writable

import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType1Font
import pdfscript.model.PageFormat
import pdfscript.model.PageMargin
import java.util.*

class Context(val format:PageFormat, val margin:PageMargin) {

    val properties = HashMap<String, Any>()

    init {
        properties.put("fontName", PDType1Font.HELVETICA)
        properties.put("fontSize", 12f)
        properties.put("border", true)
    }

    fun fontName():PDFont = properties.get("fontName") as PDFont
    fun fontName(fontName:PDFont) = properties.set("fontName", fontName)

    fun fontSize():Float = properties.get("fontSize") as Float
    fun fontSize(fontSize:Number) = properties.set("fontSize", fontSize.toFloat())

    fun foregroundColor():Optional<String> = Optional.ofNullable(properties.get("foregroundColor")?.toString())
    fun foregroundColor(color:String) = properties.set("foregroundColor", color)

    fun backgroundColor():Optional<String> = Optional.ofNullable(properties.get("backgroundColor")?.toString())
    fun backgroundColor(color:String) = properties.set("backgroundColor", color)

    fun border():Optional<Boolean> = Optional.ofNullable(properties.get("border")).map { it as Boolean }
    fun border(border:Boolean) = properties.set("border", border)

    fun borderBottomColor():Optional<String> = Optional.ofNullable(properties.get("borderBottomColor")?.toString())
    fun borderBottomColor(color:String) = properties.set("borderBottomColor", color)

    fun borderTopColor():Optional<String> = Optional.ofNullable(properties.get("borderTopColor")?.toString())
    fun borderTopColor(color:String) = properties.set("borderTopColor", color)

    fun borderLeftColor():Optional<String> = Optional.ofNullable(properties.get("borderLeftColor")?.toString())
    fun borderLeftColor(color:String) = properties.set("borderLeftColor", color)

    fun borderRightColor():Optional<String> = Optional.ofNullable(properties.get("borderRightColor")?.toString())
    fun borderRightColor(color:String) = properties.set("borderRightColor", color)

    fun paddingTop():Optional<Float> = Optional.ofNullable(properties.get("paddingTop")).map { it as Float }
    fun paddingTop(paddingTop:Number) = properties.set("paddingTop", paddingTop.toFloat())

    fun paddingBottom():Optional<Float> = Optional.ofNullable(properties.get("paddingBottom")).map { it as Float }
    fun paddingBottom(paddingBottom:Number) = properties.set("paddingBottom", paddingBottom.toFloat())

    fun align():Optional<String> = Optional.ofNullable(properties.get("align")).map { it.toString() }
    fun align(align:String) = properties.set("align", align)

    fun ratio():Optional<List<Float>> = Optional.ofNullable(properties.get("ratio")).map { it as List<Float> }
    fun ratio(vararg ratio:Number) = properties.set("ratio", ratio.asList().map { it.toFloat() })

    fun copy():Context {
        val newContext = Context(format, margin)
        newContext.properties.putAll(properties)
        return newContext
    }

    fun lineHeight() = (fontName().boundingBox.height / 1000) * fontSize()
    fun lineWidth(text:String, size:Float = fontSize()) = (fontName().getStringWidth(text) / 1000) * fontSize()

    fun capHeight() = (fontName().fontDescriptor.capHeight / 1000) * fontSize()
    fun boxHeight() = (fontName().boundingBox.height / 1000) * fontSize()

    fun isAlignCenter() = align().isPresent && align().get().equals("center")
    fun isAlignRight() = align().isPresent && align().get().equals("right")

}
