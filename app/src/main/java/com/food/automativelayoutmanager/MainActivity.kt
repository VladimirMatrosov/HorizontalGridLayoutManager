package com.food.automativelayoutmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.food.automativelayoutmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var dataProvider: DataProvider
    private lateinit var adapter: ListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataProvider = DataProvider(100)
        adapter = ListAdapter { position ->
            dataProvider.removeItem(position)
            adapter.removeItem(dataProvider.getItems(), position)
        }
        binding.list.layoutManager = HorizontalGridLayoutManager(
            columnCount = 5,
            rowCount = 2,
            reverseLayout = true
        )
        binding.list.adapter = adapter

        adapter.setItems(dataProvider.getItems())

        binding.add.setOnClickListener {
            dataProvider.addItem()
            adapter.addItem(dataProvider.getItems())
        }

        binding.left.setOnClickListener {
            binding.list.smoothScrollToPosition(20)
        }

        binding.right.setOnClickListener {
            binding.list.smoothScrollToPosition(60)
        }
    }
}
