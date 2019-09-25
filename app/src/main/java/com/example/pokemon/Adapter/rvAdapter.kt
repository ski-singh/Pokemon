package com.example.pokemon

import android.content.Context

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pokemon.Database.Pokemon
import com.example.pokemon.UI.clickHandler
import kotlinx.android.synthetic.main.rv_card.view.*


class rvAdapter(val list:ArrayList<Pokemon>, val context: Context,val handler:clickHandler): RecyclerView.Adapter<rvAdapter.Viewholder>() {

    //creating view by inflating cardView layout
    override fun onCreateViewHolder(parent: ViewGroup, context: Int): Viewholder {
        val li=parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView=li.inflate(R.layout.rv_card,parent,false)
        return Viewholder(itemView)
    }
    //getting total count of arrayList
    override fun getItemCount(): Int {
        return list.size


    }
    //Binding data to cardView
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.itemView.cardTv.text=list[position].name.capitalize()
        holder.itemView.setOnClickListener{
            handler.handleOnClick(list[position].name)
        }
    }

    //single item which is displayed in recyclerView
     class Viewholder(itemView: View): RecyclerView.ViewHolder(itemView)
}