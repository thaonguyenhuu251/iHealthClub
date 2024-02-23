package com.htnguyen.ihealthclub.view.mainscreen.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.CommentModel
import com.htnguyen.ihealthclub.model.UserAction
import com.htnguyen.ihealthclub.model.TypeAction
import com.htnguyen.ihealthclub.utils.SHARED_PREFERENCES_KEY
import com.htnguyen.ihealthclub.utils.USER_ID
import com.htnguyen.ihealthclub.view.adapter.PostCommentAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.htnguyen.ihealthclub.databinding.FragmentBottomSheetCommentBinding
import com.htnguyen.ihealthclub.utils.FirebaseUtils
import kotlinx.android.synthetic.main.fragment_bottom_sheet_comment.*
import kotlinx.android.synthetic.main.layout_comment_input.*
import kotlinx.android.synthetic.main.layout_comment_input.view.*


class BottomSheetCommentFragment : BottomSheetDialogFragment() {
    private var idUser: String = ""
    private var commentAdapter: PostCommentAdapter? = null
    private val userActions = mutableListOf<UserAction>()
    private lateinit var commentModel: CommentModel
    private lateinit var commentFeedback: CommentModel
    private lateinit var sharedPreferences: SharedPreferences
    private var idComment: String = ""
    private lateinit var database: DatabaseReference
    private lateinit var binding: FragmentBottomSheetCommentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences =
            requireContext().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        idUser = sharedPreferences.getString(USER_ID, "USER_ID").toString()
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
        database = Firebase.database.reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val sheetInternal: View =
                bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
            BottomSheetBehavior.from(sheetInternal).state = BottomSheetBehavior.STATE_EXPANDED
        }

        val view = inflater.inflate(R.layout.fragment_bottom_sheet_comment, container, false)
        view.setBackgroundResource(R.drawable.rounded_top_background_white)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            idComment = arguments!!.getString("IDPOST", "")
        }
        /*val bottomSheet = view.findViewById<RelativeLayout>(R.id.bottom_sheet)
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = (resources.displayMetrics.heightPixels * 0.7).toInt()
        bottomSheet.layoutParams = layoutParams

        // Tạo BottomSheetBehavior và thiết lập cho bottom sheet
        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        behavior.peekHeight = (resources.displayMetrics.heightPixels * 0.7).toInt()

        // Bắt sự kiện kéo lên để full màn hình
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.peekHeight = resources.displayMetrics.heightPixels
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })*/

        initView(view)
    }

    private fun initView(view: View) {
        commentAdapter =
            PostCommentAdapter(
                idUser = idUser,
                context = requireContext(),
                listComment = listComment(),
                callback = { comment ->

                },
                feedback = { comment ->
                    commentModel = comment
                    commentFeedback = CommentModel()
                    commentFeedback.feedbackTo = comment.idUser
                    commentFeedback.typeAction = TypeAction.COMMENT1
                    etComment.requestFocus()
                },
                onActionLike = { reaction, comment ->
                    val userReactionLike = UserAction(
                        idUser = idUser,
                        typeAction = reaction.reactTypeAction,
                        timeAction = System.currentTimeMillis(),
                        contentAction = ""
                    )
                    if (userReactionLike.typeAction != TypeAction.NO)
                        FirebaseUtils.databaseCommentLike.child(comment.idComment.toString())
                            .child(userReactionLike.idUser.toString())
                            .setValue(userReactionLike)
                    else
                        FirebaseUtils.databaseCommentLike.child(comment.idComment.toString())
                            .child(userReactionLike.idUser.toString())
                            .setValue(null)
                },
                onActionListLike = { comment ->
                    FirebaseUtils.getCommentLikeList(
                        idComment = comment.idComment.toString(),
                        idUser = idUser,
                        onSuccessListLike = {
                            BottomSheetActionFragment.newInstance(it).show(requireActivity().supportFragmentManager, "")
                        }
                    )
                }
            )
        val rcvComment = view.findViewById<RecyclerView>(R.id.rvComment)
        rcvComment.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rcvComment.setHasFixedSize(true)
        rcvComment.isNestedScrollingEnabled = false
        rcvComment.adapter = commentAdapter

        view.findViewById<EditText>(R.id.etComment).addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (etComment.text.toString().trim().isNotEmpty()) {
                    imgEmoji.visibility = View.GONE
                    imgSticker.visibility = View.GONE
                    imgGif.visibility = View.GONE
                    imgSend.visibility = View.VISIBLE
                } else {
                    imgEmoji.visibility = View.VISIBLE
                    imgSticker.visibility = View.VISIBLE
                    imgGif.visibility = View.VISIBLE
                    imgSend.visibility = View.GONE
                }
            }
        })

        commentModel = CommentModel()
        commentFeedback = CommentModel()
        lnTest.imgSend.setOnClickListener {
            createComment(commentModel)
        }

    }


    private fun listComment(): MutableList<CommentModel> {
        val listComment = mutableListOf<CommentModel>()
        FirebaseUtils.databasePostComment.child(idComment).get().addOnSuccessListener {
            for (postSnapshot in it.children) {
                val record = postSnapshot.getValue<CommentModel>()
                if (record != null) {
                    listComment.add(record)

                }
            }
            commentAdapter?.listComment = listComment
            commentAdapter?.notifyDataSetChanged()
        }.addOnFailureListener {

        }
        return listComment
    }

    private fun createComment(commentModel: CommentModel) {
        if (commentFeedback.typeAction == TypeAction.COMMENT0 || commentFeedback.typeAction == TypeAction.NO) {
            commentModel.idComment = "Comment${System.currentTimeMillis()}"
            commentModel.idUser = sharedPreferences.getString(USER_ID, "").toString()
            commentModel.timeAction = System.currentTimeMillis()
            commentModel.typeAction = TypeAction.COMMENT0
            commentModel.contentAction = etComment.text.toString()
            commentModel.userAction = userActions
            commentModel.listComment = mutableListOf()

            FirebaseUtils.databasePostComment.child(idComment)
                .child("Comment${commentModel.timeAction}").setValue(commentModel)
                .addOnSuccessListener {
                    etComment.text.clear()
                    FirebaseUtils.databasePost.child(idComment).child("commentTotal").get().addOnSuccessListener {
                        var totalComment = it.getValue<Long>()?.plus(1)
                        FirebaseUtils.databasePost.child(idComment).child("commentTotal").setValue(totalComment)
                    }
                }.addOnFailureListener {

                }
        } else {
            commentFeedback.idComment = "Comment${System.currentTimeMillis()}"
            commentFeedback.idUser = sharedPreferences.getString(USER_ID, "").toString()
            commentFeedback.timeAction = System.currentTimeMillis()
            commentFeedback.typeAction = TypeAction.COMMENT1
            commentFeedback.contentAction = etComment.text.toString()
            commentFeedback.userAction = userActions
            val listFeedback = commentModel.listComment
            listFeedback.add(commentFeedback)

            FirebaseUtils.databasePostComment.child(idComment)
                .child("Comment${commentModel.timeAction}").child("listComment")
                .setValue(listFeedback)
                .addOnSuccessListener {
                    etComment.text.clear()
                    FirebaseUtils.databasePost.child(idComment).child("commentTotal").get().addOnSuccessListener {
                        var totalComment = it.getValue<Long>()?.plus(1)
                        FirebaseUtils.databasePost.child(idComment).child("commentTotal").setValue(totalComment)
                    }
                }.addOnFailureListener {

                }
        }

    }

    companion object {
        fun newInstance(idPost: String): BottomSheetCommentFragment {
            val bottomSheetCommentFragment = BottomSheetCommentFragment()
            val bundle = Bundle()
            bundle.putString("IDPOST", idPost)
            bottomSheetCommentFragment.arguments = bundle
            return bottomSheetCommentFragment
        }
    }
}