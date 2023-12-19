package com.htnguyen.ihealthclub.view.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.UserSaved

class ProfileUserAdapter(val context: Context, var listUser: List<UserSaved>?, val callback : (UserSaved) -> Unit) :
    RecyclerView.Adapter<ProfileUserAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindItem(user = listUser!![position])
    }

    override fun getItemCount(): Int = listUser?.size!!

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tv_name)
        private val imgAvatar: ImageView = itemView.findViewById(R.id.img_avatar)

        @SuppressLint("SetTextI18n")
        fun bindItem(user: UserSaved) {
            tvName.text = user.userName

            Glide
                .with(context)
                .load(user.photoUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_user_thumbnail)
                .into(imgAvatar);

            itemView.setOnClickListener {
                callback.invoke(user)
            }
            itemView.findViewById<ImageView>(R.id.iv_more).setOnClickListener {
                showDialog()
            }
        }
    }

    private fun showDialog() {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_user_login_more)
        dialog.show()
    }
}

