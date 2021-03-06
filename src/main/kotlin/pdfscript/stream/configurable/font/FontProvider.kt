/*
 * Copyright (c) 2016-2019, Leftshift One
 * __________________
 * [2019] Leftshift One
 * All Rights Reserved.
 * NOTICE:  All information contained herein is, and remains
 * the property of Leftshift One and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Leftshift One
 * and its suppliers and may be covered by Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Leftshift One.
 */

package pdfscript.stream.configurable.font

import org.apache.pdfbox.pdmodel.font.PDFont
import org.slf4j.LoggerFactory
import pdfscript.stream.configurable.map.FontsMap

/**
 * Handles fonts and holds replacements for characters not available in a specific font so that PDF generation is
 * possible even though some glyphs are not available in a font.
 *
 * ATTENTION: Since PDFont objects depend on a document instance,
 * the FontProvider is not save to be used for different document instances!
 *
 * @author Michael Mair
 */
class FontProvider {

    private val log = LoggerFactory.getLogger(this::class.java)
    private val fonts: MutableMap<String, CharArray> = mutableMapOf()
    private val rawFonts: MutableMap<String, PDFont> = mutableMapOf()

    @JvmOverloads
    fun addFont(font: PDFont, charReplacement: Char? = null) {
        if (fonts.containsKey(font.name)) return
        fonts[font.name] = prepareReplacements(font, charReplacement)
        rawFonts[font.name] = font
        if (!FontsMap.hasFont(font.name))
            FontsMap.register(font.name, font)
    }

    fun getFont(name: String): PDFont {
        if (fonts.containsKey(name))
            return rawFonts.get(name)!!
        if (FontsMap.hasFont(name))
            return FontsMap.resolve(name)
        throw java.lang.RuntimeException("no font with name $name found")
    }

    fun hasFont(name: String): Boolean {
        return fonts.containsKey(name) || FontsMap.hasFont(name)
    }

    fun sanitize(font: PDFont, string: String): String {
        try {
            font.encode(string)
            return string
        } catch (ex: Exception) {
            log.debug("'$string' cannot be encoded entirely")
        }

        if (!fonts.containsKey(font.name)) {
            addFont(font)
        }
        val availableChars = fonts[font.name]
        if (availableChars == null || availableChars.isEmpty()) {
            return string
        }

        val builder = StringBuilder(string.length)
        string.codePoints().forEach {
            if (it < availableChars.size) builder.append(availableChars[it])
        }
        return builder.toString()
    }

    private fun prepareReplacements(font: PDFont, charReplacement: Char?): CharArray {
        log.info("Preparing font ${font.name}")
        var replacement = charReplacement
        if (replacement == null) {
            replacement = firstSupportedChar(font).toChar()
        }
        return (0..(1 shl 16))
                .map { if (font.isSupported(it)) it.toChar() else replacement }
                .toCharArray()
    }

    private fun firstSupportedChar(font: PDFont): Int {
        return (0..(1 shl 16)).find { font.isSupported(it) }
                ?: throw RuntimeException("Font ${font.name} has no glyphs")
    }

    /**
     * This method has been inspired by https://svn.apache.org/viewvc/pdfbox/trunk/examples/src/main/java/org/apache/pdfbox/examples/pdmodel/EmbeddedMultipleFonts.java?view=markup.
     */
    private fun PDFont.isSupported(codePoint: Int): Boolean {
        return try {
            this.encode(String(Character.toChars(codePoint)))
            true
        } catch (e: Exception) {
            false
        }
    }
}
