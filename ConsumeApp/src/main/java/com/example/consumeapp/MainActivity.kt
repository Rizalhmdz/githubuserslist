package com.example.consumeapp

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumeapp.Adapter.FavoriteAdapter
import com.example.consumeapp.Helper.MappingHelper
import com.example.consumeapp.databinding.ActivityMainBinding
import com.example.consumeapp.db.DatabaseContract
import com.example.consumeapp.entity.FavoriteItems
import com.example.consumeapp.model.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteAdapter
    private lateinit var favoriteViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBarTitle()

        binding.recyclerViewFavorite.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFavorite.setHasFixedSize(true)

        adapter = FavoriteAdapter()
        binding.recyclerViewFavorite.adapter = adapter

        favoriteViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)
        try{
            val handlerThread = HandlerThread("DataObserver")
            handlerThread.start()
            val handler = Handler(handlerThread.looper)
            val myObserver = object : ContentObserver(handler) {
                override fun onChange(self: Boolean) {
                    loadNotesAsync()
                }
            }
            contentResolver.registerContentObserver(DatabaseContract.FavoriteUserColumns.CONTENT_URI, true, myObserver)
        }
        catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<FavoriteItems>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavoriteUser = list
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressbarFavorite.visibility = View.VISIBLE
        } else {
            binding.progressbarFavorite.visibility = View.GONE
        }
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            showLoading(true)
            val defFav = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(DatabaseContract.FavoriteUserColumns.CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favoriteData = defFav.await()
            showLoading(false)
            if (favoriteData.size > 0) {
                adapter.listFavoriteUser = favoriteData
            } else {
                adapter.listFavoriteUser = ArrayList()
                showSnackbarMessage(getString(R.string.not_found))
            }
        }
    }
    override fun onResume() {
        super.onResume()
        loadNotesAsync()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavoriteUser)
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.recyclerViewFavorite, message, Snackbar.LENGTH_SHORT).show()
    }
    private fun setActionBarTitle() {
        if (supportActionBar != null) {
            supportActionBar?.title = getString(R.string.favorite_users)
        }
    }
}
