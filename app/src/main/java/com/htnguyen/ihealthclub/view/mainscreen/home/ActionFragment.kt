package com.htnguyen.ihealthclub.view.mainscreen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.ActionHome
import com.htnguyen.ihealthclub.view.adapter.ActionHomeAdapter
import kotlinx.android.synthetic.main.fragment_action.*
import java.util.*

class ActionFragment : Fragment() {
    private var actionsAdapter: ActionHomeAdapter? = null
    private val list = mutableListOf<ActionHome>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_action, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    private fun initView(){
        listActions()
        initRecycleView()
    }

    private fun initRecycleView(){
        actionsAdapter = ActionHomeAdapter(requireContext(), listActions = list){ actionsHome ->

        }

        rv_action.layoutManager = GridLayoutManager(requireContext(),2)
        rv_action.setHasFixedSize(true)
        rv_action.adapter = actionsAdapter

    }

    private fun listActions(){
        list.add(ActionHome(actionName = "Congratulating..", srcImage = R.drawable.action_congratulating))
        list.add(ActionHome(actionName = "Drinking...", srcImage = R.drawable.action_drinking))
        list.add(ActionHome(actionName = "Eating...", srcImage = R.drawable.action_eating))
        list.add(ActionHome(actionName = "Going to...", srcImage = R.drawable.action_going_to))
        list.add(ActionHome(actionName = "Listening...", srcImage = R.drawable.action_listening))
        list.add(ActionHome(actionName = "Participating...", srcImage = R.drawable.action_participating))
        list.add(ActionHome(actionName = "Playing...", srcImage = R.drawable.action_playing))
        list.add(ActionHome(actionName = "Reading...", srcImage = R.drawable.action_reading))
        list.add(ActionHome(actionName = "Searching...", srcImage = R.drawable.action_searching))
        list.add(ActionHome(actionName = "Supporting...", srcImage = R.drawable.action_supporting))
        list.add(ActionHome(actionName = "Thinking...", srcImage = R.drawable.action_thinking))
        list.add(ActionHome(actionName = "Watching...", srcImage = R.drawable.action_watching))
    }

    private fun filter(text: String) {
        val filteredList: MutableList<ActionHome> = mutableListOf<ActionHome>()
        for (item in list) {
            if (item.actionName.lowercase().contains(text.lowercase())) {
                filteredList.add(item)
            }
        }
        actionsAdapter?.filterList(filteredList)
    }


    public fun getText(text: String) {
        filter(text)
    }



}