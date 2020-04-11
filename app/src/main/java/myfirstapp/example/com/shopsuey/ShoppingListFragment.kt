package myfirstapp.example.com.shopsuey

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.text.DecimalFormat

class ShoppingListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view:View = inflater.inflate(R.layout.fragment_shopping_list,container,false)
        val listView = view.findViewById<ListView>(R.id.lv_shoppingList)
        val fm = this.requireFragmentManager()
        val frag = this


        listView.adapter = ShoppingListFragment.MyCustomAdapter(view.context, fm, frag)
        return view
    }

        private class MyCustomAdapter(
            context: Context,
            fragmentManager: FragmentManager,
            shoppingListFragment: ShoppingListFragment
        ): BaseAdapter() {

            val TABLE_STOCK_ITEMS = Tablenames.Stockitems.toString()
            val TABLE_STOCK = Tablenames.Stock.toString()

            val db = DataBaseHandler(context)
            private val mContext: Context
            private val fm : FragmentManager
            private val sf : ShoppingListFragment

            init {
                mContext = context
                fm = fragmentManager
                sf = shoppingListFragment

            }

            override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {

                val readDataForShoppingList = db.readDataForShoppingList()

                var row_ShoppingItem = View(mContext)
                if(readDataForShoppingList.size>0 &&position<readDataForShoppingList.size) {

                    Log.d("test",readDataForShoppingList.size.toString())
                val layoutInflater = LayoutInflater.from(mContext)

                     row_ShoppingItem = layoutInflater.inflate(R.layout.row_shopping_item,viewGroup,false)

                    Log.d("position: ",position.toString())
                    Log.d("size",readDataForShoppingList.size.toString())
                    val rowItemName =
                        row_ShoppingItem.findViewById<TextView>(R.id.tvShoppingRowName)
                    rowItemName.text = readDataForShoppingList.get(position).name


                    val rowItemDesc =
                        row_ShoppingItem.findViewById<TextView>(R.id.tvShoppingRowDesc)
                    rowItemDesc.text = readDataForShoppingList.get(position).description

                    val rowItemPrice =
                        row_ShoppingItem.findViewById<TextView>(R.id.tvShoppingRowPrice)
                    val price = readDataForShoppingList.get(position).price

                    val dec = DecimalFormat("#,###.00")
                    rowItemPrice.text = dec.format(price).toString() + " €"

                    val content = row_ShoppingItem.findViewById<TextView>(R.id.tvShoppingContent)
                    content.text = readDataForShoppingList.get(position).content.toString()


                    val unit = row_ShoppingItem.findViewById<TextView>(R.id.tvShoppingUnit)
                    unit.text = readDataForShoppingList.get(position).unit


                row_ShoppingItem.setOnClickListener({
                    val ft = fm.beginTransaction()
                    val idf = ItemDetailFragment(db.readDataFromStockItems().get(position).id)

                    ft.replace(R.id.fragment_container,idf)
                    ft.commit()
                })


                }
                return row_ShoppingItem
            }

            override fun getItem(position: Int): Any {
                return "Teststring"
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun getCount(): Int {
                return db.readDataFromStockItems().size
            }





        }


}