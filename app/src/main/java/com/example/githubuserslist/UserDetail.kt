package com.example.githubuserslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.contentValuesOf
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserslist.Adapter.FavoriteAdapter
import com.example.githubuserslist.Adapter.UserAdapter
import com.example.githubuserslist.Helper.MappingHelper
import com.example.githubuserslist.databinding.ActivityUserDetailBinding
import com.example.githubuserslist.db.DatabaseContract
import com.example.githubuserslist.db.FavoriteUserHelper
import com.example.githubuserslist.entity.FavoriteItems
import com.example.githubuserslist.model.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserDetail : AppCompatActivity(), View.OnClickListener {

    private var isFavorite = false
    private var favoriteItems: FavoriteItems? = null
//    private var position: Int = 0
    private lateinit var favoriteUserHelper: FavoriteUserHelper

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var adapterF: FavoriteAdapter


    private var name: String? = ""
    private var username: String? = ""
    private var profile_picture: String? = ""
    private var followers: String? = ""
    private var following: String? = ""
    private var location: String?  = ""

    companion object {
        val TAG = UserDetail::class.java.simpleName
        const val EXTRA_USERNAME = "extra_username"
        @StringRes
        private val TAB_TITLES = intArrayOf(
                R.string.tab_text_1,
                R.string.tab_text_2,
        )

//        const val EXTRA_FAVORITE = "extra_favorite"
//        const val EXTRA_POSITION = "extra_position"
//        const val REQUEST_ADD = 100
//        const val RESULT_ADD = 101
//        const val REQUEST_UPDATE = 200
//        const val RESULT_UPDATE = 201
//        const val RESULT_DELETE = 301
//        const val ALERT_DIALOG_CLOSE = 10
//        const val ALERT_DIALOG_DELETE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        binding.fabFavorite.setOnClickListener(this)

        viewPagerConfig()
        setData()
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    private fun setData() {
        val dataUser = intent.getParcelableExtra(EXTRA_USERNAME) as UserItems
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

//            GlobalScope.launch(Dispatchers.Main) {
//                val favoriteHelper = FavoriteUserHelper.getInstance(applicationContext)
//                favoriteHelper.open()
//                val deferredFavorite = async(Dispatchers.IO) {
//                    val cursor = dataUser.username?.let { favoriteHelper.queryByUsername(it) }
//                    MappingHelper.mapCursorToArrayList(cursor)
//                }
//                favoriteHelper.close()
////                showLoading(false)
//                val favorites = deferredFavorite.await()
//                if (favorites.size > 0) {
//
////                    adapterF.listFavoriteUser = favorites
//                } else {
////                    adapterF.listFavoriteUser = ArrayList()
//                    Toast.makeText(this@UserDetail, "No data", Toast.LENGTH_SHORT).show()
//                }
//            }

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
            if (isFavorite) {

                val result = this.username?.let { favoriteUserHelper.deleteByUsername(it).toLong() }
                if (result != null) {
                    if (result > 0) {
                        Toast.makeText(this@UserDetail, "deleted from Favorite", Toast.LENGTH_SHORT).show()
//                        finish()
                    } else {
                        Toast.makeText(this@UserDetail, "Failed delete from Favorite", Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                favoriteItems?.username = this.username
                favoriteItems?.name = this.name
                favoriteItems?.profile_picture = this.profile_picture
                favoriteItems?.followers = this.followers
                favoriteItems?.following = this.following
                favoriteItems?.location = this.location

                val values = contentValuesOf(
                    DatabaseContract.FavoriteUserColumns.USERNAME to this.username,
                    DatabaseContract.FavoriteUserColumns.NAME to this.name,
                    DatabaseContract.FavoriteUserColumns.PROFILE_PICTURE to this.profile_picture,
                    DatabaseContract.FavoriteUserColumns.FOLLOWING to this.following,
                    DatabaseContract.FavoriteUserColumns.FOLLOWERS to this.followers,
                    DatabaseContract.FavoriteUserColumns.LOCATION to this.location
                )
                val result = favoriteUserHelper.insert(values)
                if (result > 0) {
                    favoriteItems?.id = result.toInt()
//                    setResult(RESULT_ADD, intent)
//                    finish()
                } else {
                    Toast.makeText(this@UserDetail, "Failed add to Favorite", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}