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
import com.example.consumeapp.entity.FavoriteItems
import com.example.consumeapp.entity.UserItems
import java.util.*


class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    var listFavoriteUser = ArrayList<FavoriteItems>()
            set(listFavoriteUser) {
            if (listFavoriteUser.size > 0) {
                this.listFavoriteUser.clear()
            }
            this.listFavoriteUser.addAll(listFavoriteUser)

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_items, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavoriteUser[position])
        val data = listFavoriteUser[position]
        holder.itemView.setOnClickListener {
            val dataUserIntent = UserItems(
                data.name,
                data.username,
                data.profile_picture,
                data.followers,
                data.following,
                data.location
            )
            val mIntent = Intent(it.context, UserDetail::class.java)
            mIntent.putExtra(UserDetail.EXTRA_USERNAME, dataUserIntent)
            it.context.startActivity(mIntent)
        }
    }


    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = UserItemsBinding.bind(itemView)
        fun bind(favoriteItems: FavoriteItems) {
            with(itemView){
            binding.tvUsername.text = favoriteItems.username
                if(favoriteItems.name == "null") binding.tvNama.text = resources.getString(R.string.null_name)
                else binding.tvNama.text = favoriteItems.name
                Glide.with(binding.imgPp)
                    .load(favoriteItems.profile_picture)
                    .into(binding.imgPp)
            }
        }

    }

    override fun getItemCount(): Int = this.listFavoriteUser.size

}