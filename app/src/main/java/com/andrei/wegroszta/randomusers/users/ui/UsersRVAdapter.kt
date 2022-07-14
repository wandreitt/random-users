package com.andrei.wegroszta.randomusers.users.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andrei.wegroszta.randomusers.R
import com.andrei.wegroszta.randomusers.databinding.ItemUserBinding
import com.bumptech.glide.Glide

class UsersRVAdapter : ListAdapter<UserItemUIState, UsersRVAdapter.UserViewHolder>(
    UsersDiffCallback
) {
    private object UsersDiffCallback : DiffUtil.ItemCallback<UserItemUIState>() {
        override fun areItemsTheSame(oldItem: UserItemUIState, newItem: UserItemUIState): Boolean {
            return oldItem.username == newItem.username
        }

        override fun areContentsTheSame(
            oldItem: UserItemUIState,
            newItem: UserItemUIState
        ): Boolean {
            return oldItem == newItem
        }
    }

    class UserViewHolder(
        val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(uiUser: UserItemUIState) = with(binding) {
            tvName.text = uiUser.fullName
            tvDetails.text = root.context.getString(
                R.string.user_item_description_template,
                uiUser.age,
                uiUser.nationality
            )
            tvUserTime.text = uiUser.userTime

            ivAttachment.isVisible = uiUser.hasAttachment

            Glide.with(root.context)
                .load(uiUser.pictureThumbnailUrl)
                .placeholder(R.drawable.iv_person)
                .error(R.drawable.iv_person)
                .circleCrop()
                .into(binding.ivPicture)

            val favRes = if (uiUser.isFavourite)
                R.drawable.ic_star_full
            else
                R.drawable.ic_star_empty

            ivFavourite.setImageResource(favRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)

        holder.binding.ivFavourite.setOnClickListener {
            item.changeFavouriteStatus(item)
        }
    }
}
