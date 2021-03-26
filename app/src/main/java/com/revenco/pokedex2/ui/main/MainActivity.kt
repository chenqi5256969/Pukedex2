package com.revenco.pokedex2.ui.main

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.util.Executors
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView
import com.revenco.pokedex2.PokedexApp
import com.revenco.pokedex2.R
import com.revenco.pokedex2.model.Pokemon
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


    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationStartContainer()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.fetchPokemonList(page = 0, isLoading = true, isLoadMore = false)
        mainRA.loadMoreModule.loadMoreView = SimpleLoadMoreView()
        mainRecycler.layoutManager = GridLayoutManager(this, 2)
        mainRecycler.adapter = mainRA
        mainRA.loadMoreModule.setOnLoadMoreListener {
            mainViewModel.fetchPokemonList(page = page, isLoading = false, isLoadMore = true)
        }

        mainViewModel.fetchPokemonList(0)
        mainViewModel.uiState.whatIfNotNull {
            it.observe(this,
                Observer<MainViewModel.UiModel?> { t ->
                    mainSwipeRefresh.isRefreshing = t?.showLoading ?: false
                    t?.showSuccess?.let { data ->
                        if (data.isNullOrEmpty()) {
                            mainRA.loadMoreModule.loadMoreEnd()
                        } else {
                            if (!t.showLoadMore) {
                                mainRA.setList(data)
                            } else {
                                mainRA.addData(data)
                            }
                            mainRA.loadMoreModule.loadMoreComplete()
                            page = 100
                        }
                    }
                    t?.showError?.let {
                    }
                })
        }
        mainSwipeRefresh.setOnRefreshListener { mainViewModel.fetchPokemonList(0) }
        mainRA.setOnItemClickListener { adapter, view, position ->
            val tfLayout = view.findViewById<TransformationLayout>(R.id.mainItemTF)
            DetailActivity.toDetailActivity(
                this@MainActivity, tfLayout,
                adapter.data[position] as Pokemon
            )
        }
    }
}