package com.example.facebookclone.view.mainscreen.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.facebookclone.R
import com.example.facebookclone.model.ActionHome
import com.example.facebookclone.view.adapter.ActionHomeAdapter
import kotlinx.android.synthetic.main.fragment_action.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ActionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ActionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var actionsAdapter: ActionHomeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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
        initRecycleView()
    }

    private fun initRecycleView(){
        actionsAdapter = ActionHomeAdapter(requireContext(), listActions = listActions()){ actionsHome ->

        }

        rv_action.layoutManager = GridLayoutManager(requireContext(),2)
        rv_action.setHasFixedSize(true)
        rv_action.adapter = actionsAdapter

    }

    private fun listActions() :  MutableList<ActionHome>{
        val list = mutableListOf<ActionHome>()
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
        return list
    }

}