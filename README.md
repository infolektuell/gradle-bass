# Gradle Bass Plugin

[Bass] is a native audio library that supports several platforms and comes with a variety of optional plugins.
Its binaries and headers are distributed as zip files, so users have to download and extract them by hand.
This Gradle plugin automates this process and makes the relevant files available for the build.

## Features

- [x] Downloads and verifies the Bass libs for the current build platform and architecture
- [x] Compatible with [Configuration Cache]

## Usage

```
plugins {
    id("de.infolektuell.bass") version "0.1.0"
}

bass.plugins = listOf("flac", "mix")
```

## License

[MIT License](LICENSE.txt)

[bass]: https://www.un4seen.com/
[configuration cache]: https://docs.gradle.org/current/userguide/configuration_cache.html
