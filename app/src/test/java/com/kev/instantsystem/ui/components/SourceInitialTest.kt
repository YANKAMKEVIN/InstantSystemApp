package com.kev.instantsystem.ui.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SourceInitialTest {

    @Test
    fun `initial is the first letter or digit of the source`() {
        assertEquals("L", sourceInitial("le Monde"))
        assertEquals("9", sourceInitial(" 9to5Mac"))
        assertEquals("B", sourceInitial("(BBC News)"))
        assertEquals("?", sourceInitial(""))
    }

    @Test
    fun `a source always gets the same colour, within the palette`() {
        val names = listOf("Le Monde", "BBC News", "The Verge", "", "Ars Technica", "Wired")
        names.forEach { name ->
            val index = sourceColorIndex(name, 6)
            assertTrue(index in 0 until 6)
            assertEquals(index, sourceColorIndex(name, 6))
        }
        assertEquals(sourceColorIndex("Le Monde", 6), sourceColorIndex(" le monde ", 6))
    }
}
