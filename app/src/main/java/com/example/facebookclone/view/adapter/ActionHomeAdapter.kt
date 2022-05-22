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
import com.example.facebookclone.model.ActionHome

class ActionHomeAdapter (val context: Context, var listActions: MutableList<ActionHome>, val callback : (ActionHome) -> Unit):
    RecyclerView.Adapter<ActionHomeAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActionHomeAdapter.ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_action_home, parent, false)
        return ItemViewHolder(view)
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindItem(action = listActions!![position])
    }

    override fun getItemCount(): Int = listActions?.size!!

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tv_actions_home: TextView = itemView.findViewById(R.id.tv_actions_home)
        private val iv_actions_home: ImageView = itemView.findViewById(R.id.iv_actions_home)

        @SuppressLint("SetTextI18n", "Range")
        fun bindItem(action: ActionHome) {
            tv_actions_home.text = action.actionName
            iv_actions_home.setImageResource(action.srcImage)

            itemView.setOnClickListener {
                callback.invoke(action)
            }
        }
    }
}