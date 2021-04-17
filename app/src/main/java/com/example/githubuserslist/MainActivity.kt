package com.example.githubuserslist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserslist.Adapter.UserAdapter
import com.example.githubuserslist.databinding.ActivityMainBinding
import com.example.githubuserslist.model.MainViewModel
import com.example.githubuserslist.model.ReminderActivity

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(false)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                MainViewModel::class.java)

        binding.btnSearch.setOnClickListener {
            val keyword = binding.editUsername.text.toString()
            var url = "https://api.github.com/search/users?q=$keyword"
            if (keyword.isEmpty()) return@setOnClickListener

            showLoading(true)
            binding.status.text = "Searcing for \"$keyword\""
            mainViewModel.getSearch(url, this)
        }
        setList()
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
            R.id.set_reminder -> {
                val mIntent = Intent(this, ReminderActivity::class.java)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setList() {
        mainViewModel.getUsers().observe(this, { userItems ->
            if (userItems != null) {
                adapter.setData(userItems)
                binding.status.text = "Result"
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressbarMain.visibility = View.VISIBLE
        } else {
            binding.progressbarMain.visibility = View.GONE
        }
    }
}
