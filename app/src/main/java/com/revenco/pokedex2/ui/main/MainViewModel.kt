package com.revenco.pokedex2.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.revenco.pokedex2.base.LiveCoroutinesViewModel
import com.revenco.pokedex2.model.Pokemon
import com.revenco.pokedex2.model.base.PukdexResult
import com.revenco.pokedex2.repository.MainRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
) : LiveCoroutinesViewModel() {

    var resultLiveData = MutableLiveData<PukdexResult<List<Pokemon>>>()

    fun fetchPokemonList(page: Int, isLoading: Boolean = true) {
        liveOnUICoroutines {
            resultLiveData.value = PukdexResult.Success(isShowLoading = isLoading)
            mainRepository.fetchPokemonList(page, onError = { msg ->
                resultLiveData.value = PukdexResult.Error(errorMsgs = msg)
            }).collect { value: List<Pokemon> ->
                resultLiveData.value = PukdexResult.Success(
                    success = value,
                    isShowLoading = false
                )
            }
        }
    }
}