package com.stanford.kotlinpokedex.Retrofit

import com.stanford.kotlinpokedex.Model.Pokedex
import io.reactivex.Observable
import retrofit2.http.GET

interface IPokemonList {
    @get:GET("pokedex.json")
    var listPokemon:Observable<Pokedex>
}