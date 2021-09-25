package com.nomad88.taglib.android

class FileRef(val filePath: String) : AutoCloseable {

    private var ptr: Long = try {
        TagLib.fileRef_create(filePath)
    } catch (e: Throwable) {
        0L
    }

    private var tagInstance: Tag? = null

    fun isNull(): Boolean {
        if (ptr == 0L) return false
        return TagLib.fileRef_isNull(ptr)
    }

    fun tag(): Tag? {
        if (ptr == 0L) return null
        return tagInstance ?: run {
            val tagPtr = TagLib.fileRef_tag(ptr)
            val newTagInstance = if (tagPtr == 0L) null else Tag(tagPtr)
            tagInstance = newTagInstance
            newTagInstance
        }
    }

    override fun close() {
        if (ptr == 0L) return
        tagInstance?.close()
        tagInstance = null
        TagLib.fileRef_release(ptr)
        ptr = 0L
    }
}
