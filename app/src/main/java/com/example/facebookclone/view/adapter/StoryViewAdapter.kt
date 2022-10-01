package com.example.facebookclone.view.adapter
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
import com.example.facebookclone.R
import com.example.facebookclone.model.ObjectStory

import com.example.facebookclone.view.mainscreen.home.PickImageStoryActivity

class StoryViewAdapter(val url : String, val context: Context, var list: MutableList<Any>, val callback : (ObjectStory) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (position == 0){
            (holder as CreateNewViewHolder).bindItem()

        }else{
            (holder as ItemViewHolder).bindItem(list[position] as ObjectStory)
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return if (position == 0){
            VIEW_TYPE_ONE
        }else{
            VIEW_TYPE_TWO
        }
    }


    inner class CreateNewViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val iv_story_add: ImageView = itemView.findViewById(R.id.iv_add_story)

        fun bindItem() {
            Glide.with(context).load(url).error(AppCompatResources.getDrawable(context, R.drawable.img_profile)).into(iv_story_add)
            itemView.setOnClickListener {
                val intent = Intent(context,PickImageStoryActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    inner class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val iv_avartar_story: ImageView = itemView.findViewById(R.id.iv_avatar_story)
        private val tv_name_story: TextView = itemView.findViewById((R.id.tv_name_story))
        private val iv_content_story: ImageView = itemView.findViewById(R.id.iv_content_story)
        fun bindItem(story: ObjectStory) {
            Glide.with(context).load(story.urlAvatar).error(AppCompatResources.getDrawable(context, R.drawable.img_profile)).into(iv_avartar_story)
            tv_name_story.text = story.createBy
            Glide.with(context).load(story.listFile.get(0).url).error(AppCompatResources.getDrawable(context, R.drawable.avatar)).into(iv_content_story)
            itemView.setOnClickListener {
                callback.invoke(story)

            }
        }
    }
}
