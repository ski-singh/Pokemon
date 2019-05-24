package com.example.pokemon

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pokemon.Database.Pokemon
import com.example.pokemon.UI.Main2Activity
import com.example.pokemon.UI.clickHandler
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_row.view.*
import kotlinx.android.synthetic.main.rv_card.view.*


class rvAdapter(val list:ArrayList<Pokemon>, val context: Context,val handler:clickHandler): RecyclerView.Adapter<rvAdapter.Viewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, context: Int): Viewholder {
        val li=parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView=li.inflate(R.layout.rv_card,parent,false)
        return Viewholder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size


    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {

        holder.itemView.cardTv.text=list[position].name.capitalize()
       holder.itemView.setOnClickListener{
           handler.handleOnClick(list[position].name)

//          val intnt=Intent(context,Main2Activity::class.java)
//              intnt.putExtra("pokename",list[position].name)
//
//           startActivity(context,intnt,null)


       }


//
//        Picasso.with(context).load(list[position].frontDefault)
//            .into(holder.itemView.front)
//        Picasso.with(context).load(list[position].frontShiny)
//            .into(holder.itemView.frontShiny)
//        Picasso.with(context).load(list[position].backDefault)
//            .into(holder.itemView.back)
//        Picasso.with(context).load(list[position].backShiny)
//            .into(holder.itemView.backShiny)
//        holder.itemView.name.text=list[position].name
//        holder.itemView.pokeid.text=(list[position].id).toString()
//        holder.itemView.pokeheight.text= list[position].height.toString()
//        holder.itemView.pokeweigth.text=list[position].weight.toString()
//        holder.itemView.pokeorder.text=list[position].order.toString()
//        holder.itemView.baseExperince.text=list[position].baseExperience.toString()



    }

     class Viewholder(itemView: View): RecyclerView.ViewHolder(itemView) {



    }
}