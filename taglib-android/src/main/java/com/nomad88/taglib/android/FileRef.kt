package com.nomad88.taglib.android

class FileRef(val filePath: String) : AutoCloseable {

    private var ptr: Long = try {
        TagLib.fileRef_create(filePath)
    } catch (e: Throwable) {
        0L
    }

    private var tagInstance: Tag? = null
    private var audioPropertiesInstance: AudioProperties? = null

    fun isNull(): Boolean {
        if (ptr == 0L) return false
        return TagLib.fileRef_isNull(ptr)
    }

    fun tag(): Tag? {
        if (ptr == 0L) return null
        return tagInstance ?: run {
            val tagPtr = TagLib.fileRef_tag(ptr)
            val newInstance = if (tagPtr == 0L) null else Tag(tagPtr)
            tagInstance = newInstance
            newInstance
        }
    }

    fun audioProperties(): AudioProperties? {
        if (ptr == 0L) return null
        return audioPropertiesInstance ?: run {
            val audioPropertiesPtr = TagLib.fileRef_audioProperties(ptr)
            val newInstance =
                if (audioPropertiesPtr == 0L) null else AudioProperties(audioPropertiesPtr)
            audioPropertiesInstance = newInstance
            newInstance
        }
    }

    override fun close() {
        if (ptr == 0L) return

        tagInstance?.close()
        tagInstance = null

        audioPropertiesInstance?.close()
        audioPropertiesInstance = null

        TagLib.fileRef_release(ptr)
        ptr = 0L
    }
}
