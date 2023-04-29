package com.example.marvelapp.framework.imageloader

import android.widget.ImageView
import com.bumptech.glide.Glide
import javax.inject.Inject

class ImageLoaderImpl @Inject constructor(
): ImageLoader {

    override fun loadImage(imageView: ImageView, imageUrl: String, fallback: Int) {
        Glide.with(imageView.rootView)
            .load(imageUrl)
            .fallback(fallback)
            .into(imageView)
    }

}