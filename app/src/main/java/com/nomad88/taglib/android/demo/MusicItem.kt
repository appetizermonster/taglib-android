package com.nomad88.taglib.android.demo

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore

data class MusicItem(
    val id: Long,
    val title: String,
    val filePath: String
) {
    val contentUri: Uri
        get() = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
}
