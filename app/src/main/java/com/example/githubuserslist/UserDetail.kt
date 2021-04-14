package com.example.githubuserslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.githubuserslist.databinding.ActivityUserDetailBinding

class UserDetail : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding


    companion object{
        const val DATA_NAMA = "Nama Akun"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityUserDetailBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val user = intent.getParcelableExtra<UserItems>(DATA_NAMA) as UserItems
//        binding.namaDetail.text = user.name
//
//        user.name?.let { setActionBarTitle(it) }
//
//        binding.usernameDetail.text = "@"+user.username
//        Glide.with(this)
//                .load(user.profile_picture)
//                .into(binding.detailPp)
//        binding.detaiFollowers.text = user.followers
//        binding.detailFollowing.text = user.following
//        binding.detailLokasi.text = user.location
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }
}