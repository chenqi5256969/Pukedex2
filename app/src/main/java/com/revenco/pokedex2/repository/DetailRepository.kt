package com.revenco.pokedex2.repository

import com.revenco.pokedex2.model.PokemonInfo
import com.revenco.pokedex2.network.PokedexClient
import com.revenco.pokedex2.persistence.PokemonInfoDao
import com.skydoves.sandwich.*
import com.skydoves.whatif.whatIfNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import javax.inject.Inject


class DetailRepository @Inject constructor(
    private val client: PokedexClient,
    private val infoDao: PokemonInfoDao
) {
    suspend fun fetchPokemonInfo(
        name: String,
        onSuccess: () -> Unit,
        onError: (msg: String) -> Unit
    ) =
        flow<PokemonInfo> {
            val pokemonInfo = infoDao.getPokemonInfo(name)
            if (pokemonInfo == null) {
                val response = client.fetchPokemonInfo(name)
                response.suspendOnSuccess {
                    data.whatIfNotNull { response ->
                        infoDao.insertPokemonInfo(response)
                        emit(response)
                        onSuccess()
                    }
                }.onError {
                    onError(message())
                }.onException {
                    onError(message())
                }
            } else {
                emit(pokemonInfo)
                onSuccess()
            }
        }.flowOn(Dispatchers.IO)

}