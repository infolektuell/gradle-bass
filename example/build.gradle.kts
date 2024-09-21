plugins {
    id("base")
    id("de.infolektuell.bass") version "0.1.0"
}

bass.libraries {
    register("bassflac")
    register("bassmix")
}
