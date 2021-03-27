package com.revenco.pokedex2.ui.main

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.revenco.pokedex2.base.LiveCoroutinesViewModel
import com.revenco.pokedex2.model.Pokemon
import com.revenco.pokedex2.model.base.PukdexResult
import com.revenco.pokedex2.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
) : LiveCoroutinesViewModel() {

    var result = MutableLiveData<PukdexResult<List<Pokemon>>>()

    fun fetchPokemonList(page: Int, isLoading: Boolean = true) {
        liveOnUICoroutines {
            result.value = PukdexResult.Success(isShowLoading = isLoading)
            mainRepository.fetchPokemonList(page, onSuccess = {
            }, onError = { msg ->
                result.value = PukdexResult.Error(errorMsgs = msg)
            }).collect { value: List<Pokemon> ->
                result.value = PukdexResult.Success(
                    success = value,
                    isShowLoading = false
                )
            }
        }
    }
}