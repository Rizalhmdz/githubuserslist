package com.example.consumeapp

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.consumeapp.Adapter.UserAdapter
import com.example.consumeapp.databinding.ActivityUserDetailBinding
import com.example.consumeapp.entity.UserItems
import com.example.consumeapp.model.MainViewModel
import com.example.consumeappt.Adapter.SectionPagerDetailAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetail : AppCompatActivity() {

    companion object {
        val TAG = UserDetail::class.java.simpleName
        const val EXTRA_USERNAME = "extra_username"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
        )
    }

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)
        setData()

        viewPagerConfig()

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
        if(dataUser.name == "null") binding.nameDetail.text = "-"
        else binding.nameDetail.text = dataUser.name
        if(dataUser.location == "null") binding.detailLocation.text = getString(R.string.no_set)
        else binding.detailLocation.text = dataUser.location
        binding.detailFollowing.text = dataUser.following
        binding.detailFollowers.text = dataUser.followers
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
}
