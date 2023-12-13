package com.htnguyen.ihealthclub.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.EmojiHome
import java.util.ArrayList

class EmojiGeneralAdapter(val context: Context, var list: ArrayList<EmojiHome>, val callback: (EmojiHome) -> Unit):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == StoryViewAdapter.VIEW_TYPE_ONE) {
            return CreateNewViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_comment_story, parent, false)
            )
        }
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_emoji_general, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return if (position == 0){
            VIEW_TYPE_ONE
        }else{
            VIEW_TYPE_TWO
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (position == 0){
            (holder as EmojiGeneralAdapter.CreateNewViewHolder).bindItem()

        }else{
            (holder as EmojiGeneralAdapter.ItemViewHolder).bindItem(list[position] as EmojiHome)
        }
    }


    inner class CreateNewViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        fun bindItem() {
            itemView.setOnClickListener {
            }
            itemView.setBackgroundResource(R.color.general_miro_white)
        }
    }

    inner class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val tv_emojis: TextView = itemView.findViewById(R.id.emoji_general)
        fun bindItem(emoji: EmojiHome) {
            tv_emojis.text = emoji.srcImage
            itemView.setOnClickListener {

                callback.invoke(emoji)

            }
            itemView.setBackgroundResource(R.color.general_miro_white)
        }
    }
}