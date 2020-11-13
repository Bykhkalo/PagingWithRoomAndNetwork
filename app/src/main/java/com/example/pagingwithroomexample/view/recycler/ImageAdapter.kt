package com.example.pagingwithroomexample.view.recycler

import android.util.Log
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingwithroomexample.R
import com.example.pagingwithroomexample.utils.Debug.Companion.TAG
import com.example.viewimages.model.ImageItem
import com.memebattle.pwc.util.NetworkState

class ImageAdapter(
    private val retryCallback: () -> Unit, private val callbackOnClick: ActivityCallbackOnClick
) : PagedListAdapter<ImageItem, RecyclerView.ViewHolder>(POST_COMPARATOR) {

    private var networkState: NetworkState? = null


    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<ImageItem>() {
            override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
                return oldItem.equals(newItem) && oldItem.id == newItem.id

            }


            override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
                return oldItem.id == newItem.id
            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.image_item -> ImageItemViewHolder.create(parent)
            R.layout.network_state_item -> NetworkStateItemViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            val item = getItem(position)
            Log.d(TAG, "onBindViewHolder: payload: ${item}")
        } else {
            onBindViewHolder(holder, position)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: pos: $position")
        when (getItemViewType(position)) {
            R.layout.image_item -> {

                val itemData = getItem(position)
                if (itemData != null) {
                    holder.itemView.setOnClickListener {
                        callbackOnClick.showImageCallback(itemData.largeImageURL)
                    }
                }



                (holder as ImageItemViewHolder).bind(getItem(position))
            }
            R.layout.network_state_item -> (holder as NetworkStateItemViewHolder).bindTo(
                networkState
            )
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_item
        } else {
            R.layout.image_item
        }
    }


    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED


    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
                Log.d(TAG, "setNetworkState: loading holder removed")
            } else {
                notifyItemInserted(super.getItemCount())
                Log.d(TAG, "setNetworkState: loading holder inserted")
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    interface ActivityCallbackOnClick {
        fun showImageCallback(imageUrl: String?)
    }

}