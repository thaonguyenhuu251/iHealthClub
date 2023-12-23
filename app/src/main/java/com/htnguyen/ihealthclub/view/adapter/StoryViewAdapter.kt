package com.htnguyen.ihealthclub.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.ObjectStory
import com.htnguyen.ihealthclub.model.Post
import com.htnguyen.ihealthclub.utils.FirebaseUtils

import com.htnguyen.ihealthclub.view.mainscreen.home.PickImageStoryActivity

class StoryViewAdapter(
    val url: String,
    val context: Context,
    val options: FirebaseRecyclerOptions<ObjectStory>,
    val callback: (ObjectStory, String) -> Unit
) : FirebaseRecyclerAdapter<ObjectStory, RecyclerView.ViewHolder>(options) {
    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ONE) {
            return CreateNewViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_edit_story, parent, false)
            )
        }
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_story_watch, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, story: ObjectStory) {
        if (position != 0) {
            (holder as ItemViewHolder).bindItem(getItem(position))
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) {
            (holder as CreateNewViewHolder).bindItem()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_ONE
        } else {
            VIEW_TYPE_TWO
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    inner class CreateNewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iv_story_add: ImageView = itemView.findViewById(R.id.iv_add_story)

        fun bindItem() {
            Glide.with(context).load(url)
                .error(AppCompatResources.getDrawable(context, R.drawable.ic_user_thumbnail))
                .into(iv_story_add)
            itemView.setOnClickListener {
                val intent = Intent(context, PickImageStoryActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iv_avartar_story: ImageView = itemView.findViewById(R.id.iv_avatar_story)
        private val tv_name_story: TextView = itemView.findViewById((R.id.tv_name_story))
        private val iv_content_story: ImageView = itemView.findViewById(R.id.iv_content_story)
        fun bindItem(story: ObjectStory) {
            FirebaseUtils.getUserById(story.idUser, onSuccess = {user ->
                Glide.with(context).load(user.photoUrl)
                    .error(AppCompatResources.getDrawable(context, R.drawable.ic_user_thumbnail))
                    .into(iv_avartar_story)
                tv_name_story.text = user.name
            })
            Glide.with(context).load(story.listFile.get(0).url)
                .error(AppCompatResources.getDrawable(context, R.drawable.ic_user_thumbnail))
                .into(iv_content_story)
            itemView.setOnClickListener {
                callback.invoke(story, tv_name_story.text.toString())
            }
        }
    }
}
