package com.stanford.kotlinpokedex


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.inputmethodservice.InputMethodService
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mancj.materialsearchbar.MaterialSearchBar
import com.stanford.kotlinpokedex.Adapter.PokemonListAdapter
import com.stanford.kotlinpokedex.Common.Common
import com.stanford.kotlinpokedex.Common.ItemOffsetDecoration
import com.stanford.kotlinpokedex.Model.Pokemon
import com.stanford.kotlinpokedex.Retrofit.IPokemonList
import com.stanford.kotlinpokedex.Retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_pokemon_list.*


class PokemonList : Fragment() {

    internal var compositeDisposable = CompositeDisposable()
    internal var iPokemonList:IPokemonList
    internal var last_suggest:MutableList<String> = ArrayList()

    internal lateinit var recycler_view:RecyclerView
    internal lateinit var adapter:PokemonListAdapter
    internal lateinit var search_adapter:PokemonListAdapter
    internal lateinit var search_bar:MaterialSearchBar

    init {
        val retrofit = RetrofitClient.instance
        iPokemonList = retrofit.create(IPokemonList::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val itemView = inflater.inflate(R.layout.fragment_pokemon_list, container, false)

        recycler_view = itemView.findViewById(R.id.pokemon_recyclerview) as RecyclerView
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = GridLayoutManager(activity, 2)

        val itemDecoration = ItemOffsetDecoration(activity!!, R.dimen.spacing)
        recycler_view.addItemDecoration(itemDecoration)

        // Setup SearchBar
        search_bar = itemView.findViewById(R.id.search_bar) as MaterialSearchBar
        search_bar.setHint("Enter Pokemon Name")
        search_bar.setCardViewElevation(10)
        search_bar.addTextChangeListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val suggest = ArrayList<String>()
                for (search in last_suggest)
                    if (search.toLowerCase().contains(search_bar.text.toLowerCase()))
                        suggest.add(search)
                search_bar.lastSuggestions = suggest
            }
        })
        search_bar.setOnSearchActionListener(object: MaterialSearchBar.OnSearchActionListener{
            override fun onButtonClicked(buttonCode: Int) {

            }

            override fun onSearchStateChanged(enabled: Boolean) {
                if (!enabled)
                    pokemon_recyclerview.adapter = adapter
            }

            override fun onSearchConfirmed(text: CharSequence?) {
                startSearch(text.toString())
            }

        })

        fetchData()

        return itemView
    }

    @SuppressLint("ServiceCast")
    private fun startSearch(text: String) {
        if (Common.pokemonList.size > 0) {
            val result = ArrayList<Pokemon>()
            for (pokemon in Common.pokemonList)
                if (pokemon.name!!.toLowerCase().contains(text.toLowerCase()))
                    result.add(pokemon)
            search_adapter = PokemonListAdapter(activity!!, result)
            pokemon_recyclerview.adapter = search_adapter

            hideKeyboard()
        }
    }

    private fun fetchData(){
        compositeDisposable.add(iPokemonList.listPokemon
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ pokemonDex ->
                Common.pokemonList = pokemonDex.pokemon!!
                adapter = PokemonListAdapter(activity!!, Common.pokemonList)
                recycler_view.adapter = adapter

                last_suggest.clear()
                for (pokemon in Common.pokemonList)
                    last_suggest.add(pokemon.name!!)
                search_bar.visibility = View.VISIBLE
                search_bar.lastSuggestions = last_suggest
            }
        );
    }


    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(if (currentFocus == null) View(this) else currentFocus)
    }

    fun Context.hideKeyboard(view: View?) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view!!.windowToken, 0)
    }
}
