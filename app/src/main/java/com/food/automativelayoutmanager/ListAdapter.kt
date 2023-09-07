package com.food.automativelayoutmanager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.food.automativelayoutmanager.entity.Item

class ListAdapter : RecyclerView.Adapter<ListHolder>() {
    private var items: List<Item> = emptyList()

    fun setItems(items: List<Item>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)

        return ListHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        holder.onBind(items[position])
    }
}
