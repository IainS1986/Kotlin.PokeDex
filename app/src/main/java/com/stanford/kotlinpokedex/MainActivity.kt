package com.stanford.kotlinpokedex

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.stanford.kotlinpokedex.Common.Common
import com.stanford.kotlinpokedex.Model.Pokemon

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //Create Broadcast handle
    private val showDetail = object:BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            if(intent!!.action!!.toString() == Common.KEY_ENABLE_HOME) {
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                supportActionBar!!.setDisplayShowHomeEnabled(true)

                //Replace Fragment
                val detailFragment = PokemonDetail.getNewInstance()
                val num = intent.getStringExtra("num")

                val bundle = Bundle()
                bundle.putString("num", num)
                detailFragment.arguments = bundle

                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.list_pokemon_fragment, detailFragment, "detail")
                fragmentTransaction.addToBackStack("detail")
                fragmentTransaction.commitAllowingStateLoss()

                //Set pokemon name for toolbar
                val pokemon = Common.findPokemonByNum(num)
                toolbar.title = pokemon!!.name
            }
        }
    }

    private val showEvolution = object:BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            if(intent!!.action!!.toString() == Common.KEY_NUM_EVOLUTION) {
                //Replace Fragment
                val detailFragment = PokemonDetail.getNewInstance()
                val num = intent.getStringExtra("num")

                val bundle = Bundle()
                bundle.putString("num", num)
                detailFragment.arguments = bundle

                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.list_pokemon_fragment, detailFragment, "detail")
                fragmentTransaction.addToBackStack("detail")
                fragmentTransaction.commitAllowingStateLoss()

                //Set pokemon name for toolbar
                val pokemon = Common.findPokemonByNum(num)
                toolbar.title = pokemon!!.name
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.setTitle("POKEMON LIST")
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)

        //Register broadcast
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(showDetail, IntentFilter(Common.KEY_ENABLE_HOME))

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(showEvolution, IntentFilter(Common.KEY_NUM_EVOLUTION))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            android.R.id.home -> {
                toolbar.title = "POKEMON LIST"

                //Clear all fragment in stack with name 'detail'
                supportFragmentManager.popBackStack("detail", FragmentManager.POP_BACK_STACK_INCLUSIVE)

                supportActionBar!!.setDisplayShowHomeEnabled(false)
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1)
        {
            supportFragmentManager.popBackStackImmediate()

            var fragment = supportFragmentManager.findFragmentByTag("detail")
            if (fragment is PokemonDetail)
            {
                toolbar.title = fragment.pokemon!!.name
            }
        }
        else
        {
            toolbar.title = "POKEMON LIST"
            supportActionBar!!.setDisplayShowHomeEnabled(false)
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)

            super.onBackPressed()
        }
    }
}
