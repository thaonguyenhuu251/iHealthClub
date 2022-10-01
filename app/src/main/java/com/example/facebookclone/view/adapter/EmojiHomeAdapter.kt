package com.example.facebookclone.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.facebookclone.R
import com.example.facebookclone.model.EmojiHome

class EmojiHomeAdapter(val context: Context, var listEmojis: MutableList<EmojiHome>, val callback : (EmojiHome) -> Unit):
    RecyclerView.Adapter<EmojiHomeAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmojiHomeAdapter.ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_emoji_home, parent, false)
        return ItemViewHolder(view)
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindItem(emoji = listEmojis[position])
    }

    override fun getItemCount(): Int = listEmojis.size


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tv_emojis_home: TextView = itemView.findViewById(R.id.tv_emojis_home)
        private val tv_emojis: TextView = itemView.findViewById(R.id.emoji_custom_text_view)

        fun bindItem(emoji: EmojiHome) {
            tv_emojis_home.text = emoji.emojiName
            tv_emojis.text = emoji.srcImage

            itemView.setOnClickListener {
                callback.invoke(emoji)
            }
        }
    }

    fun filterList(listEmoji: MutableList<EmojiHome>) {
        listEmojis = listEmoji
        notifyDataSetChanged()
    }

}