package com.revenco.pokedex2.persistence

import androidx.room.TypeConverter
import com.revenco.pokedex2.model.PokemonInfo
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.json.JSONArray
import org.json.JSONObject


object TypeResponseConverter {

    private val moshi=Moshi.Builder().build()

    @JvmStatic
    @TypeConverter
    fun fromString(value: String): List<PokemonInfo.TypeResponse>? {
        val listType = Types.newParameterizedType(List::class.java, PokemonInfo.TypeResponse::class.java)
        val adapter: JsonAdapter<List<PokemonInfo.TypeResponse>> = moshi.adapter(listType)
        return adapter.fromJson(value)
    }

    @JvmStatic
    @TypeConverter
    fun fromInfoType(type: List<PokemonInfo.TypeResponse>?): String {
        val listType = Types.newParameterizedType(List::class.java, PokemonInfo.TypeResponse::class.java)
        val adapter: JsonAdapter<List<PokemonInfo.TypeResponse>> = moshi.adapter(listType)
        return adapter.toJson(type)
    }
}