package com.nomad88.taglib.android

import android.util.Log

class MP4File private constructor(
    val filePath: String,
    private var ptr: Long
) : AutoCloseable {

    private var tagInstance: MP4Tag? = null
    private var audioPropertiesInstance: AudioProperties? = null

    init {
        if (TagLibAndroid.DEBUG) {
            Log.d(TAG, "file created: $filePath / ptr: $ptr")
        }
    }

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

        val orgPtr = ptr
        TagLib.mp4File_release(ptr)
        ptr = 0L

        if (TagLibAndroid.DEBUG) {
            Log.d(TAG, "file closed: ptr: $orgPtr")
        }
    }

    companion object {
        private const val TAG = "TagLib::MP4File"

        @JvmStatic
        fun isSupported(filePath: String): Boolean {
            return if (TagLib.isLibraryLoaded())
                TagLib.mp4File_isSupported(filePath) else
                false
        }

        @JvmStatic
        fun create(filePath: String, readAudioProperties: Boolean = true): MP4File? {
            if (!TagLib.isLibraryLoaded()) {
                return null
            }
            val ptr = TagLib.mp4File_create(filePath, readAudioProperties)
            return if (ptr == 0L) null else MP4File(filePath, ptr)
        }
    }
}
