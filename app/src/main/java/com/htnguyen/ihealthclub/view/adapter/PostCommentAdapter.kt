package com.htnguyen.ihealthclub.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.CommentModel
import com.htnguyen.ihealthclub.model.Post
import com.htnguyen.ihealthclub.model.TypeAction
import com.htnguyen.ihealthclub.model.UserAction
import com.htnguyen.ihealthclub.utils.FirebaseUtils
import com.htnguyen.ihealthclub.utils.Utils
import com.htnguyen.ihealthclub.view.reacbutton.ClubReactions
import com.htnguyen.ihealthclub.view.reacbutton.ReactButton
import com.htnguyen.ihealthclub.view.reacbutton.ReactText
import com.htnguyen.ihealthclub.view.reacbutton.Reaction
import java.text.MessageFormat

class PostCommentAdapter(
    val idUser: String,
    val context: Context,
    var listComment: MutableList<CommentModel>,
    val callback: (CommentModel) -> Unit,
    val feedback: (CommentModel) -> Unit,
    val onActionLike: (reaction: Reaction, comment: CommentModel) -> Unit = { reaction, comment -> },
    val onActionListLike: (CommentModel) -> Unit,
) :
    RecyclerView.Adapter<PostCommentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(commentModel = listComment[position])
    }

    override fun getItemCount(): Int {
        return listComment.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUserName: TextView = itemView.findViewById(R.id.txtUserName)
        private val txtUserNameTo: TextView = itemView.findViewById(R.id.txtUserNameTo)
        private val txtComment: TextView = itemView.findViewById(R.id.txtComment)
        private val txtNumberAction: TextView = itemView.findViewById(R.id.txtNumberAction)
        private val txtTime: TextView = itemView.findViewById(R.id.txtTime)
        private val imgAvatar: ImageView = itemView.findViewById(R.id.imgAvatar)
        private val imageTo: ImageView = itemView.findViewById(R.id.imageTo)
        private val txtFeedback: TextView = itemView.findViewById(R.id.txtFeedback)
        private val rcvComment = itemView.findViewById<RecyclerView>(R.id.rvComment)
        private val reactText: ReactText = itemView.findViewById(R.id.reactText)

        private val imgFirstAction: ImageView = itemView.findViewById(R.id.imgFirstAction)
        private val imgSecondAction: ImageView = itemView.findViewById(R.id.imgSecondAction)
        private val imgThreeAction: ImageView = itemView.findViewById(R.id.imgThreeAction)

        fun bindItem(commentModel: CommentModel) {
            FirebaseUtils.getUserById(
                commentModel.idUser,
                onSuccess = { user ->
                    Glide.with(context).load(user.photoUrl)
                        .error(
                            AppCompatResources.getDrawable(
                                context,
                                R.drawable.ic_user_thumbnail
                            )
                        )
                        .into(imgAvatar)

                    tvUserName.text = user.name
                },
                onFailure = {

                }

            )
            txtComment.text = commentModel.contentAction
            txtTime.text = commentModel.timeAction?.let { Utils.getTimeAgo(it) }

            if (commentModel.typeAction == TypeAction.COMMENT0 && commentModel.listComment.isNotEmpty()) {
                rcvComment.visibility = View.VISIBLE

            } else {
                rcvComment.visibility = View.GONE
            }

            if (commentModel.typeAction == TypeAction.COMMENT1) {
                imageTo.visibility = View.VISIBLE
                imgSecondAction.visibility = View.GONE
                imgThreeAction.visibility = View.GONE
                FirebaseUtils.getUserById(
                    commentModel.feedbackTo,
                    onSuccess = { user ->
                        txtUserNameTo.text = user.name
                    },
                    onFailure = {

                    }

                )
            } else {
                imageTo.visibility = View.GONE
                imgSecondAction.visibility = View.VISIBLE
                imgThreeAction.visibility = View.VISIBLE
            }

            var commentAdapter =
                PostCommentAdapter(
                    idUser = idUser,
                    context = context,
                    listComment = commentModel.listComment,
                    callback = { comment ->

                    },
                    feedback = { comment ->
                        commentModel.idUser = comment.idUser
                        feedback(commentModel)
                    },
                    onActionLike = { reaction, comment ->
                        onActionLike(reaction, comment)
                    },
                    onActionListLike = { comment ->
                        onActionListLike(commentModel)
                    },
                )
            rcvComment.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rcvComment.setHasFixedSize(true)
            rcvComment.isNestedScrollingEnabled = false
            rcvComment.adapter = commentAdapter
            itemView.setOnClickListener {
                callback.invoke(commentModel)
            }

            reactText.setReactions(*ClubReactions.reactions)
            reactText.defaultReaction = ClubReactions.defaultReact
            reactText.setEnableReactionTooltip(true)

            var userAction = UserAction()
            FirebaseUtils.getReactionComment(
                commentModel.idComment.toString(),
                idUser,
                onSuccess = { userReaction ->
                    userAction = userReaction
                    if (reactText.currentReaction != ClubReactions()
                            .getReaction(userReaction.typeAction)
                    )
                        reactText.currentReaction = ClubReactions()
                            .getReaction(userReaction.typeAction)
                },
                onSuccessTotalLike = {
                    txtNumberAction.text = "${it}"
                })

            reactText.setOnReactionChangeListener { reaction ->
                if (reaction != ClubReactions().getReaction(userAction.typeAction) || userAction.idUser.isNullOrEmpty())
                    onActionLike(reaction, commentModel)
            }

            txtNumberAction.setOnClickListener {
                onActionListLike(commentModel)
            }

            txtFeedback.setOnClickListener {
                feedback(commentModel)
            }
        }
    }
}

