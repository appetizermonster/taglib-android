package com.nomad88.taglib.android.demo

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.DiffResult
import com.airbnb.epoxy.OnModelBuildFinishedListener
import com.airbnb.mvrx.viewModel
import com.airbnb.mvrx.withState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.nomad88.taglib.android.MP4File
import com.nomad88.taglib.android.demo.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val epoxyController by lazy { buildEpoxyController() }

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEpoxyRecyclerView()
        setupFilterEditText()
        checkPermissionAndLoadMusicItems()
    }

    private fun buildEpoxyController() = object : AsyncEpoxyController() {
        override fun buildModels() = withState(viewModel) { state ->
            state.filteredMusicItems.forEach {
                musicItemView {
                    id(it.id)
                    title(it.title)
                    filePath(it.filePath)
                    onClick { _ -> onMusicItemClick(it.filePath) }
                }
            }
        }
    }

    private fun setupEpoxyRecyclerView() {
        binding.epoxyRecyclerView.run {
            itemAnimator = null
            setControllerAndBuildModels(epoxyController)
        }

        lifecycleScope.launch {
            viewModel.stateFlow
                .map { it.filteredMusicItems }
                .debounce(50L)
                .collect {
                    epoxyController.requestModelBuild()
                    epoxyController.addModelBuildListener(object : OnModelBuildFinishedListener {
                        override fun onModelBuildFinished(result: DiffResult) {
                            epoxyController.removeModelBuildListener(this)
                            binding.epoxyRecyclerView.scrollToPosition(0)
                        }
                    })
                }
        }
    }

    private fun setupFilterEditText() {
        binding.filterEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setFilter(text.toString())
        }
    }

    private fun checkPermissionAndLoadMusicItems() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    Timber.d("onPermissionGranted")
                    viewModel.loadMusicItems()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Timber.d("onPermissionDenied")
                }

                override fun onPermissionRationaleShouldBeShown(
                    request: PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .check()
    }

    private fun onMusicItemClick(filePath: String) {
        Timber.d("onMusicItemClick: $filePath")
        val mp4File = MP4File.create(filePath)
        if (mp4File == null) {
            Toast.makeText(this, "Not MP4 File", Toast.LENGTH_SHORT).show()
            return
        }

        val tag = mp4File.tag()
        val audioProps = mp4File.audioProperties()
        val title = tag?.title()
        val artist = tag?.artist()
        val album = tag?.album()
        val albumArtist = tag?.albumArtist()
        val genre = tag?.genre()
        val year = tag?.year()
        val track = tag?.track()
        val disc = tag?.disc()
        val lyrics = tag?.lyrics()
        val bitrate = audioProps?.bitrate()
        mp4File.close()

        val details = "Title: $title\n" +
                "Artist: $artist\n" +
                "Album: $album\n" +
                "Album artist: $albumArtist\n" +
                "Genre: $genre\n" +
                "Year: $year\n" +
                "Track: $track\n" +
                "Disc: $disc\n" +
                "Lyrics: $lyrics\n" +
                "Bitrate: $bitrate\n"
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(filePath)
            .setMessage(details)
            .setPositiveButton("Close") { _, _ -> }
            .create()
        dialog.show()
    }
}
