package myfirstapp.example.com.shopsuey

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.fragment_add_note.*
import java.text.DecimalFormat

class ShoppingListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_shopping_list, container, false)
        val listView = view.findViewById<ListView>(R.id.lv_shoppingList)
        val listViewNote = view.findViewById<ListView>(R.id.lv_notes)
        val noteButton = view.findViewById<Button>(R.id.btn_addNote)
        val fm = this.requireFragmentManager()
        val frag = this

        listViewNote.adapter = ShoppingListFragment.MyNoteAdapter(view.context, fm, frag)
        listView.adapter = ShoppingListFragment.MyCustomAdapter(view.context, fm, frag)

        noteButton.setOnClickListener({
            val ft = fm.beginTransaction()
            val nf = NoteFragment()
            ft.replace(R.id.fragment_container, nf)
            ft.commit()
        })

        return view
    }

    private class MyCustomAdapter(
        context: Context,
        fragmentManager: FragmentManager,
        shoppingListFragment: ShoppingListFragment
    ) : BaseAdapter() {

        val TABLE_STOCK_ITEMS = Tablenames.Stockitems.toString()
        val TABLE_STOCK = Tablenames.Stock.toString()
        var sumPrices = 0.0

        val db = DataBaseHandler(context)
        private val mContext: Context
        private val fm: FragmentManager
        private val sf: ShoppingListFragment

        init {
            mContext = context
            fm = fragmentManager
            sf = shoppingListFragment

        }

        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {

            val readDataForShoppingList = db.readDataForShoppingList()

            var row_ShoppingItem = View(mContext)
         //   if (readDataForShoppingList.size > 0 && position < readDataForShoppingList.size) {

                Log.d("test", readDataForShoppingList.size.toString())
                val layoutInflater = LayoutInflater.from(mContext)

                row_ShoppingItem =
                    layoutInflater.inflate(R.layout.row_shopping_item, viewGroup, false)

                Log.d("position: ", position.toString())
                Log.d("size", readDataForShoppingList.size.toString())
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

                val decInt = DecimalFormat("###,###,###")

                content.text = decInt.format(readDataForShoppingList.get(position).content.toString().toInt()).toString()


                val unit = row_ShoppingItem.findViewById<TextView>(R.id.tvShoppingUnit)
                unit.text = readDataForShoppingList.get(position).unit

                val buyingAmount = row_ShoppingItem.findViewById<TextView>(R.id.tvBuyingAmount)
                buyingAmount.text = readDataForShoppingList.get(position).amount.toString() + " x"

                val sumTv = viewGroup?.rootView?.findViewById<TextView>(R.id.tvShoppingSum)
                val sum = getSum(db.readDataForShoppingList())

                sumTv?.text = "Total: " + dec.format(sum).toString() + " €"



                row_ShoppingItem.setOnClickListener({
                    val ft = fm.beginTransaction()
                    val idf = ItemDetailFragment(db.readDataFromStockItems().get(position).id)

                    ft.replace(R.id.fragment_container, idf)
                    ft.commit()
                })


         //   }
            return row_ShoppingItem
        }

        override fun getItem(position: Int): Any {
            return "Teststring"
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return db.readDataForShoppingList().size
        }

        fun getSum(list: MutableList<ShoppingListItem>): Double {
            var sum = 0.0
            for (item in list) {
                sum += item.price * item.amount
            }
            return sum
        }


    }

    private class MyNoteAdapter(
        context: Context,
        fragmentManager: FragmentManager,
        shoppingListFragment: ShoppingListFragment

    ) : BaseAdapter() {


        val TABLE_NOTES = Tablenames.Notes.toString()

        val db = DataBaseHandler(context)
        private val mContext: Context
        private val fm: FragmentManager
        private val sf: ShoppingListFragment

        init {
            mContext = context
            fm = fragmentManager
            sf = shoppingListFragment
        }

        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            var row_NoteItem = View(mContext)

                val layoutInflater = LayoutInflater.from(mContext)
                val readDataForNotesList = db.readDataForNotesList()

                row_NoteItem = layoutInflater.inflate(R.layout.row_note, viewGroup, false)

                val rowItemName =
                    row_NoteItem.findViewById<TextView>(R.id.tvRowItemName)
                rowItemName.text = readDataForNotesList.get(position).name


            var delButton = row_NoteItem.findViewById<AppCompatImageButton>(R.id.tvNoteRowDeleteButton)
            delButton.setOnClickListener({
                Toast.makeText(mContext,"test",Toast.LENGTH_LONG)
                db.deleteData(TABLE_NOTES,readDataForNotesList.get(position).id.toInt())
                val ft = fm.beginTransaction()
                ft.detach(sf)
                ft.attach(sf)
                ft.commit()

            })

            return row_NoteItem

        }

        override fun getItem(position: Int): Any {
            return "Teststring"
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return db.readDataForNotesList().size
        }





    }

}