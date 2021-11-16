package com.nomad88.taglib.android.internal

interface FileNativeDelegate {
    fun isSupported(filePath: String): Boolean
    fun create(filePath: String, readAudioProperties: Boolean): Long
    fun release(ptr: Long)
    fun tag(ptr: Long): Long
    fun audioProperties(ptr: Long): Long
}
