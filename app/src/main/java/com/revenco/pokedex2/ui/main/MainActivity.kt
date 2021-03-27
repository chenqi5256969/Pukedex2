package com.revenco.pokedex2.ui.main

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.util.Executors
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView
import com.revenco.pokedex2.PokedexApp
import com.revenco.pokedex2.R
import com.revenco.pokedex2.base.LiveCoroutinesViewModel
import com.revenco.pokedex2.model.Pokemon
import com.revenco.pokedex2.model.base.PukdexResult
import com.revenco.pokedex2.ui.adapter.MainRecyclerAdapter
import com.revenco.pokedex2.ui.detail.DetailActivity
import com.skydoves.transformationlayout.TransformationActivity
import com.skydoves.transformationlayout.TransformationAppCompatActivity
import com.skydoves.transformationlayout.TransformationCompat.onTransformationStartContainer
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayout.onTransformationStartContainer
import com.skydoves.whatif.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var page: Int = 0

    private val mainRA: MainRecyclerAdapter by lazy {
        MainRecyclerAdapter()
    }

    private val mainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationStartContainer()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        mainRecycler.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = mainRA
        }
        mainRA.apply {
            loadMoreModule.loadMoreView = SimpleLoadMoreView()
            loadMoreModule.setOnLoadMoreListener(loadMore)
            setOnItemClickListener(recyclerViewItemClick)
        }
        mainViewModel.apply {
            fetchPokemonList(page = 0, isLoading = true)
            result.observe(this@MainActivity, resultObserver)
        }
        mainSwipeRefresh.setOnRefreshListener { mainViewModel.fetchPokemonList(0) }
    }

    private val loadMore =
        OnLoadMoreListener { mainViewModel.fetchPokemonList(page = page, isLoading = false) }

    private val resultObserver = Observer<PukdexResult<List<Pokemon>>?> { result ->
        mainSwipeRefresh.isRefreshing = result?.ShowLoading ?: false
        result?.successResult?.let { data ->
            if (data.isNullOrEmpty()) {
                mainRA.loadMoreModule.loadMoreEnd()
            } else {
                mainRA.addData(data)
                mainRA.loadMoreModule.loadMoreComplete()
                page++
            }
        }
        result?.ErrorMsg?.let {
        }
    }

    private val recyclerViewItemClick = OnItemClickListener { adapter, view, position ->
        val tfLayout = view.findViewById<TransformationLayout>(R.id.mainItemTF)
        DetailActivity.toDetailActivity(
            this@MainActivity, tfLayout,
            adapter.data[position] as Pokemon
        )
    }
}