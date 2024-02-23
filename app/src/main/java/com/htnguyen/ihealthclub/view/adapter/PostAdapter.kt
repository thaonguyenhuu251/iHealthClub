package com.htnguyen.ihealthclub.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.Post
import com.htnguyen.ihealthclub.model.TypeFile
import com.htnguyen.ihealthclub.model.UserAction
import com.htnguyen.ihealthclub.utils.FirebaseUtils
import com.htnguyen.ihealthclub.utils.Utils
import com.htnguyen.ihealthclub.view.reacbutton.ClubReactions
import com.htnguyen.ihealthclub.view.reacbutton.ReactButton
import com.htnguyen.ihealthclub.view.reacbutton.Reaction
import java.text.MessageFormat

class PostAdapter(
    val idUser: String,
    val context: Context,
    val callback: (Post) -> Unit,
    val onActionLike: (reaction: Reaction, post: Post) -> Unit = { reation, post -> },
    val onActionComment: (TextView, Post) -> Unit,
    val onActionListLike: (TextView, Post) -> Unit,
    val options: FirebaseRecyclerOptions<Post>
) : FirebaseRecyclerAdapter<Post, RecyclerView.ViewHolder>(options) {
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, post: Post) {
        when (getItem(position).typePost) {
            TypeFile.IMAGE -> (holder as PostAdapter.ItemViewHolderImage).bindItem(getItem(position))
            TypeFile.VIDEO -> (holder as PostAdapter.ItemViewHolderVideo).bindItem(getItem(position))
            TypeFile.OTHER -> (holder as PostAdapter.ItemViewHolderText).bindItem(getItem(position))
            else -> {}
        }
    }

    override fun getItemViewType(position: Int): Int {
        when (getItem(position).typePost) {
            TypeFile.IMAGE -> return VIEW_TYPE_ONE
            TypeFile.VIDEO -> return VIEW_TYPE_TWO
            TypeFile.OTHER -> return VIEW_TYPE_THREE

            else -> {}
        }
        return VIEW_TYPE_ONE
    }

    inner class ItemViewHolderImage(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
            reactButton.defaultReaction = ClubReactions.defaultReact
            reactButton.setReactions(*ClubReactions.reactions)
            reactButton.setEnableReactionTooltip(true)

            val txtComment: TextView = itemView.findViewById(R.id.reactComment)
            txtComment.setOnClickListener {
                onActionComment.invoke(txtComment, post)
            }
            tv_total_comment.setOnClickListener {
                onActionComment.invoke(txtComment, post)
            }


            tv_total_like.setOnClickListener {
                onActionListLike.invoke(txtComment, post)
            }

            FirebaseUtils.getUserById(
                post.idUser,
                onSuccess = { user ->
                    Glide.with(context).load(user.photoUrl)
                        .error(
                            AppCompatResources.getDrawable(
                                context,
                                R.drawable.ic_user_thumbnail
                            )
                        )
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
            var userAction = UserAction()
            FirebaseUtils.getReactionPost(post.idPost, idUser, onSuccess = { userReaction ->
                userAction = userReaction
                if (reactButton.currentReaction != ClubReactions()
                        .getReaction(userReaction.typeAction))
                    reactButton.currentReaction = ClubReactions()
                        .getReaction(userReaction.typeAction)
            }, onSuccessTotalLike = {
                tv_total_like.text = "${it}"
            })


            reactButton.setOnReactionChangeListener {
                if (it != ClubReactions()
                        .getReaction(userAction.typeAction) || userAction.idUser.isNullOrEmpty()) onActionLike(
                    it,
                    post
                )
            }

            Glide.with(context).load(post.listFile.get(0))
                .error(AppCompatResources.getDrawable(context, R.drawable.ic_user_thumbnail))
                .into(img_pick)

            et_thinking_pos.text = post.bodyStatus

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

            val tv_total_like: TextView = itemView.findViewById((R.id.tv_total_like))
            val tv_total_comment: TextView = itemView.findViewById((R.id.tv_total_comment))
            val tv_total_share: TextView = itemView.findViewById((R.id.tv_total_share))

            val reactButton: ReactButton = itemView.findViewById(R.id.reactLike)
            reactButton.setReactions(*ClubReactions.reactions)
            reactButton.defaultReaction = ClubReactions.defaultReact
            reactButton.setEnableReactionTooltip(true)

            reactButton.setOnReactionChangeListener {
                onActionLike(reactButton.currentReaction, post)
            }

            val txtComment: TextView = itemView.findViewById(R.id.reactComment)
            txtComment.setOnClickListener {
                onActionComment.invoke(txtComment, post)
            }

            FirebaseUtils.getUserById(
                post.idUser,
                onSuccess = { user ->
                    Glide.with(context).load(user.photoUrl)
                        .error(
                            AppCompatResources.getDrawable(
                                context,
                                R.drawable.ic_user_thumbnail
                            )
                        )
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
            var userAction = UserAction()
            FirebaseUtils.getReactionPost(post.idPost, idUser, onSuccess = { userReaction ->
                userAction = userReaction
                if (reactButton.currentReaction != ClubReactions()
                        .getReaction(userReaction.typeAction))
                    reactButton.currentReaction = ClubReactions()
                        .getReaction(userReaction.typeAction)
            }, onSuccessTotalLike = {
                tv_total_like.text = "${it}"
            })


            reactButton.setOnReactionChangeListener {
                if (it != ClubReactions()
                        .getReaction(userAction.typeAction) || userAction.idUser.isNullOrEmpty()) onActionLike(
                    it,
                    post
                )
            }

            et_thinking_pos.text = post.bodyStatus
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