package com.example.githubuserslist

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.tv.TvContract.Channels.CONTENT_URI
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
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

//    private var isFavorite = false
//    private lateinit var gitHelper: FavoriteHelper
//    private var favorites: Favorite? = null
//    private lateinit var imageAvatar: String


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
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        favoriteUserHelper = FavoriteUserHelper.getInstance(applicationContext)
        favoriteUserHelper.open()

    favoriteItems = intent.getParcelableExtra(EXTRA_USERNAME) as FavoriteItems
    if (favoriteItems != null) {
        setDataObject()
        isFavorite = true
        val Mark: Int = R.drawable.star_filled_52px
        binding.fabFavorite.setImageResource(Mark)
    } else {
        setData()
    }

        binding.fabFavorite.setOnClickListener(this)

        viewPagerConfig()
//        setData()
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
            val Mark: Int = R.drawable.star_52px
            val unMark: Int = R.drawable.star_filled_52px

                if (isFavorite) {
                    favoriteUserHelper.deleteById(favoriteItems?.username.toString())
                    Toast.makeText(this, "deleted from Favorite", Toast.LENGTH_SHORT).show()
                    binding.fabFavorite.setImageResource(unMark)
                    isFavorite = false
                } else {

                    val values = contentValuesOf(
                        DatabaseContract.FavoriteUserColumns.USERNAME to this.username,
                        DatabaseContract.FavoriteUserColumns.NAME to this.name,
                        DatabaseContract.FavoriteUserColumns.PROFILE_PICTURE to this.profile_picture,
                        DatabaseContract.FavoriteUserColumns.FOLLOWING to this.following,
                        DatabaseContract.FavoriteUserColumns.FOLLOWERS to this.followers,
                        DatabaseContract.FavoriteUserColumns.LOCATION to this.location
                    )

                    isFavorite = true
                    contentResolver.insert(CONTENT_URI, values)
                    Toast.makeText(this, "Added to Favorite", Toast.LENGTH_SHORT)
                        .show()
                    binding.fabFavorite.setImageResource(Mark)
//                }
            }
        }
    }

    private fun setDataObject() {
        favoriteItems?.username = this.username
        favoriteItems?.name = this.name
        favoriteItems?.profile_picture = this.profile_picture
        favoriteItems?.followers = this.followers
        favoriteItems?.following = this.following
        favoriteItems?.location = this.location
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteUserHelper.close()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.setting_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.action_change_settings -> {
//                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
//                startActivity(mIntent)
//            }
//            R.id.action_change_notification -> {
//                val mIntent = Intent(this, NotificationSettings::class.java)
//                startActivity(mIntent)
//            }
//            R.id.action_favorite -> {
//                val mIntent = Intent(this, UserFavorite::class.java)
//                startActivity(mIntent)
//            }
        }
        return super.onOptionsItemSelected(item)
    }
}
//
//companion object {
//    const val EXTRA_DATA = "extra_data"
//    const val EXTRA_FAV = "extra_data"
//    const val EXTRA_NOTE = "extra_note"
//    const val EXTRA_POSITION = "extra_position"
//}

