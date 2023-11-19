package com.yourbynn.app_githubusers.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.yourbynn.app_githubusers.R
import com.yourbynn.app_githubusers.data.remote.User
import com.yourbynn.app_githubusers.databinding.ItemUsersBinding
import com.yourbynn.app_githubusers.view.DetailsActivity

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemUsersBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                root.setOnClickListener {
                    onItemClickCallback?.onItemClicked(user)
                }
                tvUsername.text = user.login
                Glide.with(itemView)
                    .load(user.avatarUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(imgAvatar)
            }
        }
    }

    private var list = ArrayList<User>()
    fun updateList(newList: List<User>) {
        val userDiff = UserDiffutils(list, newList)
        val result = DiffUtil.calculateDiff(userDiff)
        this.list.clear()
        this.list.addAll(newList)
        result.dispatchUpdatesTo(this)
    }

    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickCallback{
        fun onItemClicked(data: User)
    }
}
