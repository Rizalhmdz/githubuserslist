package com.example.githubuserslist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter internal constructor(private val context: Context) : BaseAdapter(){
    internal var user = arrayListOf<User>()

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        var itemView = view
        if(itemView == null){
            itemView = LayoutInflater.from(context).inflate(R.layout.item_list, viewGroup, false)
        }

        val viewHolder = ViewHolder(itemView as View)

        val user = getItem(position) as User
        viewHolder.bind(user)
        return itemView
    }

    override fun getItem(i: Int): Any {
        return user[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getCount(): Int {
        return user.size
    }

    private inner class ViewHolder internal  constructor(view: View){
        private val tvNama: TextView = view.findViewById(R.id.tv_nama)
        private val tvUsername : TextView = view.findViewById(R.id.tv_username)
        private val imgPP : CircleImageView = view.findViewById(R.id.img_pp)

        internal  fun bind(user: User){
            tvNama.text = user.nama
            tvUsername.text = user.username
            Glide.with(imgPP)
                .load(user.profile_pict)
                .into(imgPP)
        }
    }
}