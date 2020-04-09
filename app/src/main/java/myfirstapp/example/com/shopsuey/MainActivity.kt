package myfirstapp.example.com.shopsuey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var stockFragment: StockFragment
    lateinit var shoppingListFragment: ShoppingListFragment
    lateinit var addItemFragment: AddItemFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView : BottomNavigationView = findViewById(R.id.bottom_navigation)

        stockFragment = StockFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,stockFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {

                R.id.nav_stock -> {
                    stockFragment = StockFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,stockFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                }
                R.id.nav_shoppingList -> {
                    shoppingListFragment = ShoppingListFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,shoppingListFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                }
                R.id.nav_addItem -> {
                    addItemFragment = AddItemFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,addItemFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                }
            }
            true
        }
    }


}