//private var isFavorite = false
//private lateinit var gitHelper: FavoriteHelper
//private var favorites: Favorite? = null
//private lateinit var imageAvatar: String
//
//@SuppressLint("SetTextI18n")
//override fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//    setContentView(R.layout.user_detail)
//
//    gitHelper = FavoriteHelper.getInstance(applicationContext)
//    gitHelper.open()
//
//    favorites = intent.getParcelableExtra(EXTRA_NOTE)
//    if (favorites != null) {
//        setDataObject()
//        isFavorite = true
//        val checked: Int = R.drawable.ic_favorite
//        btn_favorite.setImageResource(checked)
//    } else {
//        setData()
//    }
//
//    viewPagerConfig()
//    btn_favorite.setOnClickListener(this)
//}
//
//private fun viewPagerConfig() {
//    val viewPagerDetailAdapter = SectionsPagerAdapter(this, supportFragmentManager)
//    view_pager.adapter = viewPagerDetailAdapter
//    tabs.setupWithViewPager(view_pager)
//
//    supportActionBar?.elevation = 0f
//}
//
//private fun setActionBarTitle(title: String) {
//    if (supportActionBar != null) {
//        this.title = title
//    }
//}
//
//@SuppressLint("SetTextI18n", "StringFormatInvalid")
//private fun setData() {
//    val dataUser = intent.getParcelableExtra(EXTRA_DATA) as UserData
//    dataUser.name?.let { setActionBarTitle(it) }
//    detail_name.text = dataUser.name
//    detail_username.text = dataUser.username
//    detail_company.text = getString(R.string.company, dataUser.company)
//    detail_location.text = getString(R.string.location, dataUser.location)
//    detail_repository.text = getString(R.string.repository, dataUser.repository)
//    Glide.with(this)
//            .load(dataUser.avatar)
//            .into(detail_avatar)
//    imageAvatar = dataUser.avatar.toString()
//}
//
//@SuppressLint("SetTextI18n")
//private fun setDataObject() {
//    val favoriteUser = intent.getParcelableExtra(EXTRA_NOTE) as Favorite
//    favoriteUser.name?.let { setActionBarTitle(it) }
//    detail_name.text = favoriteUser.name
//    detail_username.text = favoriteUser.username
//    detail_company.text = favoriteUser.company
//    detail_location.text = favoriteUser.location
//    detail_repository.text = favoriteUser.repository
//    Glide.with(this)
//            .load(favoriteUser.avatar)
//            .into(detail_avatar)
//    imageAvatar = favoriteUser.avatar.toString()
//}
//
//override fun onClick(view: View) {
//    val checked: Int = R.drawable.ic_favorite
//    val unChecked: Int = R.drawable.ic_favorite_border
//    if (view.id == R.id.btn_favorite) {
//        if (isFavorite) {
//            gitHelper.deleteById(favorites?.username.toString())
//            Toast.makeText(this, getString(R.string.delete_favorite), Toast.LENGTH_SHORT).show()
//            btn_favorite.setImageResource(unChecked)
//            isFavorite = false
//        } else {
//            val dataUsername = detail_username.text.toString()
//            val dataName = detail_name.text.toString()
//            val dataAvatar = imageAvatar
//            val datacompany = detail_company.text.toString()
//            val dataLocation = detail_location.text.toString()
//            val dataRepository = detail_repository.text.toString()
//            val dataFavorite = "1"
//
//            val values = ContentValues()
//            values.put(USERNAME, dataUsername)
//            values.put(NAME, dataName)
//            values.put(AVATAR, dataAvatar)
//            values.put(COMPANY, datacompany)
//            values.put(LOCATION, dataLocation)
//            values.put(REPOSITORY, dataRepository)
//            values.put(FAVORITE, dataFavorite)
//
//            isFavorite = true
//            contentResolver.insert(CONTENT_URI, values)
//            Toast.makeText(this, getString(R.string.add_favorite), Toast.LENGTH_SHORT).show()
//            btn_favorite.setImageResource(checked)
//        }
//    }
//}
//
//override fun onDestroy() {
//    super.onDestroy()
//    gitHelper.close()
//}
//
//override fun onCreateOptionsMenu(menu: Menu): Boolean {
//    menuInflater.inflate(R.menu.main_menu, menu)
//    return super.onCreateOptionsMenu(menu)
//}
//
//override fun onOptionsItemSelected(item: MenuItem): Boolean {
//    when (item.itemId) {
//        R.id.action_change_settings -> {
//            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
//            startActivity(mIntent)
//        }
//        R.id.action_change_notification -> {
//            val mIntent = Intent(this, NotificationSettings::class.java)
//            startActivity(mIntent)
//        }
//        R.id.action_favorite -> {
//            val mIntent = Intent(this, UserFavorite::class.java)
//            startActivity(mIntent)
//        }
//    }
//    return super.onOptionsItemSelected(item)
//}
//}
