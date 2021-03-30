package com.revenco.pokedex2.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
@JsonClass(generateAdapter = true)
data class Pokemon(
    var page: Int = 0,
    @field:Json(name = "name") @PrimaryKey val name: String,
    @field:Json(name = "url") val url: String
) : Parcelable {
    fun getImageUrl(): String {
        val index = url.split("/".toRegex()).dropLast(1).last()
        return "https://pokeres.bastionbot.org/images/pokemon/$index.png"
    }
}