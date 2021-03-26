package com.revenco.pokedex2.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.revenco.pokedex2.model.Pokemon
import com.revenco.pokedex2.model.PokemonInfo

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonList(pokemonInfo: List<Pokemon>)

    @Query("select * from Pokemon where page=:page")
    suspend fun getPokemonList(page: Int): List<Pokemon>
}