package com.example.pokemon.Database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(Pokemon::class),version=3)
abstract class PokemonDatabase:RoomDatabase(){

   abstract fun pokemonDao():PokemonDao
}


