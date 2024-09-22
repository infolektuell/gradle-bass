package de.infolektuell.basility

import com.un4seen.bass.Bass.BASS_GetVersion
import java.nio.ByteBuffer

class Bass {
    val version: VersionNumber
        get() {
        val version: Int = BASS_GetVersion()
        val buf = ByteBuffer.allocate(4)
        buf.putInt(version)
        val result = buf.array().map { it.toInt() }
        val (major, minor, patch, build) = result
        return VersionNumber(major, minor, patch, build)
    }
}
