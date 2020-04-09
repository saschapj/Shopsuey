package myfirstapp.example.com.shopsuey

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment

class StockFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_stock,container,false)

        val listView = view.findViewById<ListView>(R.id.lv_stock)

        listView.adapter = MyCustomAdapter(view.context)
        return view
    }

    private class MyCustomAdapter(context: Context): BaseAdapter() {

        private val mContext: Context

        init {
            mContext = context
        }

        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {

            val layoutInflater = LayoutInflater.from(mContext)
            val row_item = layoutInflater.inflate(R.layout.row_item,viewGroup,false)
            return row_item
            /*val textView = TextView(mContext)
            textView.text = "here is my row"
            return textView*/
        }

        override fun getItem(position: Int): Any {
            return "Teststring"
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return 5
        }

    }
}