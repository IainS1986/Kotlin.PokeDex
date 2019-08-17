package com.stanford.kotlinpokedex.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stanford.kotlinpokedex.Common.Common
import com.stanford.kotlinpokedex.Interface.IItemClickListener
import com.stanford.kotlinpokedex.Model.Pokemon
import com.stanford.kotlinpokedex.R
import kotlinx.android.synthetic.main.pokemon_list_item.view.*

class PokemonListAdapter(internal var context: Context,
                         internal var pokemonList:List<Pokemon>): RecyclerView.Adapter<PokemonListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.pokemon_list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(pokemonList[position].img).into(holder.img_pokemon)
        holder.txt_pokemon.text = pokemonList[position].name

        holder.setItemClickListener(object:IItemClickListener{
            override fun onClick(view: View, position: Int) {
                //Toast.makeText(context, "Clicked at Pokemon: " + pokemonList[position].name, Toast.LENGTH_SHORT).show()
                LocalBroadcastManager.getInstance(context)
                    .sendBroadcast(Intent(Common.KEY_ENABLE_HOME).putExtra("position", position))
            }
        })
    }

    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        internal var img_pokemon: ImageView
        internal var txt_pokemon: TextView

        internal var itemClickListener: IItemClickListener?=null

        fun setItemClickListener(listener:IItemClickListener){
            itemClickListener = listener
        }

        init{
            img_pokemon = itemView.findViewById(R.id.pokemon_image) as ImageView
            txt_pokemon = itemView.findViewById(R.id.pokemon_name) as TextView

            itemView.setOnClickListener { view -> itemClickListener!!.onClick(view, adapterPosition) }
        }
    }
}