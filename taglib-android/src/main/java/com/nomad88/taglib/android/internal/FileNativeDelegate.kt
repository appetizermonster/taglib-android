package com.nomad88.taglib.android.internal

internal interface FileNativeDelegate {
    fun isSupported(filePath: String): Boolean
    fun create(filePath: String, readAudioProperties: Boolean): Long
    fun release(ptr: Long)
    fun save(ptr: Long): Boolean
    fun tag(ptr: Long): Long
    fun audioProperties(ptr: Long): Long
}
