package com.nomad88.taglib.android

class MP4File private constructor(
    val filePath: String,
    private var ptr: Long
) : AutoCloseable {

    private var tagInstance: MP4Tag? = null
    private var audioPropertiesInstance: AudioProperties? = null

    fun tag(): MP4Tag? {
        if (ptr == 0L) return null
        return tagInstance ?: run {
            val tagPtr = TagLib.mp4File_tag(ptr)
            val newInstance = if (tagPtr == 0L) null else MP4Tag(tagPtr)
            tagInstance = newInstance
            newInstance
        }
    }

    fun audioProperties(): AudioProperties? {
        if (ptr == 0L) return null
        return audioPropertiesInstance ?: run {
            val audioPropertiesPtr = TagLib.mp4File_audioProperties(ptr)
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

        TagLib.mp4File_release(ptr)
        ptr = 0L
    }

    companion object {
        @JvmStatic
        fun isSupported(filePath: String): Boolean {
            return TagLib.mp4File_isSupported(filePath)
        }

        @JvmStatic
        fun create(filePath: String): MP4File? {
            val ptr = TagLib.mp4File_create(filePath)
            return if (ptr == 0L) null else MP4File(filePath, ptr)
        }
    }
}
