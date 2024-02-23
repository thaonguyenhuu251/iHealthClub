package com.htnguyen.ihealthclub.view.mainscreen.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.CommentModel
import com.htnguyen.ihealthclub.model.UserAction
import com.htnguyen.ihealthclub.utils.SHARED_PREFERENCES_KEY
import com.htnguyen.ihealthclub.utils.USER_ID
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.htnguyen.ihealthclub.view.adapter.PostActionAdapter
import kotlinx.android.synthetic.main.fragment_bottom_sheet_action_like.*


class BottomSheetActionFragment : BottomSheetDialogFragment() {
    private var idUser: String = ""
    private var userActions = mutableListOf<UserAction>()
    private lateinit var commentModel: CommentModel
    private lateinit var commentFeedback: CommentModel
    private lateinit var sharedPreferences: SharedPreferences
    private var idComment: String = ""
    private lateinit var database: DatabaseReference
    private var postActionAdapter: PostActionAdapter? = null

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

        val view = inflater.inflate(R.layout.fragment_bottom_sheet_action_like, container, false)
        view.setBackgroundResource(R.drawable.rounded_top_background_white)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            userActions = arguments!!.getSerializable("IDACTION") as ArrayList<UserAction>
        }

        initView(view)
    }

    private fun initView(view: View) {
        postActionAdapter = PostActionAdapter(idUser, requireContext(), userActions)
        rvAction.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvAction.setHasFixedSize(true)
        rvAction.isNestedScrollingEnabled = false
        rvAction.adapter = postActionAdapter

    }

    companion object {
        fun newInstance(listAction: List<UserAction>): BottomSheetActionFragment {
            val bottomSheetCommentFragment = BottomSheetActionFragment()
            val bundle = Bundle()
            bundle.putSerializable("IDACTION", ArrayList(listAction))
            bottomSheetCommentFragment.arguments = bundle
            return bottomSheetCommentFragment
        }
    }
}