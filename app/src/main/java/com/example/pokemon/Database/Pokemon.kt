package com.example.pokemon.Database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class Pokemon(
    @PrimaryKey @ColumnInfo(name = "Name")val name:String,
    val frontDefault: String?,
    val frontShiny: String?,
    val backDefault:String?,
    val backShiny:String?,
    val id:Int?,
    val height:Int?,
    val weight:Int?,
    val order:Int?,
    @ColumnInfo(name = "baseexperience") val baseExperience:Int?

)