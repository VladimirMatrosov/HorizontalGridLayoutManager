package com.food.automativelayoutmanager

import com.food.automativelayoutmanager.entity.Item

class DataProvider(size: Int) {
    private val items = MutableList(size) { index ->
        Item(index + 1)
    }

    fun getItems(): List<Item> {
        return items
    }

    fun addItem() {
        items.add(0, Item(items.size + 1))
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
    }
}
