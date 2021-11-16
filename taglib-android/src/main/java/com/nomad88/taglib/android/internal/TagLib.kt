package com.nomad88.taglib.android.internal

import android.util.Log
import com.nomad88.taglib.android.TagLibAndroid

internal object TagLib {

    @JvmStatic
    var isLibraryLoaded = false
        private set

    init {
        try {
            System.loadLibrary("taglibwrapper")
            isLibraryLoaded = true
            if (TagLibAndroid.DEBUG) {
                Log.d("TagLib", "Library loaded")
            }
        } catch (e: Throwable) {
            isLibraryLoaded = false
            if (TagLibAndroid.DEBUG) {
                Log.d("TagLib", "Failed to load library")
            }
        }
    }

    @JvmStatic
    fun ensureLoaded() {
        // NOTE: do nothing
    }
}
