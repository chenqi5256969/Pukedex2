package com.revenco.pokedex2.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlin.random.Random

@Entity
@JsonClass(generateAdapter = true)
data class PokemonInfo(
    @field:Json(name = "id") @PrimaryKey val id: Int,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "height") val height: Int,
    @field:Json(name = "weight") val weight: Int,
    @field:Json(name = "base_experience") val experience: Int,
    @field:Json(name = "types") val types: MutableList<TypeResponse>,
    val hp: Int = Random.nextInt(maxHp),
    val attack: Int = Random.nextInt(maxAttack),
    val defense: Int = Random.nextInt(maxDefense),
    val speed: Int = Random.nextInt(maxSpeed),
    val exp: Int = Random.nextInt(maxExp)
) {

    fun getWeightString(): String = String.format("%.1f KG", weight.toFloat() / 10)
    fun getHeightString(): String = String.format("%.1f M", height.toFloat() / 10)
    fun getHpString(): String = "$hp/$maxHp"
    fun getAttackString(): String = "$attack/$maxAttack"
    fun getDefenseString(): String = "$defense/$maxDefense"
    fun getSpeedString(): String = "$speed/$maxSpeed"

    @JsonClass(generateAdapter = true)
    data class TypeResponse(
        @field:Json(name = "slot") val slot: Int,
        @field:Json(name = "type") val type: Type
    )

    @JsonClass(generateAdapter = true)
    data class Type(
        @field:Json(name = "name") val name: String
    )

    companion object {
        const val maxHp = 300
        const val maxAttack = 300
        const val maxDefense = 300
        const val maxSpeed = 300
        const val maxExp = 1000
    }
}
