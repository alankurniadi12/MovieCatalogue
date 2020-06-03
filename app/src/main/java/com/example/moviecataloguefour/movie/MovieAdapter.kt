package com.example.moviecataloguefour.movie

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviecataloguefour.R
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter: RecyclerView.Adapter<MovieAdapter.ListViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    private val mData = ArrayList<Movie>()
    fun setData(items: ArrayList<Movie>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    /*fun addItem(movie: Movie) {
        this.mData.add(movie)
        notifyItemInserted(this.mData.size - 1)
    }

    fun updataItem(position: Int, movie: Movie) {
        this.mData[position] = movie
        notifyItemChanged(position, movie)
    }

    fun removeItem(position: Int) {
        this.mData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemChanged(position, this.mData.size)
    }*/
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val mView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_movie, viewGroup, false)
        return ListViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(listViewHolder: ListViewHolder, position: Int) {
        listViewHolder.bind(mData[position])
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            with(itemView){
                tv_item_title_movie.text = movie.title
                tv_item_desc_movie.text = movie.description
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w154/"+movie.poster)
                    .into(img_item_movie)

                itemView.setOnClickListener {onItemClickCallback?.onItemClicked(movie)}
            }
        }
    }
    interface OnItemClickCallback {
        fun onItemClicked(data: Movie)

    }
}


