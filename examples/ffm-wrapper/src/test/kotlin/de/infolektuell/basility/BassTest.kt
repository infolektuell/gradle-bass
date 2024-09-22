package de.infolektuell.basility

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class BassTest {
    @Test
    fun extractsBassVersion() {
        val bass = Bass()
        assertEquals(bass.version.toString(), "2.4.17.0")
    }
}
