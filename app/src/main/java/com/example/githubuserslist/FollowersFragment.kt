package com.example.githubuserslist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_following.*


class FollowersFragment : Fragment() {

    private lateinit var adapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var followersViewModel: MainViewModel

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        setList()
    }

    private fun setList() {
        rv_following_fragment.layoutManager = LinearLayoutManager(activity)
        rv_following_fragment.adapter = adapter
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        followersViewModel = ViewModelProvider(
                this, ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        val dataUser = activity!!.intent.getParcelableExtra(EXTRA_USERNAME) as UserItems
        val url = "https://api.github.com/users/"+dataUser.username.toString()+"/followers"
        followersViewModel.getList(url, activity!!.applicationContext)
        showLoading(true)

        followersViewModel.getUsers().observe(activity!!, Observer { listFollowing ->
            if (listFollowing != null) {
                adapter.setData(listFollowing)
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressbarFollowing.visibility = View.VISIBLE
        } else {
            progressbarFollowing.visibility = View.INVISIBLE
        }
    }

}