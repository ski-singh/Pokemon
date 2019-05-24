package com.example.pokemon.UI

import com.google.gson.annotations.SerializedName

class Response (

    val name:String,
    val sprites: Sprites,
    val id:Int,
    val height:Int,
    val weight:Int,
    val order:Int,
    @SerializedName("base_experience") val baseExperience:Int



)

class Sprites (
     @SerializedName("back_default")val backDefault : String ,
     @SerializedName("front_default") val frontDefault : String,
     @SerializedName("front_shiny") val frontShiny : String,
     @SerializedName("back_shiny") val backShiny :String
     )






