package com.revenco.pokedex2.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.revenco.pokedex2.model.Pokemon
import com.revenco.pokedex2.network.PokedexClient
import com.revenco.pokedex2.persistence.PokemonDao
import com.skydoves.sandwich.*
import com.skydoves.whatif.whatIfNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.Dispatcher
import javax.inject.Inject


class MainRepository @Inject constructor(
    private val client: PokedexClient,
  private  val dao: PokemonDao
) {
    suspend fun fetchPokemonList(
        page: Int,
        onSuccess: () -> Unit,
        onError: (msg: String) -> Unit
    ) = flow {
        var pokemonList = dao.getPokemonList(page)
        if (pokemonList.isEmpty()) {
            val response = client.fetchPokemonList(page)
            response.suspendOnSuccess {
                data.whatIfNotNull { response ->
                    pokemonList = response.results
                    pokemonList.forEach { pokemon: Pokemon ->
                        pokemon.page = page
                    }
                    dao.insertPokemonList(pokemonList)
                    emit(pokemonList)
                    onSuccess()
                }
            }.onError {
                onError(message())
            }.onException {
                onError(message())
            }
        } else {
            emit(pokemonList)
            onSuccess()
        }
    }.flowOn(Dispatchers.IO)
}