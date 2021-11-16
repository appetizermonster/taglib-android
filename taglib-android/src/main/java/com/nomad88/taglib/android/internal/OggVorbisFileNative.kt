package com.nomad88.taglib.android.internal

internal object OggVorbisFileNative : FileNativeDelegate {

    init {
        TagLib.ensureLoaded()
    }

    external override fun isSupported(filePath: String): Boolean
    external override fun create(filePath: String, readAudioProperties: Boolean): Long
    external override fun release(ptr: Long)
    external override fun save(ptr: Long): Boolean
    external override fun tag(ptr: Long): Long
    external override fun audioProperties(ptr: Long): Long
}
