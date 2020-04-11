package com.kc.movies

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.Target

/**
 * Load an image from the given url into the image view
 *
 * @param imageUrl the url to the image
 * @param circleCrop true, if the image should be circle cropped
 */
fun ImageView.loadImage(imageUrl: String, circleCrop: Boolean = false) {
    val builder = Glide.with(context)
            .load(imageUrl)

    if (circleCrop) {
        //builder.circleCrop()
    }

    builder.transition(DrawableTransitionOptions.withCrossFade())
            //.override(Target.SIZE_ORIGINAL)
            .into(this)
}