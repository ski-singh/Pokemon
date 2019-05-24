package com.example.pokemon.UI

import android.arch.persistence.room.Room
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.pokemon.Database.PokemonDatabase
import com.example.pokemon.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_row.*


class Main2Activity : AppCompatActivity() {

    val db by lazy{ Room.databaseBuilder(this,
        PokemonDatabase::class.java,
        "Pokemon1.db").fallbackToDestructiveMigration().allowMainThreadQueries().build()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_row)
        val name=intent.getStringExtra("pokename")
        val pokemon=db.pokemonDao().getPokemon(name)

        Picasso.get().load(pokemon.frontDefault)
            .into(front)
        Picasso.get().load(pokemon.frontShiny)
            .into(frontShiny)
        Picasso.get().load(pokemon.backDefault)
            .into(back)
        Picasso.get().load(pokemon.backShiny)
            .into(backShiny)
        pokename.text=pokemon.name.toUpperCase()
        pokeid.text=(pokemon.id).toString()
        pokeheight.text= pokemon.height.toString()
        pokeweigth.text=pokemon.weight.toString()
        pokeorder.text=pokemon.order.toString()
        baseExperince.text=pokemon.baseExperience.toString()

    }
}
