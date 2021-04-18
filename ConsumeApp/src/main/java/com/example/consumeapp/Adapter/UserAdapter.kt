package com.example.consumeapp.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.consumeapp.R
import com.example.consumeapp.UserDetail
import com.example.consumeapp.databinding.UserItemsBinding
import com.example.consumeapp.entity.UserItems

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>(){
    var mData = ArrayList<UserItems>()
    fun setData(items: ArrayList<UserItems>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): UserViewHolder {
        val mView = LayoutInflater.from(viewGroup.context).inflate(R.layout.user_items, viewGroup, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mData[position])
        val data = mData[position]
        holder.itemView.setOnClickListener {
            val dataUserIntent = UserItems(
                data.name,
                data.username,
                    data.profile_picture,
                data.followers,
                data.following,
                data.location,
            )
            val mIntent = Intent(it.context, UserDetail::class.java)
            mIntent.putExtra(UserDetail.EXTRA_USERNAME, dataUserIntent)
            it.context.startActivity(mIntent)
        }
    }

    override fun getItemCount(): Int = mData.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = UserItemsBinding.bind(itemView)
        fun bind(userItems: UserItems) {
            with(itemView){
                binding.tvUsername.text = userItems.username
                if(userItems.name == "null") binding.tvNama.text = resources.getString(R.string.null_name)
                else binding.tvNama.text = userItems.name
                Glide.with(binding.imgPp)
                        .load(userItems.profile_picture)
                        .into(binding.imgPp)
            }
        }
    }
}
