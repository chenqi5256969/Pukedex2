package com.revenco.pokedex2.ui.adapter.loadmore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.revenco.pokedex2.R


class LoadMoreAdapter <T : RecyclerView.ViewHolder>(private  val adapter:RecyclerView.Adapter<T>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val LOAD_MORE = 0x00012
    var state: Int = 0

    fun setLoadState(loadState: LoadState) {
        state = loadState.ordinal
    }

    override fun getItemViewType(position: Int): Int {
        if (position >= adapter.itemCount) {
            //到达底部
            return LOAD_MORE
        }
        return adapter.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == LOAD_MORE) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.recycler_load_more, parent, false)
            return LoadMoreViewHolder((view))
        }
        return adapter.onCreateViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return adapter.itemCount + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LoadMoreAdapter<*>.LoadMoreViewHolder) when (state) {
            LoadState.LOADING.ordinal -> {
                holder.linearLoading.visibility = View.VISIBLE
                holder.loadComplete.visibility = View.GONE
                holder.loadFaile.visibility = View.GONE
            }
            LoadState.SUCCESS.ordinal -> {
                holder.linearLoading.visibility = View.GONE
                holder.loadComplete.visibility = View.VISIBLE
                holder.loadFaile.visibility = View.GONE
            }
            LoadState.FAILURE.ordinal -> {
                holder.linearLoading.visibility = View.GONE
                holder.loadComplete.visibility = View.GONE
                holder.loadFaile.visibility = View.VISIBLE
            }
            LoadState.LOADINGSUCCES.ordinal -> {
                holder.linearLoading.visibility = View.GONE
                holder.loadComplete.visibility = View.GONE
                holder.loadFaile.visibility = View.GONE
            }
        } else {
            adapter.onBindViewHolder(holder as T, position)
        }
    }

    inner class LoadMoreViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        val linearLoading = view.findViewById<LinearLayout>(R.id.linearLoading)
        val loadComplete = view.findViewById<TextView>(R.id.loadComplete)
        val loadFaile = view.findViewById<TextView>(R.id.loadFaile)
    }
}