package com.example.pokemon.UI

import android.arch.persistence.room.Room
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.pokemon.Database.Pokemon
import com.example.pokemon.Database.PokemonDatabase
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.ResponseBody
import java.io.IOException
import com.google.gson.Gson as Gson


class PokeFetcher(context: Context,workerParameters: WorkerParameters):Worker(context,workerParameters) {

    val db by lazy{ Room.databaseBuilder(applicationContext,
        PokemonDatabase::class.java,
        "Pokemon1.db").fallbackToDestructiveMigration().allowMainThreadQueries().build()}

    val gson=Gson()


    override fun doWork(): Result {

        val url=inputData.getString("URL")
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url).build()
        val call = client.newCall(request)


        call.enqueue( object : Callback {
            override fun onFailure(request: Request?, e: IOException?) {

            }

            override fun onResponse(response: com.squareup.okhttp.Response?) {
                val responseBody: ResponseBody? = response?.body()
                val result = responseBody?.string()
                val parsedObject = gson.fromJson(
                    result,
                    com.example.pokemon.UI.pokeName::class.java
                )
               val pokeCount = parsedObject.count
                Log.e("pokeCount", pokeCount.toString())
                val count=db.pokemonDao().getCount()
                Log.e("_____COUNT___",count.toString())

                if (count!=pokeCount) {


                    val requestOne = Request.Builder()
                        .url("https://pokeapi.co/api/v2/pokemon/?limit=" + pokeCount).build()
                    val callOne = client.newCall(requestOne)

                    val response = callOne.execute()


                    val responseBodyOne: ResponseBody? = response?.body()
                    val resultOne = responseBodyOne?.string()
                    val parsedObjectOne = gson.fromJson(
                        resultOne,
                        com.example.pokemon.UI.pokeName::class.java
                    )

                    db.pokemonDao().deleteAll()


                    for (i in 0..pokeCount - 1) {
                        Log.e("pokename" + i, parsedObjectOne.results[i].name)
                        val pokemon = Pokemon(
                            parsedObjectOne.results[i].name, null, null, null
                            , null, null, null, null, null, null
                        )
                        db.pokemonDao().insertName(pokemon)


                    }


                }
            }






        })


        return Result.success()
    }
}