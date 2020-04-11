package myfirstapp.example.com.shopsuey

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.text.DecimalFormat

class StockFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view: View = inflater.inflate(R.layout.fragment_stock,container,false)

        val listView = view.findViewById<ListView>(R.id.lv_stock)

        val fm = this.requireFragmentManager()
        val frag = this

        listView.adapter = MyCustomAdapter(view.context,fm,frag)



        return view
    }

    private class MyCustomAdapter(
        context: Context,
        fragmentManager: FragmentManager,
        stockFragment: StockFragment
    ): BaseAdapter() {

        val TABLE_STOCK_ITEMS = Tablenames.Stockitems.toString()
        val TABLE_STOCK = Tablenames.Stock.toString()

        val db = DataBaseHandler(context)
        private val mContext: Context
        private val fm : FragmentManager
        private val sf : StockFragment
        init {
            mContext = context
            fm = fragmentManager
            sf = stockFragment
        }

        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {

            val layoutInflater = LayoutInflater.from(mContext)
            val row_item = layoutInflater.inflate(R.layout.row_item,viewGroup,false)

            val rowItemName = row_item.findViewById<TextView>(R.id.tvRowItemName)
            rowItemName.text = db.readDataFromStockItems().get(position).name

            val rowItemDesc = row_item.findViewById<TextView>(R.id.tvRowItemDesc)
            rowItemDesc.text = db.readDataFromStockItems().get(position).description

            val rowItemPrice = row_item.findViewById<TextView>(R.id.tvRowItemPrice)
            val price = db.readDataFromStockItems().get(position).price.toDouble()
            val dec = DecimalFormat("#,###.00")
            rowItemPrice.text = dec.format(price).toString()+" €"

            val content = row_item.findViewById<TextView>(R.id.tvRowItemContent)
            content.text = db.readDataFromStockItems().get(position).content.toString()

            val unit = row_item.findViewById<TextView>(R.id.tvRowItemUnit)
            unit.text = db.readDataFromStockItems().get(position).unit

            var delButton = row_item.findViewById<ImageButton>(R.id.tvRowDeleteButton)


            delButton.setOnClickListener({
                Toast.makeText(mContext,"test",Toast.LENGTH_LONG)
                db.deleteData(TABLE_STOCK_ITEMS,db.readDataFromStockItems().get(position).id)
                val ft = fm.beginTransaction()
                ft.detach(sf)
                ft.attach(sf)
                ft.commit()

            })

            row_item.setOnClickListener({
                val ft = fm.beginTransaction()
                val idf = ItemDetailFragment(db.readDataFromStockItems().get(position).id)



                ft.replace(R.id.fragment_container,idf)
                ft.commit()
            })


            return row_item

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