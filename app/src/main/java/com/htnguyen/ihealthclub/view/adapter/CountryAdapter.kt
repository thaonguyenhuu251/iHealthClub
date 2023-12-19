package com.htnguyen.ihealthclub.view.adapter

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.AreaCode
import java.util.*

class CountryAdapter(
    context: Context,
    listAreaCode: List<AreaCode>
) : ArrayAdapter<AreaCode>(context, 0, listAreaCode) {
    val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.item_area_code, parent, false)
        } else {
            view = convertView
        }
        getItem(position)?.let { country ->
            setItemForCountry(view, country)
        }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if (position == 0) {
            view = layoutInflater.inflate(R.layout.item_header_area_code, parent, false)
            view.setOnClickListener {
                val root = parent.rootView
                root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
                root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
            }
            view.findViewById<ImageView>(R.id.ivArrow)
                .animate()
                .rotation(180f)
                .setDuration(200)
                .start()

        } else {
            view = layoutInflater.inflate(R.layout.item_area_code, parent, false)
            view.findViewById<ImageView>(R.id.ivArrow).visibility = View.GONE
            getItem(position)?.let { country ->
                setItemForCountry(view, country)
            }
        }
        return view
    }

    override fun getItem(position: Int): AreaCode? {
        if (position == 0) {
            return null
        }
        return super.getItem(position - 1)
    }

    override fun getCount() = super.getCount() + 1
    override fun isEnabled(position: Int) = position != 0
    private fun setItemForCountry(view: View, country: AreaCode) {
        val tvCountry = view.findViewById<TextView>(R.id.tvCountry)
        val ivCountry = view.findViewById<TextView>(R.id.ivCountry)

        tvCountry.text = country.phone?.get(0) ?: "0"
        ivCountry.text = country.emoji

    }

}