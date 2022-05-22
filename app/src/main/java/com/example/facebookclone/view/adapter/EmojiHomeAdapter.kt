package com.example.facebookclone.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.facebookclone.R
import com.example.facebookclone.model.EmojiHome
import com.example.facebookclone.model.onItemClickListener

class EmojiHomeAdapter(val context: Context, var listEmojis: MutableList<EmojiHome>, val callback : (EmojiHome) -> Unit):
    RecyclerView.Adapter<EmojiHomeAdapter.ItemViewHolder>() {
    private lateinit var eListener: onItemClickListener

    fun setOnItemClickListener(listener: onItemClickListener){
        eListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmojiHomeAdapter.ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_emoji_home, parent, false)
        return ItemViewHolder(view, eListener)
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindItem(emoji = listEmojis!![position])
    }

    override fun getItemCount(): Int = listEmojis?.size!!

    inner class ItemViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        private val tv_emojis_home: TextView = itemView.findViewById(R.id.tv_emojis_home)
        private val iv_emojis_home: ImageView = itemView.findViewById(R.id.iv_emojis_home)

        @SuppressLint("SetTextI18n", "Range")
        fun bindItem(emoji: EmojiHome) {
            tv_emojis_home.text = emoji.emojiName
            iv_emojis_home.setImageResource(emoji.srcImage)

            itemView.setOnClickListener {
                callback.invoke(emoji)
            }
        }

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }


}