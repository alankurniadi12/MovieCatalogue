package com.example.moviecataloguefour.favorite

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviecataloguefour.CustomOnItemClickListener
import com.example.moviecataloguefour.R
import com.example.moviecataloguefour.movie.DetailMovieActivity
import com.example.moviecataloguefour.movie.Movie
import kotlinx.android.synthetic.main.item_fav_mov.view.*

class FavoriteMovieAdapter(private val activity: Activity): RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieViewHolder>() {
    var listFavMovie = ArrayList<Movie>()
        set(listFavMovie) {
            if (listFavMovie.size > 0){
                this.listFavMovie.clear()
            }
            this.listFavMovie.add(listFavMovie)

        notifyDataSetChanged()
    }

    fun addItem(favorite: Favorite){
        this.listFavMovie.add(favorite)
        notifyItemInserted(this.listFavMovie.size - 1)
    }

    fun updateItem(position: Int, favorite: Favorite) {
        this.listFavMovie[position] = favorite
        notifyItemChanged(position, favorite)
    }

    fun removeItem(position: Int){
        this.listFavMovie.removeAt(position)
        notifyItemRemoved(position)
        notifyItemChanged(position, this.listFavMovie.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fav_mov, parent, false)
        return FavoriteMovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteMovieViewHolder, position: Int) {
        holder.bind(listFavMovie[position])
    }

    override fun getItemCount(): Int = this.listFavMovie.size

    inner class FavoriteMovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(favorite: Favorite){
            with(itemView){
                tv_item_title_favorite.text = favorite.title
                tv_item_desc_favorite.text = favorite.description
                Glide.with(itemView.context).load("https://image.tmdb.org/t/p/w154/"+favorite.poster).into(img_item_favorite)

                card_view_item_favorite.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                    override fun onItemClicked(v: View, position: Int) {
                        val intent = Intent(activity, DetailMovieActivity::class.java)
                        intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, favorite)
                        activity.startActivity(intent)
                    }
                }))
            }
        }
    }
}


