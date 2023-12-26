package com.htnguyen.ihealthclub.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.EmojiSportHome

class PostEmojiAdapter(val context: Context, var listEmojis: MutableList<EmojiSportHome>, val callback : (EmojiSportHome) -> Unit):
    RecyclerView.Adapter<PostEmojiAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostEmojiAdapter.ItemViewHolder {
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

        fun bindItem(emoji: EmojiSportHome) {
            tv_emojis_home.text = emoji.emojiName
            tv_emojis.text = emoji.srcImage

            itemView.setOnClickListener {
                callback.invoke(emoji)
            }
        }
    }

    fun filterList(listEmoji: MutableList<EmojiSportHome>) {
        listEmojis = listEmoji
        notifyDataSetChanged()
    }

}