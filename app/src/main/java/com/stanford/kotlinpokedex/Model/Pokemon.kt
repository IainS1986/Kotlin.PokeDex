package com.stanford.kotlinpokedex.Model

class Pokemon {
    var id:Int? = null
    var num:String? = null
    var name:String? = null
    var img:String? = null
    var type:List<String>? = null
    var height:String? = null
    var weight:String? = null
    var candy:String? = null
    var candy_count:Int? = null
    var egg:String? = null
    var spawn_change:Double? = null
    var avg_spawn:Double? = null
    var spawn_time:String? = null
    var multipliers:List<Double>? = null
    var weaknesses:List<String>? = null
    var next_evolution:List<Evolution>? = null
    var prev_evolution:List<Evolution>? = null
}