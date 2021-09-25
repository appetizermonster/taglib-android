package com.nomad88.taglib.android.demo

import android.Manifest
import android.os.Bundle
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
import com.nomad88.taglib.android.FileRef
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
        val fileRef = FileRef(filePath)
        val tag = fileRef.tag()
        val audioProps = fileRef.audioProperties()
        val title = tag?.title()
        val artist = tag?.artist()
        val bitrate = audioProps?.bitrate()
        fileRef.close()

        val details = "Title: $title\nArtist: $artist\nBitrate: $bitrate\n"
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(filePath)
            .setMessage(details)
            .setPositiveButton("Close") { _, _ -> }
            .create()
        dialog.show()
    }
}
