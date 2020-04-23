package myfirstapp.example.com.shopsuey

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class AddItemFragment : Fragment(),AdapterView.OnItemSelectedListener {

    var itemUnit = "g";

    lateinit var options : Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_add_item,container,false)


        val addItemButton = view.findViewById<Button>(R.id.btn_addArticle)
        val itemName = view.findViewById<EditText>(R.id.etv_artName)
        val itemDesc = view.findViewById<EditText>(R.id.etv_artDesc)
        val itemPrice = view.findViewById<EditText>(R.id.etv_artPrice)

        val itemContent = view.findViewById<TextView>(R.id.etv_artContent)
        val unitSpinner = view.findViewById<Spinner>(R.id.spinnerUnit)

        options = resources.getStringArray(R.array.unitArray)

        unitSpinner.onItemSelectedListener = this


        ArrayAdapter.createFromResource(view.context,R.array.unitArray,android.R.layout.simple_spinner_item).also {
                adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            unitSpinner.adapter = adapter
        }

        addItemButton.setOnClickListener({
            if(itemName.text.toString().length>0&&itemPrice.text.toString().length>0&&itemContent.text.toString().length>0) {
                var item = Item(itemName.text.toString(),
                    itemDesc.text.toString(),
                    itemPrice.text.toString().toDouble(),
                    itemContent.text.toString().toInt(),
                    itemUnit.toString())
                var db = DataBaseHandler(requireContext())
                db.insertDataIntoStockitems(item)

                var lastItemId = db.getLastAddedItem()?.id

                var stock = lastItemId?.let { it1 -> Stock(it1,1) }

                stock?.let { it1 -> db.insertDataIntoStockTable(it1) }

                val fm = this.requireFragmentManager()
                val frag = this

                val ft = fm.beginTransaction()
                val sf = StockFragment()
                ft.replace(R.id.fragment_container,sf)
                ft.commit()


            } else{
                Toast.makeText(context,"Please fill in the data",Toast.LENGTH_LONG)
            }
        })
        return view
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        itemUnit = "g"
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

            itemUnit = options.get(position)

    }
}