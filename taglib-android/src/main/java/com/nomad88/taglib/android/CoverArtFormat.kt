package com.nomad88.taglib.android

enum class CoverArtFormat(val nativeValue: Int) {
    JPEG(13),
    PNG(14),
    BMP(27),
    GIF(12),
    Unknown(0);

    companion object {
        internal fun fromNativeValue(nativeValue: Int): CoverArtFormat? {
            if (nativeValue < 0) return null
            return values().find { it.nativeValue == nativeValue }
        }
    }
}
