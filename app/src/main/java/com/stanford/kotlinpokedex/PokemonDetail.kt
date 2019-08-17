package com.stanford.kotlinpokedex


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stanford.kotlinpokedex.Adapter.PokemonEvolutionAdapter
import com.stanford.kotlinpokedex.Adapter.PokemonTypeAdapter
import com.stanford.kotlinpokedex.Common.Common
import com.stanford.kotlinpokedex.Model.Pokemon


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PokemonDetail : Fragment() {

    internal lateinit var pokemon_img:ImageView

    internal lateinit var pokemon_name:TextView
    internal lateinit var pokemon_height:TextView
    internal lateinit var pokemon_weight:TextView

    lateinit var recycler_type:RecyclerView
    lateinit var recycler_weakness:RecyclerView
    lateinit var recycler_prev_evolution:RecyclerView
    lateinit var recycler_next_evolution:RecyclerView

    companion object{
        internal var instance:PokemonDetail?=null

        fun getInstance(): PokemonDetail{
            if(instance == null)
                instance = PokemonDetail()
            return instance!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var itemView: View = inflater.inflate(R.layout.fragment_pokemon_detail, container, false)

        val pokemon: Pokemon?
        if (arguments!!.getString("num") == null)
            pokemon = Common.pokemonList[arguments!!.getInt("position")]
        else
            pokemon = Common.findPokemonByNum(arguments!!.getString("num"))

        pokemon_img = itemView.findViewById(R.id.pokemon_image) as ImageView
        pokemon_name = itemView.findViewById(R.id.name) as TextView
        pokemon_height = itemView.findViewById(R.id.height) as TextView
        pokemon_weight = itemView.findViewById(R.id.weight) as TextView

        recycler_type = itemView.findViewById(R.id.recycler_type) as RecyclerView
        recycler_weakness = itemView.findViewById(R.id.recycler_weakness) as RecyclerView
        recycler_next_evolution = itemView.findViewById(R.id.recycler_next_evolution) as RecyclerView
        recycler_prev_evolution = itemView.findViewById(R.id.recycler_prev_evolution) as RecyclerView

        recycler_type.setHasFixedSize(true)
        recycler_weakness.setHasFixedSize(true)
        recycler_next_evolution.setHasFixedSize(true)
        recycler_prev_evolution.setHasFixedSize(true)

        recycler_type.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recycler_weakness.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recycler_next_evolution.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recycler_prev_evolution.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        setDetailPokemon(pokemon)

        return itemView
    }

    private fun setDetailPokemon(pokemon: Pokemon?) {

        //Load Image
        Glide.with(activity!!).load(pokemon!!.img).into(pokemon_img)

        pokemon_name.text = pokemon.name
        pokemon_height.text = "Height: " + pokemon.height
        pokemon_weight.text = "Weight: " + pokemon.weight

        var typeAdapter = PokemonTypeAdapter(activity!!, pokemon.type!!)
        recycler_type.adapter = typeAdapter

        var weaknessAdapter = PokemonTypeAdapter(activity!!, pokemon.weaknesses!!)
        recycler_weakness.adapter = weaknessAdapter

        if (pokemon.prev_evolution != null)
        {
            var prevEvolutionAdapter = PokemonEvolutionAdapter(activity!!, pokemon.prev_evolution!!)
            recycler_prev_evolution.adapter = prevEvolutionAdapter
        }

        if (pokemon.next_evolution != null)
        {
            var nextEvolutionAdapter = PokemonEvolutionAdapter(activity!!, pokemon.next_evolution!!)
            recycler_next_evolution.adapter = nextEvolutionAdapter
        }
    }


}
