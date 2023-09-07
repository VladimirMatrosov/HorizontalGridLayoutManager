package com.food.automativelayoutmanager

import com.food.automativelayoutmanager.entity.Item

class DataProvider {

    fun getItems(size: Int): List<Item> {
        val items = Array(size) { index ->
            Item(index + 1)
        }

        return items.toList()
    }
}
