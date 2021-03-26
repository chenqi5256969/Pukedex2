package com.revenco.pokedex2.network

import com.revenco.pokedex2.model.PokemonInfo
import com.revenco.pokedex2.model.PokemonResponse
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.ResponseDataSource
import retrofit2.Call
import retrofit2.http.*

interface PokedexService {

    @GET("pokemon")
    suspend fun fetchPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): ApiResponse<PokemonResponse>

    @GET("pokemon/{name}")
    suspend fun fetchPokemonInfo(@Path("name") name: String): ApiResponse<PokemonInfo>

    

}