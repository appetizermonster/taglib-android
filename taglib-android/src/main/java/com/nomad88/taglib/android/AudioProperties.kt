package com.nomad88.taglib.android

class AudioProperties internal constructor(internal var ptr: Long) : AutoCloseable {

    fun bitrate(): Int {
        if (ptr == 0L) return 0
        return TagLib.audioProperties_bitrate(ptr)
    }

    fun sampleRate(): Int {
        if (ptr == 0L) return 0
        return TagLib.audioProperties_sampleRate(ptr)
    }

    override fun close() {
        ptr = 0L
    }
}
