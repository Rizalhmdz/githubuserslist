package com.example.githubuserslist

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainViewModel : ViewModel() {

    private val listUsersNonMutable = ArrayList<UserItems>()
    private val listUsersMutable = MutableLiveData<ArrayList<UserItems>>()
    private val apiKey = "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"

    fun getUsers(): LiveData<ArrayList<UserItems>> {
        return listUsersMutable
    }

    fun getDataGitSearch(keyword: String, context: Context) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", apiKey)
        client.addHeader("User-Agent", "request")
        var url = "https://api.github.com/search/users?q=$keyword"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val result = responseBody?.let { String(it) }
                Log.d(MainActivity.TAG, result)
                try {
                    listUsersNonMutable.clear()
                    val jsonArray = JSONObject(result)
                    val item = jsonArray.getJSONArray("items")
                    for (i in 0 until item.length()) {
                        val jsonObject = item.getJSONObject(i)
                        val username = jsonObject.getString("login")
                        getUserDetail(username, context)
                    }
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    private fun getUserDetail(usernameLogin: String, context: Context) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", apiKey)
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$usernameLogin"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
            ) {
                val result = responseBody?.let { String(it) }
                Log.d(MainActivity.TAG, result)

                try {
                    val user = JSONObject(result)
                    val userItems = UserItems()

                    userItems.username = user.getString("login")
                    userItems.name = user.getString("name")
                    userItems.profile_picture = user.getString("avatar_url")
                    userItems.followers = user.getString("followers_url")
                    userItems.following = user.getString("following_url")
                    userItems.location = user.getString("location")

                    listUsersNonMutable.add(userItems)
                    listUsersMutable.postValue(listUsersNonMutable)
                }
                catch (e: Exception){
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }
}

//
//    fun setUser(username: String) {
//        val listItems = ArrayList<UserItems>()
//
////        val apiKey = "04bb0a2f7a73020876ba0affc09ba482"
//        val url = "https://api.github.com/users/${username}"
//
//        val client = AsyncHttpClient()
//        client.get(url, object : AsyncHttpResponseHandler() {
//            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
//                try {
//                    //parsing json
//                    val result = String(responseBody)
//                    val responseObject = JSONObject(result)
//                    val list = responseObject.getJSONArray("list")
//
//                    for (i in 0 until list.length()) {
//                        val user = list.getJSONObject(i)
//                        val userItems = UserItems()
//                        for (i in 0 until list.length()) {
//                            val user = list.getJSONObject(i)
//                            val userItem = UserItems()
//                            userItem.username = user.getString("login")
//                            userItem.name = user.getString("name")
//                            userItem.profile_picture = user.getString("avatar_url")
//                            userItem.followers = user.getString("followers_url")
//                            userItem.following = user.getString("following_url")
//                            userItem.location = user.getString("location")
//                            listItems.add(userItem)
//                        }
//                    }
//
//                    listUsers.postValue(listItems)
//                } catch (e: Exception) {
//                    Log.d("Exception", e.message.toString())
//                }
//
//            }
//
//            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
//                Log.d("onFailure", error.message.toString())
//            }
//        })
//    }


