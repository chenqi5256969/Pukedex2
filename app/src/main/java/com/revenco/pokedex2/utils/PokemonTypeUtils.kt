package com.revenco.pokedex2.utils

import android.graphics.Color


object PokemonTypeUtils {
    fun getTypeColor(type: String): Int {
        return when (type) {
            "fighting" -> Color.parseColor("#9F422A")
            "flying" ->Color.parseColor("#90B1C5")
            "poison" -> Color.parseColor("#642785")
            "ground" ->Color.parseColor("#AD7235")
            "rock" -> Color.parseColor("#4B190E")
            "bug" -> Color.parseColor("#179A55")
            "ghost" ->Color.parseColor("#363069")
            "steel" -> Color.parseColor("#5C756D")
            "fire" -> Color.parseColor("#B22328")

            "water" ->Color.parseColor("#2648DC")
            "grass" -> Color.parseColor("#007C42")
            "electric" ->Color.parseColor("#E0E64B")
            "psychic" ->Color.parseColor("#AC296B")
            "ice" -> Color.parseColor("#7ECFF2")
            "dragon" ->Color.parseColor("#378A94")
            "fairy" -> Color.parseColor("#9E1A44")
            "dark" -> Color.parseColor("#040706")
            else -> Color.parseColor("#040706")
        }
    }
}