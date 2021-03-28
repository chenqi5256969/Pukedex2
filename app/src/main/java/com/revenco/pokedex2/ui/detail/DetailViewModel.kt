package com.revenco.pokedex2.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData

import com.revenco.pokedex2.base.LiveCoroutinesViewModel

import com.revenco.pokedex2.model.PokemonInfo
import com.revenco.pokedex2.model.base.PukdexResult
import com.revenco.pokedex2.repository.DetailRepository

import kotlinx.coroutines.flow.collect

class DetailViewModel @ViewModelInject constructor(private val detailRepository: DetailRepository) :
    LiveCoroutinesViewModel() {

    var resultLiveData = MutableLiveData<PukdexResult<PokemonInfo>>()

    fun fetchPokemonInfo(name: String) {
        liveOnUICoroutines {
            resultLiveData.value = PukdexResult.Loading(isShowLoading = true)
            detailRepository.fetchPokemonInfo(name, onError = { msg ->
                resultLiveData.value = PukdexResult.Error(errorMsgs = msg)
            }).collect { value: PokemonInfo ->
                resultLiveData.value = PukdexResult.Success(
                    success = value
                )
            }
        }
    }

}