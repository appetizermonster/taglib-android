package com.nomad88.taglib.android

import android.util.Log
import com.nomad88.taglib.android.internal.FileNativeDelegate
import com.nomad88.taglib.android.internal.TagNativeDelegate

abstract class TagFile internal constructor(
    private val debugTag: String,
    val filePath: String,
    private var ptr: Long
) : AutoCloseable {

    internal abstract val nativeDelegate: FileNativeDelegate
    internal abstract val tagNativeDelegate: TagNativeDelegate

    private var tagInstance: Tag? = null
    private var audioPropertiesInstance: AudioProperties? = null

    init {
        if (TagLibAndroid.DEBUG) {
            Log.d(debugTag, "file created: $filePath / ptr: $ptr")
        }
    }

    fun tag(): Tag? {
        if (ptr == 0L) return null
        return tagInstance ?: run {
            val tagPtr = nativeDelegate.tag(ptr)
            val newInstance = if (tagPtr == 0L) null else Tag(tagPtr, tagNativeDelegate)
            tagInstance = newInstance
            newInstance
        }
    }

    fun audioProperties(): AudioProperties? {
        if (ptr == 0L) return null
        return audioPropertiesInstance ?: run {
            val audioPropertiesPtr = nativeDelegate.audioProperties(ptr)
            val newInstance =
                if (audioPropertiesPtr == 0L) null else AudioProperties(audioPropertiesPtr)
            audioPropertiesInstance = newInstance
            newInstance
        }
    }

    fun save(): Boolean {
        if (ptr == 0L) return false
        return nativeDelegate.save(ptr)
    }

    override fun close() {
        if (ptr == 0L) return

        tagInstance?.close()
        tagInstance = null

        val orgPtr = ptr
        nativeDelegate.release(ptr)
        ptr = 0L

        if (TagLibAndroid.DEBUG) {
            Log.d(debugTag, "file closed: ptr: $orgPtr")
        }
    }

}
