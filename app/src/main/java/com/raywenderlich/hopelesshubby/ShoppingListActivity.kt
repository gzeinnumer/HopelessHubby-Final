package com.raywenderlich.hopelesshubby

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_shopping_lists.*
import kotlinx.android.synthetic.main.content_shopping_lists.*

class ShoppingListActivity : AppCompatActivity() {

  private lateinit var shoppingListAdapter: ShoppingListAdapter
  private lateinit var viewModel: ShoppingListViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_shopping_lists)
    setSupportActionBar(toolbar)

    val layoutManager = LinearLayoutManager(this)
    recyclerView.layoutManager = layoutManager

    viewModel = ViewModelProviders.of(this, SavedStateViewModelFactory(this)).get(
        ShoppingListViewModel::class.java)

    btAddToList.setOnClickListener {
      val itemName = etItemName.text.toString()
      if (itemName.isNotEmpty()) {
        val position = viewModel.items.value?.size
        viewModel.addItemToShoppingList(itemName)
        shoppingListAdapter.notifyItemInserted(position ?: 0)
      }
    }

  }

  override fun onResume() {
    super.onResume()
    loadShoppingLists()
  }

  /**
   * Loads the items in the shopping list and displays them in the recycler view.
   */
  private fun loadShoppingLists() {
    viewModel.loadShoppingList()

    viewModel.items.observe(this, Observer<ArrayList<String>> {
      shoppingListAdapter = ShoppingListAdapter(it ?: arrayListOf())
      recyclerView.adapter = shoppingListAdapter
      shoppingListAdapter.notifyDataSetChanged()
    })
  }
}
