package com.revenco.pokedex2.repository

import com.revenco.pokedex2.model.Pokemon
import com.revenco.pokedex2.network.PokedexClient
import com.revenco.pokedex2.persistence.PokemonDao
import com.skydoves.whatif.whatIfNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


@ExperimentalCoroutinesApi
class MainRepository @Inject constructor(
    private val client: PokedexClient,
    private val dao: PokemonDao
) : BaseRepository() {
    suspend fun fetchPokemonList(
        page: Int,
        onError: (msg: String) -> Unit
    ) = flow {
        var pokemonList = dao.getPokemonList(page)
        if (pokemonList.isNullOrEmpty()) {
            val response = client.fetchPokemonList(page)
            safeHandleResult(response, successCallBack = { success ->
                success.whatIfNotNull { it ->
                    pokemonList = it.data!!.results
                    pokemonList!!.forEach { pokemon: Pokemon ->
                        pokemon.page = page
                    }
                    dao.insertPokemonList(pokemonList!!)
                    emit(pokemonList!!)
                }
            }, errorCallBack = { error ->
                onError(error)
            })
        } else {
            emit(pokemonList!!)
        }
    }.flowOn(Dispatchers.IO)
}