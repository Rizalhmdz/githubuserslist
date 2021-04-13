package com.example.githubuserslist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var adapter : UserAdapter

    private lateinit var dataNama : Array<String>
    private lateinit var dataUsername : Array<String>
    private lateinit var dataPP : Array<String>
    private lateinit var dataFollowers : Array<String>
    private lateinit var dataFollowing : Array<String>
    private lateinit var dataLokasi : Array<String>

    private var users = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView : ListView = findViewById(R.id.lv_main)
        adapter = UserAdapter(this)
        listView.adapter = adapter

        prepare()
        addItem()

        listView.onItemClickListener = AdapterView.OnItemClickListener{ _, _, position, _ ->
            val user = User(
                    users[position].nama,
                    users[position].username,
                    users[position].profile_pict,
                    users[position].followers,
                    users[position].following,
                    users[position].lokasi
                    )
            val lihatDetail = Intent(this@MainActivity, UserDetail::class.java)
            lihatDetail.putExtra(UserDetail.DATA_NAMA, user)
            startActivity(lihatDetail)
        }
    }

    private fun addItem() {
        for(position in dataNama.indices){
            val user = User(
                    dataNama[position],
                    dataUsername[position],
                    dataPP[position],
                    dataFollowers[position],
                    dataFollowing[position],
                    dataLokasi[position]
            )
            users.add(user)
        }
        adapter.user = users
    }

    private fun prepare() {
        dataNama = resources.getStringArray(R.array.data_nama)
        dataUsername = resources.getStringArray(R.array.data_username)
        dataPP = resources.getStringArray(R.array.data_pp)
        dataFollowers = resources.getStringArray(R.array.data_followers)
        dataFollowing = resources.getStringArray(R.array.data_following)
        dataLokasi = resources.getStringArray(R.array.data_location)

    }
}