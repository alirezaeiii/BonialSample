package com.android.sample.bonial.util

import android.graphics.Bitmap
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.palette.graphics.Palette
import com.android.sample.bonial.R
import com.android.sample.bonial.common.ViewState
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition

@BindingAdapter("showLoading")
fun <T> View.showLoading(viewState: ViewState<T>?) {
    visibility = if (viewState is ViewState.Loading) View.VISIBLE else View.GONE
}


@BindingAdapter("showError")
fun <T> View.showError(viewState: ViewState<T>?) {
    visibility = if (viewState is ViewState.Error) View.VISIBLE else View.GONE
}

@BindingAdapter("showData")
fun <T> View.showData(viewState: ViewState<T>?) {
    visibility = if (viewState is ViewState.Success) View.VISIBLE else View.GONE
}

@BindingAdapter("imageUrl")
fun View.bindImage(url: String?) {
    Glide.with(context)
        .asBitmap()
        .load(url)
        .apply(RequestOptions().centerCrop())
        .into(object : BitmapImageViewTarget(findViewById(R.id.poster_brochure)) {
            override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                super.onResourceReady(bitmap, transition)
                Palette.from(bitmap).generate { palette ->
                    val color = palette!!.getVibrantColor(
                        ContextCompat.getColor(context, R.color.black_translucent_60)
                    )
                    setBackgroundColor(color)
                }
            }
        })
}