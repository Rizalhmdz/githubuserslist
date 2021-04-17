package com.example.githubuserslist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.contentValuesOf
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserslist.Adapter.FavoriteAdapter
import com.example.githubuserslist.Adapter.SectionPagerDetailAdapter
import com.example.githubuserslist.Adapter.UserAdapter
import com.example.githubuserslist.databinding.ActivityUserDetailBinding
import com.example.githubuserslist.db.DatabaseContract
import com.example.githubuserslist.Helper.FavoriteUserHelper
import com.example.githubuserslist.Helper.MappingHelper
import com.example.githubuserslist.entity.FavoriteItems
import com.example.githubuserslist.entity.UserItems
import com.example.githubuserslist.model.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserDetail : AppCompatActivity(), View.OnClickListener {

    companion object {
        val EXTRA_USERNAME_FAV = "extra_username"
        val TAG = UserDetail::class.java.simpleName
        const val EXTRA_USERNAME = "extra_username"
        @StringRes
        private val TAB_TITLES = intArrayOf(
                R.string.tab_text_1,
                R.string.tab_text_2,
        )
    }

    private lateinit var favoriteUserHelper: FavoriteUserHelper
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: UserAdapter
//    private lateinit var adapterF: FavoriteAdapter

    private var isFavorite = false

    private var name: String? = ""
    private var username: String? = ""
    private var profile_picture: String? = ""
    private var followers: String? = ""
    private var following: String? = ""
    private var location: String?  = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        favoriteUserHelper = FavoriteUserHelper.getInstance(applicationContext)
        favoriteUserHelper.open()
        setData()
        favoriteChecker()

        binding.fabFavorite.setOnClickListener(this)

        viewPagerConfig()

    }

    private fun favoriteChecker() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = username?.let { favoriteUserHelper.queryByUsername(it) }
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val rowUsername = deferredNotes.await()
            if (rowUsername.size > 0) {
                isFavorite = true
            } else {
                isFavorite = false
            }
            setStatusFavorite(isFavorite)
        }
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    private fun setData() {
        var dataUser = intent.getParcelableExtra(EXTRA_USERNAME) as UserItems
        Glide.with(this)
            .load(dataUser.profile_picture)
            .into(binding.detailPp)
        binding.usernameDetail.text = dataUser.username
        if(dataUser.name == "null") binding.detailLocation.text = getString(R.string.null_name)
        else binding.nameDetail.text = dataUser.name
        if(dataUser.location == "null") binding.detailLocation.text = getString(R.string.no_set)
        else binding.detailLocation.text = dataUser.location
        binding.detailFollowing.text = dataUser.following
        binding.detailFollowers.text = dataUser.followers

        this.name = dataUser.name
        this.username = dataUser.username
        this.profile_picture = dataUser.profile_picture
        this.followers = dataUser.followers
        this.following = dataUser.following
        this.location = dataUser.location


        dataUser.username?.let { setActionBarTitle(it) }
    }

    private fun viewPagerConfig() {
        val sectionsPagerAdapter = SectionPagerDetailAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    override fun onClick(view: View) {
        if (view.id == R.id.fab_favorite) {
            if(isFavorite == true){
                this.username?.let { favoriteUserHelper.deleteByUsername(it) }
                isFavorite = false
                setStatusFavorite(isFavorite)
                Toast.makeText(this, "deleted from Favorite", Toast.LENGTH_SHORT).show()
            }
            else{
                favoriteUserHelper.open()
                val values = contentValuesOf(
                    DatabaseContract.FavoriteUserColumns.USERNAME to this.username,
                    DatabaseContract.FavoriteUserColumns.NAME to this.name,
                    DatabaseContract.FavoriteUserColumns.PROFILE_PICTURE to this.profile_picture,
                    DatabaseContract.FavoriteUserColumns.FOLLOWING to this.following,
                    DatabaseContract.FavoriteUserColumns.FOLLOWERS to this.followers,
                    DatabaseContract.FavoriteUserColumns.LOCATION to this.location
                )
                favoriteUserHelper.insert(values)
                isFavorite = true
                setStatusFavorite(isFavorite)
                Toast.makeText(this, "Added to Favorite", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun setStatusFavorite(status : Boolean){
        val Mark: Int = R.drawable.star_filled_52px
        val unMark: Int = R.drawable.star_52px
        if (status == true) {
            binding.fabFavorite.setImageResource(Mark)
        } else {
            binding.fabFavorite.setImageResource(unMark)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        favoriteUserHelper.close()
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
                val mIntent = Intent(this, FavoriteUser::class.java)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
