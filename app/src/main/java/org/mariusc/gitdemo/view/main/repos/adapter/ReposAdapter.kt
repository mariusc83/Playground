package org.mariusc.gitdemo.view.main.repos.adapter

import android.databinding.DataBindingUtil
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup


import org.mariusc.gitdemo.R
import org.mariusc.gitdemo.data.network.model.RepoModel
import org.mariusc.gitdemo.data.network.model.Page
import org.mariusc.gitdemo.databinding.LoadMoreItemsLayoutBinding
import org.mariusc.gitdemo.databinding.ReposRowLayoutBinding

import java.util.ArrayList


class ReposAdapter(private val layoutInflater: LayoutInflater,
                   private val dataBindingComponent: android.databinding.DataBindingComponent) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data: MutableList<RepoModel>? = null
    private var currentPage: Page? = null
    private var isLoadingMoreItems = false
    private var infiniteAdapterCallback: InfiniteAdapterCallback? = null
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == R.id.view_type_loading_more) {
            val loadMoreItemsLayoutBinding = DataBindingUtil.inflate<LoadMoreItemsLayoutBinding>(layoutInflater,
                    R.layout.load_more_items_layout,
                    parent,
                    false)
            return object : RecyclerView.ViewHolder(loadMoreItemsLayoutBinding.root) {}
        }
        val binding = DataBindingUtil
                .inflate<ReposRowLayoutBinding>(layoutInflater,
                        R.layout.repos_row_layout,
                        parent,
                        false,
                        dataBindingComponent)

        val trackViewHolder = ReposViewHolder(binding)
        trackViewHolder.setListener(mOnItemClickedListener)
        return trackViewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType != R.id.view_type_loading_more) {
            if (holder is ReposViewHolder) {
                holder.bind(data!![position])
                if (position > data!!.size - LOAD_MORE_THRESHOLD
                        && canLoadMore()
                        && !isLoadingMoreItems) {
                    setIsLoadingMoreItems(true)
                    infiniteAdapterCallback!!.onRequestMoreItems()
                }
            }
        }
    }

    private fun setIsLoadingMoreItems(isLoadingMoreItems: Boolean) {
        if (isLoadingMoreItems == this.isLoadingMoreItems) return
        this.isLoadingMoreItems = isLoadingMoreItems

        if (isLoadingMoreItems) {
            mainThreadHandler.post { notifyItemInserted(itemCount - 1) }
        } else {
            mainThreadHandler.post { notifyItemRemoved(itemCount) }
        }

    }


    override fun getItemCount(): Int {
        return dataSize() + if (isLoadingMoreItems) 1 else 0
    }

    private fun dataSize(): Int {
        return if (data != null) data!!.size else 0
    }


    override fun getItemViewType(position: Int): Int {
        if (position >= data!!.size) return R.id.view_type_loading_more
        return super.getItemViewType(position)
    }

    fun setData(page: Page) {
        currentPage = page
        currentPage?.let {
            if (it.isFirst) {
                data = ArrayList(it.data.size)
            }

            this.data!!.addAll(it.data)

            if (isLoadingMoreItems)
                notifyInsertion(it.data.size)
            else
                notifyDataSetChanged()

        }

    }

    private fun notifyInsertion(numberOfItemsAdded: Int) {
        this.isLoadingMoreItems = false
        mainThreadHandler.post {
            val oldListEnd = data!!.size - numberOfItemsAdded
            notifyItemRemoved(oldListEnd)
            notifyItemRangeInserted(oldListEnd, numberOfItemsAdded)
        }

    }


    private val mOnItemClickedListener = object : ReposViewHolder.OnItemClickedListener {
        override fun onItemClicked(model: RepoModel) {

        }
    }

    private fun canLoadMore(): Boolean {
        return currentPage == null || currentPage!!.hasMore
    }

    fun setInfiniteAdapterCallback(infiniteAdapterCallback: InfiniteAdapterCallback) {
        this.infiniteAdapterCallback = infiniteAdapterCallback
    }

    interface InfiniteAdapterCallback {
        fun onRequestMoreItems()
    }

    companion object {
        private val LOAD_MORE_THRESHOLD = 5
    }
}
