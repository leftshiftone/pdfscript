package pdfscript.configurable.map

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pdfscript.stream.configurable.map.FontsMap

class FontsMapTest {

    @Test
    fun testHelvetica() {
        Assertions.assertNotNull(FontsMap.resolve("helvetica"))
    }

}
