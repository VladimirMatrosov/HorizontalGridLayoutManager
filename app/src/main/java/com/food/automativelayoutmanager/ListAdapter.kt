package com.food.automativelayoutmanager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.food.automativelayoutmanager.entity.Item

class ListAdapter(private val onItemDeleted: (position: Int) -> Unit) :
    RecyclerView.Adapter<ListHolder>() {
    private var items: List<Item> = emptyList()

    fun setItems(items: List<Item>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun addItem(items: List<Item>) {
        this.items = items
        notifyItemInserted(0)
    }

    fun removeItem(items: List<Item>, position: Int) {
        this.items = items
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_list, parent, false)

        return ListHolder(itemView, onItemDeleted)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        holder.onBind(items[position])
    }
}
