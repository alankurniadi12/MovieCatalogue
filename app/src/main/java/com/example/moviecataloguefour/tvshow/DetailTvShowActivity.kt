package com.example.moviecataloguefour.tvshow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import com.bumptech.glide.Glide
import com.example.moviecataloguefour.R
import kotlinx.android.synthetic.main.activity_detail_tv_show.*

class DetailTvShowActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TVSHOW = "extra_tvshow"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tv_show)

        showLoading(true)
        getDataTvShow()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }






    private fun getDataTvShow() {
        val tvshow = intent.getParcelableExtra(EXTRA_TVSHOW) as TvShow
        tv_detail_title_tvshow.text = tvshow.title
        tv_detail_desc_tvshow.text = tvshow.description
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w154/"+tvshow.poster)
            .into(img_detail_tvshow)
        showLoading(false)
    }

    private fun showLoading (state: Boolean){
        if (state){
            progressbar_tv_detail.visibility = View.VISIBLE
        }else{
            progressbar_tv_detail.visibility = View.GONE
        }
    }
}
