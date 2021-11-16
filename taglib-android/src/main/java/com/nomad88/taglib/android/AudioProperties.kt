package com.nomad88.taglib.android

class AudioProperties internal constructor(internal var ptr: Long) : AutoCloseable {

    fun bitrate(): Int {
        if (ptr == 0L) return 0
        return native_bitrate(ptr)
    }

    fun sampleRate(): Int {
        if (ptr == 0L) return 0
        return native_sampleRate(ptr)
    }

    override fun close() {
        ptr = 0L
    }

    @Suppress("FunctionName")
    companion object {
        @JvmStatic
        private external fun native_bitrate(ptr: Long): Int

        @JvmStatic
        private external fun native_sampleRate(ptr: Long): Int
    }
}
