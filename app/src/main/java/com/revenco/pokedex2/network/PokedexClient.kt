package com.revenco.pokedex2.network

import javax.inject.Inject

class PokedexClient @Inject constructor(
    private val pokedexService: PokedexService
) {
    suspend fun fetchPokemonList(page: Int) =
        pokedexService.fetchPokemonList(
            limit = PAGING_SIZE,
            offset = page * PAGING_SIZE
        )

    suspend fun fetchPokemonInfo(name: String) =
        pokedexService.fetchPokemonInfo(name = name)

    companion object {
        private val PAGING_SIZE = 20
    }
}