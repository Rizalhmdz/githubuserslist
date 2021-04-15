package com.example.githubuserslist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_following.*


class FollowingFragment : Fragment() {

    private lateinit var adapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
//    private lateinit var binding: ActivityMainBinding


    companion object {
        const val EXTRA_USERNAME = "extra_username"
        private const val ARG_SECTION_NUMBER = "section_number"
//        @JvmStatic
//        fun newInstance(index: Int) =
//                FollowingFragment().apply {
//                    arguments = Bundle().apply {
//                        putInt(ARG_SECTION_NUMBER, index)
//                    }
//                }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val recyclerView: RecyclerView = view.findViewById(R.id.rv_following_fragment)
//        companion object {
//            val TAG = FragmentFollower::class.java.simpleName
//            const val EXTRA_DETAIL = "extra_detail"
//        }
//
        private val listData: ArrayList<UserItems> = ArrayList()
//        private lateinit var adapter: ListDataFollowerAdapter
        private lateinit var followingViewModel: MainViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        rv_following_fragment.layoutManager = LinearLayoutManager(activity)
        rv_following_fragment.adapter = adapter
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        followingViewModel = ViewModelProvider(
                this, ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        val dataUser = activity!!.intent.getParcelableExtra(EXTRA_USERNAME) as UserItems
//        config()
        val url = "https://api.github.com/users/"+dataUser.username.toString()+"/following"
        followingViewModel.getList(url, activity!!.applicationContext)
        showLoading(true)

        followingViewModel.getUsers().observe(activity!!, Observer { listFollowing ->
            if (listFollowing != null) {
                adapter.setData(listFollowing)
                showLoading(false)
            }
        })
    }

//        private fun config() {
//            rv_following_fragment.layoutManager = LinearLayoutManager(activity)
//            rv_following_fragment.setHasFixedSize(true)
//            rv_following_fragment.adapter = adapter
//        }

        private fun showLoading(state: Boolean) {
            if (state) {
                progressbarFollowing.visibility = View.VISIBLE
            } else {
                progressbarFollowing.visibility = View.INVISIBLE
            }
        }

    }