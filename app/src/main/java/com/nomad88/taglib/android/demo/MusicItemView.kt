package com.nomad88.taglib.android.demo

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.nomad88.taglib.android.demo.databinding.EpoxyMusicItemViewBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class MusicItemView(context: Context) : FrameLayout(context) {

    var onClick: OnClickListener? = null
        @CallbackProp set
    var onModifyClick: OnClickListener? = null
        @CallbackProp set

    private val binding =
        EpoxyMusicItemViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.root.setOnClickListener { onClick?.onClick(this) }
        binding.modifyButton.setOnClickListener { onModifyClick?.onClick(it) }
    }

    @TextProp
    fun setTitle(value: CharSequence) {
        binding.titleView.text = value
    }

    @TextProp
    fun setFilePath(value: CharSequence) {
        binding.filePathView.text = value
    }
}
