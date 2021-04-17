package com.example.githubuserslist

import android.content.Intent
import android.media.tv.TvContract.Channels.CONTENT_URI
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserslist.Adapter.FavoriteAdapter
import com.example.githubuserslist.Helper.FavoriteUserHelper
import com.example.githubuserslist.Helper.MappingHelper
import com.example.githubuserslist.databinding.ActivityFavoriteUserBinding
import com.example.githubuserslist.entity.FavoriteItems
import com.example.githubuserslist.model.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteUser : AppCompatActivity() {



    private lateinit var adapter: FavoriteAdapter
    private lateinit var favoriteViewModel: MainViewModel
    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var favoriteUserHelper: FavoriteUserHelper

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        favoriteUserHelper = FavoriteUserHelper.getInstance(applicationContext)
        favoriteUserHelper.open()

        setActionBarTitle()

        binding.recyclerViewFavorite.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFavorite.setHasFixedSize(true)

        adapter = FavoriteAdapter(this)
        binding.recyclerViewFavorite.adapter = adapter

        favoriteViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                MainViewModel::class.java)

        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<FavoriteItems>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavoriteUser = list
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.setting_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.change_language -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.favorite_page -> {
//                val mIntent = Intent(this, FavoriteUser::class.java)
//                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
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
                val cursor = favoriteUserHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favoriteData = defFav.await()
            showLoading(false)
            if (favoriteData.size > 0) {
                adapter.listFavoriteUser = favoriteData
            } else {
                adapter.listFavoriteUser = ArrayList()
                showSnackbarMessage("List Not Found")
            }
        }
    }
    override fun onResume() {
        super.onResume()
        loadNotesAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteUserHelper.close()
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
            supportActionBar?.title = "Favorite Users"
        }
    }
}