package com.example.pokemon.UI

import android.app.AlertDialog
import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatAutoCompleteTextView
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.work.*
import com.example.pokemon.Database.Pokemon
import com.example.pokemon.Database.PokemonDatabase
import com.example.pokemon.R
import com.example.pokemon.rvAdapter
import com.google.gson.Gson
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.ResponseBody
import kotlinx.android.synthetic.main.alert_view.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import java.io.IOException
import java.util.concurrent.TimeUnit


class FragmentMain:Fragment(){



    lateinit var SearchString:String
    lateinit var  adapter: rvAdapter
    lateinit var  pokemon:Pokemon
    lateinit var searchedPokemon:Pokemon
    lateinit var pokeName: Array<String>
    var pokeCount:Int=0
    val pokelist= arrayListOf<Pokemon>()
    val gson= Gson()



    val db by lazy{ Room.databaseBuilder(requireContext(),
        PokemonDatabase::class.java,
        "Pokemon1.db").fallbackToDestructiveMigration().allowMainThreadQueries().build()}


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


       val constraints=androidx.work.Constraints.Builder().
           setRequiredNetworkType(NetworkType.CONNECTED)
           .setRequiresBatteryNotLow(true)
           .build()

        val data=Data.Builder()
            .putString("URL","https://pokeapi.co/api/v2/pokemon").build()


        val pokeFetcherRequest= OneTimeWorkRequestBuilder<PokeFetcher>()
            .setInitialDelay(10,TimeUnit.HOURS)
            .setConstraints(constraints)
            .setInputData(data).build()

        WorkManager.getInstance().enqueue(pokeFetcherRequest)

        val peroidicFetcher= PeriodicWorkRequestBuilder<PokeFetcher>(1,TimeUnit.DAYS)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        WorkManager.getInstance().enqueue(peroidicFetcher)

        pokelist.addAll(db.pokemonDao().getAll())
        rv.layoutManager=GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
        adapter= rvAdapter(pokelist, requireContext(),activity as clickHandler)
        rv.adapter=adapter







        fab.setOnClickListener{

            val view= LayoutInflater.from(requireContext()).
                inflate(R.layout.alert_view,null,false)
            pokeName=db.pokemonDao().getName()

            val autoTextView=view.alertEt as AppCompatAutoCompleteTextView?
            val adapt=ArrayAdapter<String>(requireContext(),android.R.layout.select_dialog_item,pokeName)
            autoTextView?.threshold=1
            autoTextView?.setAdapter(adapt)


            val alertDia=AlertDialog.Builder(requireContext())
                .setTitle("Enter pokemon name to search")
                .setView(view)
                .setPositiveButton("Search") { dialog, which ->
                    val alertet = view.alertEt as EditText
                    SearchString = alertet?.text.toString()
                    if (SearchString in pokeName)
                    {
                    val id = db.pokemonDao().getId(SearchString)
                    Log.e("id",id.toString())


                    if (id==0) {


                        val client = OkHttpClient()
                        val request = Request.Builder()
                            .url("https://pokeapi.co/api/v2/pokemon/" + SearchString).build()
                        val call = client.newCall(request)


                        call.enqueue(object : Callback {
                            override fun onResponse(response: com.squareup.okhttp.Response?) {
                                val responseBody: ResponseBody? = response?.body()
                                val result = responseBody?.string()
                                val parsedObject = gson.fromJson(result, Response::class.java)


                                pokemon = Pokemon(
                                    parsedObject.name,
                                    parsedObject.sprites.frontDefault, parsedObject.sprites.frontShiny
                                    , parsedObject.sprites.backDefault, parsedObject.sprites.backShiny
                                    , parsedObject.id, parsedObject.height, parsedObject.weight,
                                    parsedObject.order, parsedObject.baseExperience
                                )
                                db.pokemonDao().Insert(pokemon)
                                Log.e("pokenamessssss",db.pokemonDao().getId(SearchString).toString())
                                searchedPokemon = db.pokemonDao().getPokemon(SearchString)
                                Log.e("poke",searchedPokemon.name+searchedPokemon.id)
                                pokelist.clear()
                                pokelist.addAll(db.pokemonDao().getAll())

                                activity?.runOnUiThread() {
                                    adapt.notifyDataSetChanged()
                                    adapter.notifyDataSetChanged()
                                    val intnt=Intent(context,Main2Activity::class.java)
                                    intnt.putExtra("pokename",SearchString)

                                    ContextCompat.startActivity(requireContext(), intnt, null)


                                }


                            }


                            override fun onFailure(request: Request?, e: IOException?) {
                                activity?.runOnUiThread() {
                                    Toast.makeText(context, "TURN ON MOBILE DATA", Toast.LENGTH_LONG).show()
                                }
                            }


                        })



                    } else {

                        searchedPokemon = db.pokemonDao().getPokemon(SearchString)
                        val intnt = Intent(context, Main2Activity::class.java)
                        intnt.putExtra("pokename", SearchString)

                        ContextCompat.startActivity(requireContext(), intnt, null)
                    }




                    }
                    else
                    {
                        val toast=Toast.makeText(context,"INVALID POKEMON NAME",Toast.LENGTH_SHORT)
                            toast.show()
                    }
                }.create()
            alertDia.show()


        }








    }

}

