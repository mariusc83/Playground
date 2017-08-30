package org.mariusc.gitdemo.view.main.repos.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

import org.mariusc.gitdemo.data.network.model.RepoModel
import org.mariusc.gitdemo.databinding.ReposRowLayoutBinding


class ReposViewHolder(private val binding: ReposRowLayoutBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {


    private var mListener: OnItemClickedListener? = null


    fun setListener(mListener: OnItemClickedListener) {
        this.mListener = mListener
    }

    override fun onClick(v: View) {
        if (mListener != null) mListener!!.onItemClicked(binding.repo)
    }

    fun bind(repoModel: RepoModel) {
        binding.repo = repoModel
    }


    interface OnItemClickedListener {
        fun onItemClicked(model: RepoModel)
    }

    init {
        this.binding.root.setOnClickListener(this)
    }
}
