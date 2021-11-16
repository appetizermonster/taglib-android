package com.nomad88.taglib.android

import com.nomad88.taglib.android.internal.*

class OggVorbisFile private constructor(
    filePath: String,
    ptr: Long
) : TagFile("TagLib::OggVorbisFile", filePath, ptr) {

    override val nativeDelegate: FileNativeDelegate = OggVorbisFileNative
    override val tagNativeDelegate: TagNativeDelegate = OggVorbisTagNative

    companion object {
        @JvmStatic
        fun isSupported(filePath: String): Boolean {
            return if (TagLib.isLibraryLoaded)
                OggVorbisFileNative.isSupported(filePath) else
                false
        }

        @JvmStatic
        fun create(filePath: String, readAudioProperties: Boolean = true): OggVorbisFile? {
            if (!TagLib.isLibraryLoaded) {
                return null
            }
            val ptr = OggVorbisFileNative.create(filePath, readAudioProperties)
            return if (ptr == 0L) null else OggVorbisFile(filePath, ptr)
        }
    }
}
