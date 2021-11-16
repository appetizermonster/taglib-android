package com.nomad88.taglib.android.internal

object MP4FileNative : FileNativeDelegate {

    init {
        TagLib.ensureLoaded()
    }

    external override fun isSupported(filePath: String): Boolean
    external override fun create(filePath: String, readAudioProperties: Boolean): Long
    external override fun release(ptr: Long)
    external override fun tag(ptr: Long): Long
    external override fun audioProperties(ptr: Long): Long
}
