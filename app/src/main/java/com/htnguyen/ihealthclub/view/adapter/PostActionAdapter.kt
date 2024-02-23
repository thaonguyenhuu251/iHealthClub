package com.htnguyen.ihealthclub.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.UserAction
import com.htnguyen.ihealthclub.utils.FirebaseUtils
import java.text.MessageFormat

class PostActionAdapter(
    val idUser: String,
    val context: Context,
    var listAction: MutableList<UserAction>,
) :  RecyclerView.Adapter<PostActionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post_reaction, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listAction.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(listAction[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img_avatar: ImageView = itemView.findViewById(R.id.img_avatar)
        val tv_name: TextView = itemView.findViewById((R.id.tv_name))
        fun bindItem(userAction: UserAction) {
            userAction.idUser?.let {
                FirebaseUtils.getUserById(
                    it,
                    onSuccess = { user ->
                        Glide.with(context).load(user.photoUrl)
                            .error(
                                AppCompatResources.getDrawable(
                                    context,
                                    R.drawable.ic_user_thumbnail
                                )
                            )
                            .into(img_avatar)

                        tv_name.text = user.name
                    },
                    onFailure = {

                    })
            }
        }

    }

}