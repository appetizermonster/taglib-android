package com.nomad88.taglib.android

import com.nomad88.taglib.android.internal.*

class MP4File private constructor(
    filePath: String,
    ptr: Long
) : TagFile("TagLib::MP4File", filePath, ptr) {

    override val nativeDelegate: FileNativeDelegate = MP4FileNative
    override val tagNativeDelegate: TagNativeDelegate = MP4TagNative

    companion object {
        @JvmStatic
        fun isSupported(filePath: String): Boolean {
            return if (TagLib.isLibraryLoaded)
                MP4FileNative.isSupported(filePath) else
                false
        }

        @JvmStatic
        fun create(filePath: String, readAudioProperties: Boolean = true): MP4File? {
            if (!TagLib.isLibraryLoaded) {
                return null
            }
            val ptr = MP4FileNative.create(filePath, readAudioProperties)
            return if (ptr == 0L) null else MP4File(filePath, ptr)
        }
    }
}
