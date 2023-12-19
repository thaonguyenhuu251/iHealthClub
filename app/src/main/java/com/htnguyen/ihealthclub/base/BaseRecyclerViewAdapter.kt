package com.htnguyen.ihealthclub.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.htnguyen.ihealthclub.model.BaseItem
import com.htnguyen.ihealthclub.BR

open class BaseRecyclerViewAdapter<T> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listItems: List<T> = emptyList()

    var onClickItem: (position: Int, view: View?) -> Unit = { _: Int, _: View? -> }
    var onRemoveItem: (position: Int) -> Unit = { item: Int -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewDataBinding: ViewDataBinding =
            DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        return BaseViewHolder(viewDataBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model: BaseItem = getItems(position) as BaseItem
        val baseViewHolder: BaseViewHolder = holder as BaseViewHolder
        baseViewHolder.bind(model)
        baseViewHolder.viewDataBinding.root.setOnClickListener { p0 -> onClickItem(position, p0) }
    }

    open fun setItems(listItems: List<T>) {
        this.listItems = listItems
        notifyDataSetChanged()
    }

    fun updateItems(listItems: List<T>) {
        this.listItems = emptyList()
        this.listItems = listItems
        notifyDataSetChanged()
    }

    override fun getItemCount() = listItems.size

    override fun getItemViewType(position: Int): Int {
        return getLayoutId(position)
    }

    private fun getLayoutId(position: Int): Int {
        val model: BaseItem = listItems[position] as BaseItem
        return model.layoutResourceId
    }

    open fun getItems(position: Int): T {
        return listItems[position]
    }

    fun getListItems() = listItems

    open fun getListItem(): ArrayList<T> {
        return listItems as ArrayList<T>
    }

    class BaseViewHolder(var viewDataBinding: ViewDataBinding) : RecyclerView.ViewHolder(
        viewDataBinding.root
    ) {
        fun bind(model: BaseItem?) {
            viewDataBinding.setVariable(BR.viewModel, model)
            viewDataBinding.executePendingBindings()
        }
    }

    fun clearData() {
        listItems = emptyList()
        notifyDataSetChanged()
    }
}