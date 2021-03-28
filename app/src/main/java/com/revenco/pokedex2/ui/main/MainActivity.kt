package com.revenco.pokedex2.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView
import com.revenco.pokedex2.R
import com.revenco.pokedex2.model.Pokemon
import com.revenco.pokedex2.model.base.PukdexResult
import com.revenco.pokedex2.ui.adapter.MainRecyclerAdapter
import com.revenco.pokedex2.ui.detail.DetailActivity
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayout.onTransformationStartContainer
import com.skydoves.whatif.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
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
            loadMoreModule.setOnLoadMoreListener(loadMoreListener)
            setOnItemClickListener(recyclerViewItemClick)
        }
        mainViewModel.apply {
            fetchPokemonList(page = 0, isLoading = true)
            resultLiveData.observe(this@MainActivity, resultObserver)
        }
        mainSwipeRefresh.setOnRefreshListener {
            isLoadMore = false
            mainViewModel.fetchPokemonList(0)
        }
    }

    private var isLoadMore = false
    private val loadMoreListener =
        OnLoadMoreListener {
            isLoadMore = true
            mainViewModel.fetchPokemonList(page = page, isLoading = false)
        }

    private val resultObserver = Observer<PukdexResult<List<Pokemon>>?> { result ->
        mainSwipeRefresh.isRefreshing = result?.ShowLoading ?: false
        when (result) {
            is PukdexResult.Success -> {
                result.successResult?.let { data ->
                    if (data.isNullOrEmpty()) {
                        mainRA.loadMoreModule.loadMoreEnd()
                    } else {
                        if (isLoadMore) {
                            mainRA.addData(data)
                            mainRA.loadMoreModule.loadMoreComplete()
                        } else {
                            mainRA.setList(data)
                        }
                        page++
                    }
                }
            }
            is PukdexResult.Error -> {
                result.ErrorMsg?.let {
                    mainRA.loadMoreModule.loadMoreFail()
                }
            }
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