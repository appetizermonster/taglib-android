package com.nomad88.taglib.android.demo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
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

    private var musicItemToWrite: MusicItem? = null

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
                    onModifyClick { _ -> onModifyMusicItemClick(it) }
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
        val mp4File = MP4File.create(filePath, readAudioProperties = true)
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
        val sampleRate = audioProps?.sampleRate()
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
                "Bitrate: $bitrate\n" +
                "SampleRate: $sampleRate\n"
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(filePath)
            .setMessage(details)
            .setPositiveButton("Close") { _, _ -> }
            .create()
        dialog.show()
    }

    private fun onModifyMusicItemClick(musicItem: MusicItem) {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(musicItem.filePath)
            .setMessage("Do you want to modify this file?")
            .setPositiveButton("Yes") { _, _ -> modifyMusicItemFile(musicItem) }
            .setNegativeButton("No") { _, _ -> }
            .create()
        dialog.show()
    }

    private fun modifyMusicItemFile(musicItem: MusicItem) {
        musicItemToWrite = musicItem

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val hasPermission = checkUriPermission(
                musicItem.contentUri,
                android.os.Process.myPid(),
                android.os.Process.myUid(),
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED
            if (hasPermission) {
                onWritePermissionGranted()
            } else {
                val writeRequestIntent =
                    MediaStore.createWriteRequest(contentResolver, listOf(musicItem.contentUri))
                val request = IntentSenderRequest.Builder(writeRequestIntent).build()
                writePermissionContract.launch(request)
            }
        } else {
            Dexter.withContext(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        onWritePermissionGranted()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        onWritePermissionDenied()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        request: PermissionRequest,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
        }
    }

    private fun onWritePermissionGranted() {
        val musicItem = musicItemToWrite
        musicItemToWrite = null

        if (musicItem == null) {
            Toast.makeText(this, "No music item to write", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.modifyMusicItemFile(musicItem) { success ->
            Toast.makeText(this, "Modified: success: $success", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onWritePermissionDenied() {
        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        musicItemToWrite = null
    }

    private val writePermissionContract = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onWritePermissionGranted()
        } else {
            onWritePermissionDenied()
        }
    }
}
