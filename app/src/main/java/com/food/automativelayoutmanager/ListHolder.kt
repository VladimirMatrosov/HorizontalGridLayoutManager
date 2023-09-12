package com.food.automativelayoutmanager

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.food.automativelayoutmanager.entity.Item

class ListHolder(itemView: View, onItemClick: (position: Int) -> Unit) :
    RecyclerView.ViewHolder(itemView) {
    init {
        itemView.setOnClickListener { onItemClick(adapterPosition) }
    }

    fun onBind(item: Item) {
        itemView.findViewById<TextView>(R.id.text).text = item.number.toString()
    }
}
