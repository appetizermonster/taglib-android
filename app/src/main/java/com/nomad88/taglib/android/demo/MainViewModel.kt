package com.nomad88.taglib.android.demo

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.nomad88.taglib.android.MP4File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import kotlin.random.Random

data class MainState(
    val filter: String = "",
    val musicItems: List<MusicItem> = emptyList()
) : MavericksState {
    val filteredMusicItems by lazy {
        val trimmed = filter.trim().replace(" ", "").lowercase()
        if (trimmed.isEmpty()) {
            musicItems
        } else {
            musicItems.filter {
                it.filePath
                    .replace(" ", "")
                    .lowercase()
                    .contains(trimmed)
            }
        }
    }
}

class MainViewModel(
    initialState: MainState,
    private val cacheDir: File,
    private val contentResolver: ContentResolver
) : MavericksViewModel<MainState>(initialState) {

    companion object : MavericksViewModelFactory<MainViewModel, MainState> {
        @JvmStatic
        override fun create(viewModelContext: ViewModelContext, state: MainState): MainViewModel {
            val ctx = viewModelContext.activity
            val cacheDir = ctx.cacheDir
            val contentResolver = ctx.contentResolver
            return MainViewModel(state, cacheDir, contentResolver)
        }
    }

    fun setFilter(value: String) = setState { copy(filter = value) }

    fun loadMusicItems() = viewModelScope.launch {
        val musicItems = doLoadMusicItems()
        Timber.d("musicItems size: ${musicItems.size}")
        setState { copy(musicItems = musicItems) }
    }

    private suspend fun doLoadMusicItems(): List<MusicItem> = withContext(Dispatchers.IO) {
        val baseUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns._ID,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.DATA
        )
        contentResolver.query(
            baseUri,
            projection,
            MediaStore.Audio.AudioColumns.IS_ALARM + " != 1 AND " +
                    MediaStore.Audio.AudioColumns.IS_NOTIFICATION + " != 1 AND " +
                    MediaStore.Audio.AudioColumns.IS_RINGTONE + " != 1",
            null,
            null
        )?.use { cursor ->
            val musicItems = ArrayList<MusicItem>(cursor.count)
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)
            val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)
            val dataCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val title = cursor.getString(titleCol)
                val data = cursor.getString(dataCol)
                val musicItem = MusicItem(
                    id = id,
                    title = title ?: "",
                    filePath = data ?: ""
                )
                musicItems.add(musicItem)
            }
            musicItems
        } ?: emptyList()
    }

    fun modifyMusicItemFile(musicItem: MusicItem, onComplete: (success: Boolean) -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            if (!MP4File.isSupported(musicItem.filePath)) {
                withContext(Dispatchers.Main) { onComplete(false) }
                return@launch
            }

            val file = File(musicItem.filePath)
            val success = modifyFile(musicItem.contentUri, file) { editingFile ->
                val mp4File = MP4File.create(editingFile.absolutePath) ?: return@modifyFile false
                val tag = mp4File.tag()
                val result = tag?.run {
                    setTitle("new title!!")
                    setArtist("new artist!!")
                    setAlbum("new album!!")
                    setAlbumArtist("new album artist")
                    setGenre("new genre")
                    setYear(2021)
                    setTrack(Random.nextInt(1, 100))
                    setDisc(Random.nextInt(1, 20))
                    setLyrics("new lyrics: ${System.currentTimeMillis()}")
                    save()
                } ?: false
                mp4File.close()
                result
            }
            withContext(Dispatchers.Main) { onComplete(success) }
        }

    private fun modifyFile(
        contentUri: Uri,
        file: File,
        block: (editingFile: File) -> Boolean
    ): Boolean {
        val tempFile = File(cacheDir, file.name)
        if (!replaceFile(file, tempFile)) {
            tempFile.deleteSafely()
            return false
        }
        var success = false
        try {
            val edited = block(tempFile)
            if (edited && tempFile.length() > 0) {
                tempFile.inputStream().use { source ->
                    contentResolver.openOutputStream(contentUri)?.use { target ->
                        StreamUtils.copyStream(source, target)
                        success = true
                    }
                }
            }
        } catch (e: Throwable) {
            Timber.e(e, "Failed to modify file")
            success = false
        }
        tempFile.deleteSafely()
        return success
    }

    private fun replaceFile(source: File, target: File): Boolean {
        try {
            if (target.exists()) {
                target.delete()
            }
            target.outputStream().use { t ->
                source.inputStream().use { s -> StreamUtils.copyStream(s, t) }
            }
        } catch (e: Throwable) {
            Timber.e(e, "Failed to replace file")
            return false
        }
        return true
    }

    private fun File.deleteSafely() {
        try {
            delete()
        } catch (e: Throwable) {
            Timber.e(e, "Failed to delete safely")
        }
    }

}
