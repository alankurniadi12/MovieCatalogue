package com.example.moviecataloguefour.tvshow

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
import kotlinx.android.synthetic.main.fragment_tv_show.*

/**
 * A simple [Fragment] subclass.
 */
class TvShowFragment : Fragment() {


    companion object {
        val TAG = TvShowFragment::class.java.simpleName
    }
    private lateinit var viewModel: TvShowViewModel
    private lateinit var adapter: TvShowAdapter
    private val list = ArrayList<TvShow>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TvShowAdapter()
        adapter.notifyDataSetChanged()

        viewModel = ViewModelProvider (this, ViewModelProvider.NewInstanceFactory()).get(TvShowViewModel::class.java)
        showRecyclerView()
        showLoading(true)
        viewModel.setListTvShow()

        viewModel.getTvShow().observe(viewLifecycleOwner, Observer { tvShowItems ->
            if (tvShowItems != null){
                adapter.setData(tvShowItems)
                showLoading(false)
            }
        })

    }

    private fun showRecyclerView() {
        rv_item_tvshow.layoutManager = LinearLayoutManager(context)
        rv_item_tvshow.adapter = adapter

        adapter.setData(list)

        adapter.setOnItemClickCallback(object : TvShowAdapter.OnItemClickCallback{
            override fun onItemClicked(tvShow: TvShow) {
                val intent = Intent(activity, DetailTvShowActivity::class.java)
                intent.putExtra(DetailTvShowActivity.EXTRA_TVSHOW, tvShow)
                startActivity(intent)
            }
        })
    }

    fun showLoading(state: Boolean){
        if (state){
            progressBar.visibility = View.VISIBLE
        }else{
            progressBar.visibility = View.GONE
        }
    }

}
