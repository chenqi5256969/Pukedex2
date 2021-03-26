package com.revenco.pokedex2.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.revenco.pokedex2.base.LiveCoroutinesViewModel
import com.revenco.pokedex2.model.Pokemon
import com.revenco.pokedex2.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
) : LiveCoroutinesViewModel() {

    var uiState = MutableLiveData<UiModel>()

    fun fetchPokemonList(page: Int, isLoading: Boolean = true, isLoadMore: Boolean = false) {
        viewModelScope.launch(Dispatchers.Main) {
            emitUiState(showLoading = isLoading, showLoadMore = isLoadMore)
            mainRepository.fetchPokemonList(page, onSuccess = {
            }, onError = { msg ->
                emitUiState(showLoading = false, showLoadMore = isLoadMore, showError = msg)
            }).collect { value: List<Pokemon> ->
                emitUiState(showLoading = false, showLoadMore = isLoadMore, showSuccess = value)
            }
        }
    }

    private fun emitUiState(
        showLoading: Boolean = false,
        showError: String? = null,
        showSuccess: List<Pokemon>? = null,
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
        val showSuccess: List<Pokemon>?,
        val showLoadMore: Boolean,
        val showRefresh: Boolean,
        val needLogin: Boolean? = null
    )
}