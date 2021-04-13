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
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<User>(DATA_NAMA) as User
        binding.namaDetail.text = user.nama

        user.nama?.let { setActionBarTitle(it) }

        binding.usernameDetail.text = "@"+user.username
        Glide.with(this)
                .load(user.profile_pict)
                .into(binding.detailPp)
        binding.detaiFollowers.text = user.followers
        binding.detailFollowing.text = user.following
        binding.detailLokasi.text = user.lokasi
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }
}