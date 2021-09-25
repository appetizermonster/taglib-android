package com.nomad88.taglib.android.demo

import android.content.ContentResolver
import android.provider.MediaStore
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

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
    private val contentResolver: ContentResolver
) : MavericksViewModel<MainState>(initialState) {

    companion object : MavericksViewModelFactory<MainViewModel, MainState> {
        @JvmStatic
        override fun create(viewModelContext: ViewModelContext, state: MainState): MainViewModel {
            val contentResolver = viewModelContext.activity.contentResolver
            return MainViewModel(state, contentResolver)
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
}
