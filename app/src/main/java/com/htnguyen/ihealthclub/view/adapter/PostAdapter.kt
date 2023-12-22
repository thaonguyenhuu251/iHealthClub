package com.htnguyen.ihealthclub.view.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.Post
import com.htnguyen.ihealthclub.model.TypeFile
import com.htnguyen.ihealthclub.utils.FirebaseUtils
import com.htnguyen.ihealthclub.utils.Utils
import com.htnguyen.ihealthclub.view.reacbutton.FbReactions
import com.htnguyen.ihealthclub.view.reacbutton.ReactButton
import com.htnguyen.ihealthclub.view.reacbutton.Reaction
import java.text.MessageFormat
import java.util.*

class PostAdapter(
    val idUser: String,
    val context: Context,
    var list: List<Post>,
    val callback: (Post) -> Unit,
    val onActionLike: (reaction: Reaction, post: Post) -> Unit = {reation, post ->},
    val callback3: (TextView, Post)->Unit

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
        const val VIEW_TYPE_THREE = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
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

        when ((list[position] as Post).typePost) {
            TypeFile.IMAGE -> (holder as PostAdapter.ItemViewHolderImage).bindItem(list[position] as Post)
            TypeFile.VIDEO -> (holder as PostAdapter.ItemViewHolderVideo).bindItem(list[position] as Post)
            TypeFile.OTHER -> (holder as PostAdapter.ItemViewHolderText).bindItem(list[position] as Post)
            else -> {}
        }
    }


    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        when ((list[position] as Post).typePost) {
            TypeFile.IMAGE -> return VIEW_TYPE_ONE
            TypeFile.VIDEO -> return VIEW_TYPE_TWO
            TypeFile.OTHER -> return VIEW_TYPE_THREE

            else -> {}
        }
        return VIEW_TYPE_ONE
    }

    inner class ItemViewHolderImage(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @RequiresApi(Build.VERSION_CODES.N)
        fun bindItem(post: Post) {
            val atv_post: TextView = itemView.findViewById(R.id.atv_post)
            val et_thinking_pos: TextView = itemView.findViewById((R.id.et_thinking_pos))
            val tv_time_post: TextView = itemView.findViewById((R.id.tv_time_post))
            val img_avatar: ImageView = itemView.findViewById(R.id.img_avatar)
            val img_pick: ImageView = itemView.findViewById(R.id.img_pick)

            val tv_total_like: TextView = itemView.findViewById((R.id.tv_total_like))
            val tv_total_comment: TextView = itemView.findViewById((R.id.tv_total_comment))
            val tv_total_share: TextView = itemView.findViewById((R.id.tv_total_share))


            val reactButton: ReactButton = itemView.findViewById(R.id.reactLike)
            for (action in post.listLike) {
                if (action.idUser == idUser) {
                    reactButton.defaultReaction = FbReactions().getReaction(action.typeAction)
                    break
                }
            }
            reactButton.setReactions(*FbReactions.reactions)
            reactButton.setEnableReactionTooltip(true)

            reactButton.setOnReactionChangeListener {
                onActionLike(reactButton.currentReaction, post)
            }

            val txtComment: TextView = itemView.findViewById(R.id.reactComment)
            txtComment.setOnClickListener {
                callback3.invoke(txtComment, post)
            }

            FirebaseUtils.getUserById(
                post.idUser,
                onSuccess = {user ->
                    Glide.with(context).load(user.photoUrl)
                        .error(AppCompatResources.getDrawable(context, R.drawable.ic_user_thumbnail))
                        .into(img_avatar)

                    atv_post.text = MessageFormat.format(
                        context.getString(R.string.post_name_status),
                        user.name,
                        post.emojiStatus
                    )
                },
                onFailure = {

                }

            )

            Glide.with(context).load(post.listFile.get(0))
                .error(AppCompatResources.getDrawable(context, R.drawable.ic_user_thumbnail))
                .into(img_pick)

            et_thinking_pos.text = post.bodyStatus
            tv_total_like.text = "${post.likeTotal}"
            tv_total_comment.text = MessageFormat.format(
                context.getString(R.string.post_comment_total),
                post.commentTotal
            )
            tv_total_share.text = MessageFormat.format(
                context.getString(R.string.post_share_total),
                post.shareTotal
            )
            tv_time_post.text = Utils.getTimeAgo(post.createAt)
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
            val atv_post: TextView = itemView.findViewById(R.id.atv_post)
            val et_thinking_pos: TextView = itemView.findViewById((R.id.et_thinking_pos))
            val tv_time_post: TextView = itemView.findViewById((R.id.tv_time_post))
            val img_avatar: ImageView = itemView.findViewById(R.id.img_avatar)
            val img_pick: ImageView = itemView.findViewById(R.id.img_pick)

            val tv_total_like: TextView = itemView.findViewById((R.id.tv_total_like))
            val tv_total_comment: TextView = itemView.findViewById((R.id.tv_total_comment))
            val tv_total_share: TextView = itemView.findViewById((R.id.tv_total_share))

            val reactButton: ReactButton = itemView.findViewById(R.id.reactLike)
            reactButton.setReactions(*FbReactions.reactions)
            reactButton.defaultReaction = FbReactions.defaultReact
            reactButton.setEnableReactionTooltip(true)

            reactButton.setOnReactionChangeListener {
                onActionLike(reactButton.currentReaction, post)
            }

            val txtComment: TextView = itemView.findViewById(R.id.reactComment)
            txtComment.setOnClickListener {
                callback3.invoke(txtComment, post)
            }

            Glide.with(context).load(post.listFile.get(0))
                .error(AppCompatResources.getDrawable(context, R.drawable.ic_user_thumbnail))
                .into(img_pick)
            FirebaseUtils.getUserById(
                post.idUser,
                onSuccess = {user ->
                    Glide.with(context).load(user.photoUrl)
                        .error(AppCompatResources.getDrawable(context, R.drawable.ic_user_thumbnail))
                        .into(img_avatar)

                    atv_post.text = MessageFormat.format(
                        context.getString(R.string.post_name_status),
                        user.name,
                        post.emojiStatus
                    )
                },
                onFailure = {

                }

            )
            et_thinking_pos.text = post.bodyStatus
            tv_total_like.text = "${post.likeTotal}"
            tv_total_comment.text = MessageFormat.format(
                context.getString(R.string.post_comment_total),
                post.commentTotal
            )
            tv_total_share.text = MessageFormat.format(
                context.getString(R.string.post_share_total),
                post.shareTotal
            )
            tv_time_post.text = Utils.getTimeAgo(post.createAt)
            itemView.setOnClickListener {
                callback.invoke(post)

            }
            itemView.setBackgroundResource(R.color.white)
        }
    }


}