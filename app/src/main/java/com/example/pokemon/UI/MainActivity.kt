package com.example.pokemon.UI

import android.Manifest
import android.app.Activity
import android.arch.persistence.room.Room
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.example.pokemon.Database.Pokemon
import com.example.pokemon.Database.PokemonDatabase
import com.example.pokemon.R
import com.google.gson.Gson
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.ResponseBody
import java.io.IOException

class MainActivity : AppCompatActivity(),clickHandler {


    val gson=Gson()
    val db by lazy{ Room.databaseBuilder(this,
        PokemonDatabase::class.java,
        "Pokemon1.db").fallbackToDestructiveMigration().allowMainThreadQueries().build()}
    override fun handleOnClick(name: String) {
        Log.e("handle name",name)
        val id = db.pokemonDao().getId(name)
        if(id==0)
        {

            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://pokeapi.co/api/v2/pokemon/" +name).build()
            val call = client.newCall(request)

            call.enqueue(object : Callback {
                override fun onResponse(response: com.squareup.okhttp.Response?) {
                    val responseBody: ResponseBody? = response?.body()
                    val result = responseBody?.string()
                    val parsedObject = gson.fromJson(result, Response::class.java)


                   val pokemon = Pokemon(
                        parsedObject.name,
                        parsedObject.sprites.frontDefault, parsedObject.sprites.frontShiny
                        , parsedObject.sprites.backDefault, parsedObject.sprites.backShiny
                        , parsedObject.id, parsedObject.height, parsedObject.weight,
                        parsedObject.order, parsedObject.baseExperience
                    )
                    db.pokemonDao().Insert(pokemon)
                    Log.e("pokenamessssss",db.pokemonDao().getId(name).toString())
                    val searchedPokemon = db.pokemonDao().getPokemon(name)
                    Log.e("poke",searchedPokemon.name)


                    runOnUiThread() {
                        val intnt=Intent(baseContext,Main2Activity::class.java)
                        intnt.putExtra("pokename",name)

                        startActivity( intnt)


                    }


                }


                override fun onFailure(request: Request?, e: IOException?) {
                    runOnUiThread {
                        Toast.makeText(baseContext, "TURN ON MOBILE DATA", Toast.LENGTH_LONG).show()
                    }
                }


            })

        }
        else {
            val intnt = Intent(this, Main2Activity::class.java)
            intnt.putExtra("pokename", name)
            startActivity(intnt)
        }
    }
//    val gson= Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)



//      rv.layoutManager= LinearLayoutManager(this)
//        val client=OkHttpClient()
//        val request=Request.Builder()
//            .url("https://pokeapi.co/api/v2/pokemon/psyduck").build()
//        val call=client.newCall(request)
//
//        call.enqueue(object : Callback{
//            override fun onResponse(response: Response?) {
//                val responseBody: ResponseBody? = response?.body()
//                val result=responseBody?.string()
//                val parsedObject=gson.fromJson(result,Response::class.java)
//                Log.e("name",parsedObject.name)
//                Log.e("id",parsedObject.id.toString())
//                Log.e("height",parsedObject.height.toString())
//                Log.e("weight",parsedObject.weight.toString())
//                Log.e("order",parsedObject.order.toString())
//                Log.e("baseExperience",parsedObject.baseExperience.toString())
//
//            }
//
//            override fun onFailure(request: Request?, e: IOException?) {
//                Log.e("REQUEST","Request failed")
//            }
//
//
//
//        })

    }
}
