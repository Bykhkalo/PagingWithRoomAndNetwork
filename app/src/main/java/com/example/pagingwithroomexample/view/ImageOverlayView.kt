package com.example.pagingwithroomexample.view


import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import com.example.pagingwithroomexample.R


class ImageOverlayView : RelativeLayout {
    var imageUrl: String? = null

    // String fileUri;
    constructor(context: Context?, imageUrl: String?) : super(context) {
        this.imageUrl = imageUrl
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun sendShareIntent() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, imageUrl)
        sendIntent.type = "text/plain"
        context.startActivity(sendIntent)
    }

    private fun downloadImage(imageUrl: String?) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(imageUrl)
        val request = DownloadManager.Request(uri)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        if (downloadManager != null) {
            val reference = downloadManager.enqueue(request)
        }
    }

    private fun init() {
        val view = inflate(context, R.layout.view_overlay, this)
        view.findViewById<View>(R.id.btn_share).setOnClickListener { v: View? ->
            Toast.makeText(context, "Sharing", Toast.LENGTH_SHORT).show()
            sendShareIntent()
        }
        view.findViewById<View>(R.id.btn_save).setOnClickListener { v: View? ->
            Toast.makeText(context, "Downloading", Toast.LENGTH_SHORT).show()
            downloadImage(imageUrl)
        }
    }
}
