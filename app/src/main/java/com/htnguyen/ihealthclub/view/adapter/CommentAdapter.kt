package com.htnguyen.ihealthclub.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.CommentModel
import com.htnguyen.ihealthclub.view.reacbutton.FbReactions
import com.htnguyen.ihealthclub.view.reacbutton.ReactButton

class CommentAdapter (val context: Context, var listComment: MutableList<CommentModel>, val callback : (CommentModel) -> Unit)  :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false)
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
        fun bindItem(commentModel: CommentModel) {
            //tvUserName.text  = commentModel
            itemView.setOnClickListener {
                callback.invoke(commentModel)
            }
            val reactButton: ReactButton = itemView.findViewById<ReactButton>(R.id.reactButton)
            reactButton.setReactions(*FbReactions.reactions)
            reactButton.defaultReaction = FbReactions.defaultReact
            reactButton.setEnableReactionTooltip(true)

            reactButton.setOnReactionChangeListener { reaction ->

            }

            reactButton.setOnReactionDialogStateListener(object :
                ReactButton.OnReactionDialogStateListener {
                override fun onDialogOpened() {

                }

                override fun onDialogDismiss() {

                }
            })
        }
    }
}

