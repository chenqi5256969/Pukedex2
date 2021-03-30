package com.revenco.pokedex2.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.revenco.pokedex2.model.Pokemon
import com.revenco.pokedex2.model.PokemonInfo

/**
 * 切记:
 * 在使用room的过程中，不能直接存储list，需要进行类型转换
 */
@Database(entities = [Pokemon::class, PokemonInfo::class], version = 1, exportSchema = true)
@TypeConverters(value = [TypeResponseConverter::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun pokemonInfoDao(): PokemonInfoDao
}