package com.revenco.pokedex2.repository

import com.revenco.pokedex2.model.PokemonInfo
import com.revenco.pokedex2.network.PokedexClient
import com.revenco.pokedex2.persistence.PokemonInfoDao
import com.skydoves.whatif.whatIfNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class DetailRepository @Inject constructor(
    private val client: PokedexClient,
    private val infoDao: PokemonInfoDao
) : BaseRepository() {
    suspend fun fetchPokemonInfo(
        name: String,
        onError: (msg: String) -> Unit
    ) =
        flow<PokemonInfo> {
            val pokemonInfo = infoDao.getPokemonInfo(name)
            if (pokemonInfo == null) {
                val response = client.fetchPokemonInfo(name)
                safeHandleResult(response, successCallBack = { data ->
                    data.whatIfNotNull { response ->
                        infoDao.insertPokemonInfo(data.data!!)
                        emit(data.data!!)
                    }
                }, errorCallBack = { error ->
                    onError(error)
                })
            } else {
                emit(pokemonInfo)
            }
        }.flowOn(Dispatchers.IO)

}