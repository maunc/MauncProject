package com.us.mytest.ui.activity.pic

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.utils.ActivityCompatHelper
import com.us.mytest.R


/**
 *ClsFunction：
 *CreateDate：2024/5/13
 *Author：TimeWillRememberUs
 */
val glideEngine: GlideEngin by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    GlideEngin.INSTANCE
}
class GlideEngin : ImageEngine {
    companion object {
        val INSTANCE: GlideEngin by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            GlideEngin()
        }
    }
    override fun loadImage(context: Context?, url: String?, imageView: ImageView?) {
        if (!ActivityCompatHelper.assertValidRequest(context)) return
        Glide.with(context!!)
            .load(url)
            .into(imageView!!)
    }

    override fun loadImage(
        context: Context?,
        imageView: ImageView?,
        url: String?,
        maxWidth: Int,
        maxHeight: Int
    ) {
        if (!ActivityCompatHelper.assertValidRequest(context)) return
        Glide.with(context!!)
            .load(url)
            .into(imageView!!)
    }

    override fun loadAlbumCover(context: Context?, url: String?, imageView: ImageView?) {
        if (!ActivityCompatHelper.assertValidRequest(context)) return
        Glide.with(context!!)
            .asBitmap()
            .load(url)
            .override(180, 180)
            .sizeMultiplier(0.5f)
            .transform(CenterCrop(), RoundedCorners(8))
            .placeholder(R.drawable.ic_launcher_background)
            .into(imageView!!)
    }

    override fun loadGridImage(context: Context?, url: String?, imageView: ImageView?) {
        if (!ActivityCompatHelper.assertValidRequest(context)) return
        Glide.with(context!!)
            .load(url)
            .override(200, 200)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .into(imageView!!)
    }

    override fun pauseRequests(context: Context?) {
        Glide.with(context!!).pauseRequests();
    }

    override fun resumeRequests(context: Context?) {
        Glide.with(context!!).resumeRequests()
    }
}