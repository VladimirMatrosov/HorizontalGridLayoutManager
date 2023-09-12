package com.food.automativelayoutmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var dataProvider: DataProvider
    private lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataProvider = DataProvider(100)
        adapter = ListAdapter { position ->
            dataProvider.removeItem(position)
            adapter.removeItem(dataProvider.getItems(), position)
        }

        findViewById<RecyclerView>(R.id.list)?.let { listView ->
            listView.layoutManager =
                HorizontalGridLayoutManager(columnCount = 5, rowCount = 2, reverseLayout = true)
            listView.adapter = adapter
            adapter.setItems(dataProvider.getItems())
        }

        findViewById<Button>(R.id.add)?.setOnClickListener {
            dataProvider.addItem()
            adapter.addItem(dataProvider.getItems())
        }
    }
}
