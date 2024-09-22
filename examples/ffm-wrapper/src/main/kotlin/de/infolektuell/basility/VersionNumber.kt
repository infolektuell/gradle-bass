package de.infolektuell.basility

data class VersionNumber(val major: Int, val minor: Int, val patch: Int, val build: Int) {
    override fun toString() = "$major.$minor.$patch.$build"
}
