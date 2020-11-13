package com.example.pagingwithroomexample.view.activity

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pagingwithroomexample.R
import com.example.pagingwithroomexample.repository.MainRepository
import com.example.pagingwithroomexample.utils.Debug
import com.example.pagingwithroomexample.view.ImageOverlayView
import com.example.pagingwithroomexample.view.recycler.ImageAdapter
import com.example.pagingwithroomexample.viewModel.MainViewModel
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig
import com.memebattle.pwc.util.NetworkState
import com.stfalcon.frescoimageviewer.ImageViewer
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), ImageAdapter.ActivityCallbackOnClick {

    private lateinit var viewModel: MainViewModel

    companion object {
        var currentImageUrl: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (currentImageUrl != null) showImageCallback(currentImageUrl)

        init()
    }

    private fun init() {
        viewModel = getViewModel()

        initAdapter()
        initSwipeToRefresh()
    }

    private fun initSwipeToRefresh() {
        viewModel.refreshState.observe(this, {
            swipe_refresh.isRefreshing = it == NetworkState.LOADING
        })
        swipe_refresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun initAdapter() {
        val adapter = ImageAdapter ({
            viewModel.retry()
        }, this)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) users_list.layoutManager =
            LinearLayoutManager(this, GridLayoutManager.HORIZONTAL, false)
        else users_list.layoutManager = LinearLayoutManager(this)

        users_list.adapter = adapter
        viewModel.images.observe(this, {
            adapter.submitList(it)
        })
        viewModel.networkState.observe(this, {
            adapter.setNetworkState(it)
        })

        viewModel.begin()
    }

    private fun getViewModel(): MainViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val repo = MainRepository(this@MainActivity, 10, 3)
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(repo) as T
            }
        })[MainViewModel::class.java]
    }

    override fun showImageCallback(imageUrl: String?) {
        //This method for recyclerView item.onClick call
        Log.d(Debug.TAG, "showImageCallback: called")

        if (imageUrl != null && imageUrl.isNotEmpty()) {
            currentImageUrl = imageUrl

            val config = ImagePipelineConfig.newBuilder(this)
                .setProgressiveJpegConfig(SimpleProgressiveJpegConfig())
                .setResizeAndRotateEnabledForNetwork(true)
                .setDownsampleEnabled(true)
                .build()

            Fresco.initialize(this, config)

            val imageOverlayView = ImageOverlayView(this, imageUrl)

            ImageViewer.Builder(this, Arrays.asList(imageUrl))
                .hideStatusBar(true)
                .allowSwipeToDismiss(true)
                .setOnDismissListener { currentImageUrl = null }
                .setOverlayView(imageOverlayView)
                .show()
        } else {
            Toast.makeText(this, "Error with url", Toast.LENGTH_SHORT).show();
        }
    }

}