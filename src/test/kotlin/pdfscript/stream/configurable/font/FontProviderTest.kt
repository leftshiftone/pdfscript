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

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * @author Michael Mair
 */
internal class FontProviderTest {

    @Test
    fun `string is sanitized for given font`() {
        val font = PDType1Font.HELVETICA
        val result = FontProvider().sanitize(font, "Fórrest اڵڶڷ Gûmp")
        assertEquals("Fórrest AAAA Gûmp", result)
    }

    @Test
    fun `string is sanitized for given font with custom replacement`() {
        val font = PDType1Font.HELVETICA
        val fontProvider = FontProvider()
        fontProvider.addFont(font, '?')
        val result = fontProvider.sanitize(font, "Fórrest اڵڶڷ Gûmp")
        assertEquals("Fórrest ???? Gûmp", result)
    }

    @Test
    fun `string is sanitized for loaded font`() {
        val font = PDType0Font.load(PDDocument(), this::class.java.getResourceAsStream("/font/NotoSansArabic-Regular.ttf"))
        val result = FontProvider().sanitize(font, "Fórrest اڵڶڷ Gûmp")
        assertEquals("\u0001\u0001\u0001\u0001\u0001\u0001\u0001 اڵڶڷ \u0001\u0001\u0001\u0001", result)
    }
}