package com.example.githubuserslist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserslist.Adapter.UserAdapter
import com.example.githubuserslist.databinding.FragmentFollowersBinding
import com.example.githubuserslist.model.MainViewModel


class FollowersFragment : Fragment() {

    private lateinit var adapter: UserAdapter
    private lateinit var followersViewModel: MainViewModel
    private lateinit var binding: FragmentFollowersBinding

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFollowersBinding.inflate(layoutInflater)
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvFollowersFragment.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowersFragment.adapter = adapter
        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        followersViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)


//        setList()
    }

//    private fun setList() {
//
//    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressbarFollowers.visibility = View.VISIBLE
        } else {
            binding.progressbarFollowers.visibility = View.INVISIBLE
        }
    }

}