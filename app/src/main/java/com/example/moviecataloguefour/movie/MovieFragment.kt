package com.example.moviecataloguefour.movie

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecataloguefour.R
import kotlinx.android.synthetic.main.fragment_movie.*


/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : Fragment() {

    companion object {
        val TAG = MovieFragment::class.java.simpleName
    }
    private lateinit var mainViewModel: MovieViewModel
    private lateinit var adapter: MovieAdapter
    private val list = ArrayList<Movie>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MovieAdapter()
        adapter.notifyDataSetChanged()

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MovieViewModel::class.java)
        showRecyclerViewMovie()
        showLoading(true)
        mainViewModel.setListMovies()

        mainViewModel.getMovies().observe(viewLifecycleOwner, Observer { movieItems ->
            if (movieItems != null){
                adapter.setData(movieItems)
                showLoading(false)
            }
        })
    }


    private fun showRecyclerViewMovie() {
        rv_item_movie.layoutManager = LinearLayoutManager(context)
        rv_item_movie.adapter = adapter

        adapter.setData(list)

        adapter.setOnItemClickCallback(object : MovieAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Movie) {
                val intent = Intent(activity, DetailMovieActivity::class.java)
                intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, data)
                startActivity(intent)

            }
        })
    }

    fun showLoading (state: Boolean){
        if (state) {
            progressBar.visibility = View.VISIBLE
        }else {
            progressBar.visibility = View.GONE
        }
    }

}
