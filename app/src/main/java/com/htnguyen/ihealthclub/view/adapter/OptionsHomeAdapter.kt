package com.htnguyen.ihealthclub.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.OptionsHome


class OptionsHomeAdapter(val context: Context, var listOptions: MutableList<OptionsHome>, val callback : (OptionsHome) -> Unit):
    RecyclerView.Adapter<OptionsHomeAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OptionsHomeAdapter.ItemViewHolder {
        val view =LayoutInflater.from(context).inflate(R.layout.item_options_home, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindItem(option = listOptions!![position])
    }

    override fun getItemCount(): Int = listOptions?.size!!

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tv_options_home: TextView = itemView.findViewById(R.id.tv_options_home)
        private val iv_options_home: ImageView = itemView.findViewById(R.id.iv_options_home)
        private val ln_background : LinearLayout = itemView.findViewById(R.id.ln_item_options)

        fun bindItem(option: OptionsHome) {
            tv_options_home.text = option.optionName
            tv_options_home.setTextColor(getColor(context, option.textColor))
            ln_background.setBackgroundResource(option.backgroundColor)

            itemView.setOnClickListener {
                callback.invoke(option)
            }
        }
    }

}