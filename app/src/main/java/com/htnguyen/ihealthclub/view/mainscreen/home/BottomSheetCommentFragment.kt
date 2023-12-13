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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.CommentModel
import com.htnguyen.ihealthclub.model.ListLike
import com.htnguyen.ihealthclub.model.TypeLike
import com.htnguyen.ihealthclub.utils.SHARED_PREFERENCES_KEY
import com.htnguyen.ihealthclub.utils.URL_PHOTO
import com.htnguyen.ihealthclub.utils.USER_ID
import com.htnguyen.ihealthclub.view.adapter.CommentAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_bottom_sheet_comment.*
import kotlinx.android.synthetic.main.layout_comment_input.*
import kotlinx.android.synthetic.main.layout_comment_input.view.*


class BottomSheetCommentFragment : BottomSheetDialogFragment{
    // TODO: Rename and change types of parameters
    private var commentAdapter: CommentAdapter? = null
    private val listLike = mutableListOf<ListLike>(ListLike("name",TypeLike.LIKE))
    private lateinit var commentModel: CommentModel
    private lateinit var sharedPreferences: SharedPreferences
    private var urlAvartar: String = ""
    private var idPost:Long= 0
    private lateinit var database: DatabaseReference

    constructor(idPost: Long) : super() {
        this.idPost = idPost
    }


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //activity?.window?.setSoftInputMode(WindowManager.LayoutParams.RE)
        sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
        database = Firebase.database.reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*// Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bottom_sheet_comment, container, false)
        // Set max height to half screen
        view.findViewById<ConstraintLayout> (R.id.cstBottomComment).maxHeight = (resources.displayMetrics.heightPixels * 0.95).toInt()
        view.stateDescription =
        return view*/

        dialog?.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val sheetInternal: View =
                bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
            BottomSheetBehavior.from(sheetInternal).state = BottomSheetBehavior.STATE_EXPANDED
        }

        val view = inflater.inflate(R.layout.fragment_bottom_sheet_comment, container, false)
        view.setBackgroundResource(R.drawable.bg_rounded_bottom_comment)
        //view.findViewById<ConstraintLayout> (R.id.cstBottomComment).maxHeight = (resources.displayMetrics.heightPixels * 0.55).toInt()
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
        commentAdapter =
            CommentAdapter(context = requireContext(), listComment = listComment()) { comment ->

            }
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


        lnTest.imgSend.setOnClickListener {

        }

    }



    private fun listComment(): MutableList<CommentModel> {
        val listComment = mutableListOf<CommentModel>()
        listComment.add(CommentModel(12,12, "P12","iop",12,listLike,"tnt"))
        listComment.add(CommentModel(12,12, "P12","iop",12,listLike,"tnt"))
        listComment.add(CommentModel(12,12, "P12","iop",12,listLike,"tnt"))
        listComment.add(CommentModel(12,12, "P12","iop",12,listLike,"tnt"))
        listComment.add(CommentModel(12,12, "P12","iop",12,listLike,"tnt"))
        listComment.add(CommentModel(12,12, "P12","iop",12,listLike,"tnt"))
        listComment.add(CommentModel(12,12, "P12","iop",12,listLike,"tnt"))
        listComment.add(CommentModel(12,12, "P12","iop",12,listLike,"tnt"))
        listComment.add(CommentModel(12,12, "P12","iop",12,listLike,"tnt"))
        listComment.add(CommentModel(12,12, "P12","iop",12,listLike,"tnt"))
        listComment.add(CommentModel(12,12, "P12","iop",12,listLike,"tnt"))
        listComment.add(CommentModel(12,12, "P12","iop",12,listLike,"tnt"))
        return listComment
    }

    private fun createComment() {
        commentModel.idComment = System.currentTimeMillis()
        commentModel.idPost = idPost
        commentModel.idUser =  sharedPreferences.getString(USER_ID, "").toString()
        commentModel.urlAvatar = sharedPreferences.getString(URL_PHOTO,"").toString()
        commentModel.createAt = System.currentTimeMillis()
        commentModel.listLike = listLike
        commentModel.contentComment = etComment.text.toString()
        database.child("comments")
        /*database.child("posts").child(post.idPost.toString()).setValue(post)
            .addOnSuccessListener {

            }.addOnFailureListener { e ->
            }*/
    }

    companion object {
        fun newInstance(idPost: Long): BottomSheetCommentFragment {
            return BottomSheetCommentFragment(idPost)
        }
    }
}