package com.revenco.pokedex2.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData

import com.revenco.pokedex2.base.LiveCoroutinesViewModel

import com.revenco.pokedex2.model.PokemonInfo
import com.revenco.pokedex2.repository.DetailRepository

import kotlinx.coroutines.flow.collect

class DetailViewModel @ViewModelInject constructor(private val detailRepository: DetailRepository) :
    LiveCoroutinesViewModel() {

    var uiState = MutableLiveData<UiModel>()

    fun fetchPokemonInfo(name: String) {
        liveOnUICoroutines {
            emitUiState(showLoading = true)
            detailRepository.fetchPokemonInfo(name, onSuccess = {
            }, onError = { msg ->
                emitUiState(showLoading = false, showError = msg)
            }).collect { value: PokemonInfo ->
                emitUiState(showLoading = false, showSuccess = value)
            }
        }
    }

    private fun emitUiState(
        showLoading: Boolean = false,
        showError: String? = null,
        showSuccess: PokemonInfo? = null,
        showLoadMore: Boolean = false,
        showRefresh: Boolean = false,
        needLogin: Boolean? = null
    ) {
        val uiModel =
            UiModel(showLoading, showError, showSuccess, showLoadMore, showRefresh, needLogin)
        uiState.postValue(uiModel)
    }


    data class UiModel(
        val showLoading: Boolean,
        val showError: String?,
        val showSuccess: PokemonInfo?,
        val showLoadMore: Boolean,
        val showRefresh: Boolean,
        val needLogin: Boolean? = null
    )
}