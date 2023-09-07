package com.food.automativelayoutmanager

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.food.automativelayoutmanager.entity.Item

class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun onBind(item: Item) {
        itemView.findViewById<TextView>(R.id.text).text = item.number.toString()
    }
}
