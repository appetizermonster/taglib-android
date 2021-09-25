package com.nomad88.taglib.android

class Tag internal constructor(internal var ptr: Long) : AutoCloseable {

    fun title(): String {
        if (ptr == 0L) return ""
        return TagLib.tag_title(ptr)
    }

    override fun close() {
        ptr = 0L
    }
}
