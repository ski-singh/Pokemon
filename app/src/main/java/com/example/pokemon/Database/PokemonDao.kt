package com.example.pokemon.Database

import android.arch.persistence.room.*
import android.support.annotation.RawRes

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun Insert(pokemon:Pokemon)

    @Query("SELECT * FROM pokemon ORDER BY name ASC")
    fun getAll():List<Pokemon>

    @Query("SELECT COUNT(name) FROM pokemon")
    fun  getCount():Int

    @Query("SELECT id FROM pokemon where name LIKE:name")
    fun getId(name:String):Int

    @Query("SELECT * FROM pokemon where name LIKE:pokiname")
    fun getPokemon(pokiname:String):Pokemon

    @Query("SELECT name FROM pokemon ORDER BY name ASC")
    fun getName():Array<String>


    @Insert
    fun insertName(pokemon: Pokemon)

    @Update
    fun Update(pokemon: Pokemon)

    @Query("DELETE FROM pokemon")
    fun deleteAll()



}