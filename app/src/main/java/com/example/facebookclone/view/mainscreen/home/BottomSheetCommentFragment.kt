package com.example.facebookclone.view.mainscreen.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.facebookclone.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetCommentFragment : BottomSheetDialogFragment() {
    // TODO: Rename and change types of parameters


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        setStyle(STYLE_NORMAL, R.style.SheetDialogStyle)
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
            val sheetInternal: View = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
            BottomSheetBehavior.from(sheetInternal).state = BottomSheetBehavior.STATE_EXPANDED
        }

        val view = inflater.inflate(R.layout.fragment_bottom_sheet_comment, container, false)
        view.setBackgroundResource(R.drawable.rounded_comment)
        view.findViewById<ConstraintLayout> (R.id.cstBottomComment).maxHeight = (resources.displayMetrics.heightPixels * 0.95).toInt()
        return view
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        fun newInstance(): BottomSheetCommentFragment {
            return BottomSheetCommentFragment()
        }
    }
}