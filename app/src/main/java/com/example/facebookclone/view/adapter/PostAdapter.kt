package com.example.facebookclone.view.adapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.amrdeveloper.reactbutton.ReactButton
import com.amrdeveloper.reactbutton.Reaction
import com.bumptech.glide.Glide
import com.example.facebookclone.R
import com.example.facebookclone.model.Post
import com.example.facebookclone.model.TypeFile
import com.example.facebookclone.view.mainscreen.reaction.FbReactions
import java.util.*
import java.util.concurrent.TimeUnit


class PostAdapter(
    val idUser: String,
    val context: Context,
    var list: MutableList<Any>,
    val callback: (Post) -> Unit,
    val callback2: (Reaction,Post) -> Unit,

    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
        const val VIEW_TYPE_THREE = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            VIEW_TYPE_ONE -> return ItemViewHolderImage(
                LayoutInflater.from(parent.context).inflate(R.layout.item_post_image, parent, false)
            )
            VIEW_TYPE_TWO -> return ItemViewHolderVideo(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_post_video, parent, false)
            )
            VIEW_TYPE_THREE -> return ItemViewHolderText(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_post_text, parent, false)
            )
        }
        return ItemViewHolderText(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post_text, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when ((list[position] as Post).typeFile) {
            TypeFile.IMAGE -> (holder as PostAdapter.ItemViewHolderImage).bindItem(list[position] as Post)
            TypeFile.VIDEO -> (holder as PostAdapter.ItemViewHolderVideo).bindItem(list[position] as Post)
            TypeFile.OTHER -> (holder as PostAdapter.ItemViewHolderText).bindItem(list[position] as Post)

        }
    }


    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        when ((list[position] as Post).typeFile) {
            TypeFile.IMAGE -> return VIEW_TYPE_ONE
            TypeFile.VIDEO -> return VIEW_TYPE_TWO
            TypeFile.OTHER -> return VIEW_TYPE_THREE

        }
        return VIEW_TYPE_ONE
    }

    inner class ItemViewHolderImage(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @RequiresApi(Build.VERSION_CODES.N)
        fun bindItem(post: Post) {
            Log.d("hunghkp1234", "bindItem: ")
            val atv_post: TextView = itemView.findViewById(R.id.atv_post)
            val et_thinking_pos: TextView = itemView.findViewById((R.id.et_thinking_pos))
            val tv_time_post: TextView = itemView.findViewById((R.id.tv_time_post))
            val tv_emoji_status: TextView = itemView.findViewById((R.id.tv_emoji_status))
            val img_avatar: ImageView = itemView.findViewById(R.id.img_avatar)
            val img_pick: ImageView = itemView.findViewById(R.id.img_pick)

            val tv_total_like: TextView = itemView.findViewById((R.id.tv_total_like))
            val tv_total_comment: TextView = itemView.findViewById((R.id.tv_total_comment))
            val tv_total_share: TextView = itemView.findViewById((R.id.tv_total_share))


            val reactButton: ReactButton = itemView.findViewById(R.id.reactButton)
            val reaction : Reaction = Reaction("Like","Like","",0)
            reactButton.setReactions(*FbReactions.reactions)
            reactButton.defaultReaction = FbReactions.defaultReact
            reactButton.setEnableReactionTooltip(true)


            reactButton.setOnClickListener { callback2.invoke(reaction,post) }
            reactButton.setOnReactionChangeListener{
                Log.d("hunghkp123", "bindItem: ")

                callback2.invoke(it,post)
            }

//            reactButton.setOnReactionChangeListener { reaction ->
//                Log.d("nht", "onReactionChange: " + reaction.reactText)
//                val listLike: MutableList<ListLike> = post.listLike
//                if(reaction.reactText != null){
//                    val checkUser: ListLike? = listLike.stream()
//                        .filter { customer -> idUser == customer.idUser }
//                        .findAny()
//                        .orElse(null)
//
//                    if(checkUser == null){
//                        val newUser = ListLike()
//                        newUser.idUser = idUser
//                        newUser.srcLike = typeReaction(reaction.reactText)
//                        post.likeTotal +=1
//                        listLike.add(newUser)
//                    }
//
//                }
//            }

//            reactButton.setOnReactionDialogStateListener(object : OnReactionDialogStateListener {
//                override fun onDialogOpened() {
////                    Log.d(com.amrdeveloper.reactbuttonlibrary.MainActivity.TAG, "onDialogOpened")
//
//                }
//
//                override fun onDialogDismiss() {
////                    Log.d(com.amrdeveloper.reactbuttonlibrary.MainActivity.TAG, "onDialogDismiss")
//                }
//            })

            Glide.with(context).load(post.urlAvatar).error(AppCompatResources.getDrawable(context, R.drawable.img_profile)).into(img_avatar)
            Glide.with(context).load(post.listFile.get(0)).error(AppCompatResources.getDrawable(context, R.drawable.img_profile)).into(img_pick)
            atv_post.text = post.createBy
            et_thinking_pos.text = post.status
            tv_emoji_status.text = post.emojiStatus
            tv_total_like.text = "${post.likeTotal} Likes"
            tv_total_comment.text = "${post.likeTotal} Comments"
            tv_total_share.text = "${post.shareTotal} Shares"
            val date: Date = Date(post.createAt)

            val dateNow = Date()

            val diff = dateNow.time - date.time //as given


            val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
            val hour = TimeUnit.MILLISECONDS.toHours(diff)
            val day = TimeUnit.MILLISECONDS.toDays(diff)
            var time = ""
            time = if (hour < 1) {
                "$minutes minutes"
            } else {
                if(hour <=24){
                    "$hour hours"
                }else{
                    "$day days"
                }

            }

            tv_time_post.text = time
            itemView.setOnClickListener {
                callback.invoke(post)

            }
            itemView.setBackgroundResource(R.color.white)


        }
    }

    inner class ItemViewHolderVideo(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(post: Post) {
            val atv_post: TextView = itemView.findViewById(R.id.atv_post)
            val et_thinking_pos: TextView = itemView.findViewById((R.id.et_thinking_pos))
            val tv_time_post: TextView = itemView.findViewById((R.id.tv_time_post))
            val tv_emoji_status: TextView = itemView.findViewById((R.id.tv_emoji_status))
            val img_avatar: ImageView = itemView.findViewById(R.id.img_avatar)
            val img_pick: ImageView = itemView.findViewById(R.id.img_pick)
            itemView.setOnClickListener {
                callback.invoke(post)

            }
            itemView.setBackgroundResource(R.color.white)
        }
    }
    inner class ItemViewHolderText(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(post: Post) {
            val atv_post: TextView = itemView.findViewById(R.id.atv_post_text)
            val et_thinking_pos: TextView = itemView.findViewById((R.id.et_thinking_pos_text))
            val tv_time_post: TextView = itemView.findViewById((R.id.tv_time_post_text))
            val tv_emoji_status: TextView = itemView.findViewById((R.id.tv_emoji_status_text))
            val img_avatar: ImageView = itemView.findViewById(R.id.img_avatar_text)
            Glide.with(context).load(post.urlAvatar).error(AppCompatResources.getDrawable(context, R.drawable.img_profile)).into(img_avatar)
            atv_post.text = post.createBy
            et_thinking_pos.text = post.status
            tv_emoji_status.text = post.emojiStatus
            val date: Date = Date(post.createAt)

            val dateNow = Date()

            val diff = dateNow.time - date.time //as given


            val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
            val hour = TimeUnit.MILLISECONDS.toHours(diff)
            var time = ""
            time = if (hour < 1) {
                "$minutes phut"
            } else {
                "$hour gio"
            }
            tv_time_post.text = time
            itemView.setOnClickListener {
                callback.invoke(post)

            }
            itemView.setBackgroundResource(R.color.white)
        }
    }


}