package com.example.consumeapp.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumeapp.Adapter.UserAdapter
import com.example.consumeapp.R
import com.example.consumeapp.entity.UserItems
import com.example.consumeapp.model.MainViewModel
import kotlinx.android.synthetic.main.fragment_followers.*


class FollowersFragment : Fragment() {

    private lateinit var adapter: UserAdapter
    private lateinit var followersViewModel: MainViewModel


    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        setList()

    }

    fun setList(){

        rv_followers_fragment.layoutManager = LinearLayoutManager(activity)
        rv_followers_fragment.adapter = adapter
        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        followersViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        val dataUser = requireActivity().intent.getParcelableExtra(EXTRA_USERNAME) as UserItems
        val url = "https://api.github.com/users/"+dataUser.username.toString()+"/followers"
        followersViewModel.getList(url, requireActivity().applicationContext)
        showLoading(true)

        followersViewModel.getUsers().observe(requireActivity(), Observer { listFollowers ->
            if (listFollowers != null) {
                adapter.setData(listFollowers)
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressbarFollowers.visibility = View.VISIBLE
        } else {
            progressbarFollowers.visibility = View.INVISIBLE
        }
    }
}