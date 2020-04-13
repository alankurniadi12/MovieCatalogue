package com.example.moviecataloguefour.tvshow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviecataloguefour.R
import kotlinx.android.synthetic.main.item_tvshow.view.*

class TvShowAdapter: RecyclerView.Adapter<TvShowAdapter.ListViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    private val mData = ArrayList<TvShow>()
    fun setData(items: ArrayList<TvShow>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val mView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_tvshow, viewGroup, false)
        return ListViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(listViewHolder: ListViewHolder, position: Int) {
        listViewHolder.bind(mData[position])
    }

    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(tvShow: TvShow){
            with(itemView){
                tv_item_title_tvshow.text = tvShow.title
                tv_item_desc_tvshow.text = tvShow.description
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w154/"+tvShow.poster)
                    .into(img_item_tvshow)

                itemView.setOnClickListener {onItemClickCallback?.onItemClicked(tvShow)}
            }
        }
    }
    interface OnItemClickCallback {
        fun onItemClicked(tvShow: TvShow)
    }
}


