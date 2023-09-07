package com.food.automativelayoutmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var dataProvider: DataProvider
    private lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataProvider = DataProvider()
        adapter = ListAdapter()

        findViewById<RecyclerView>(R.id.list)?.let { listView ->
            val layoutManager = LinearLayoutManager(listView.context)

            listView.layoutManager = layoutManager
            listView.adapter = adapter
            adapter.setItems(dataProvider.getItems(100))
        }
    }
}