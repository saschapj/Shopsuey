package myfirstapp.example.com.shopsuey

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_edit_item.*
import java.text.DecimalFormat


class EditItemFragment(datasetId: Int) : Fragment(), AdapterView.OnItemSelectedListener {

    private val datasetIdLocal : Int

    var itemUnit = "f"

    lateinit var options : Array<String>

    init {
        datasetIdLocal = datasetId
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_edit_item,container,false)

        val db = DataBaseHandler(requireContext())

        val readDataSet = db.readDataSetFromStockItems(datasetIdLocal)

        val readDataStock = db.readDataSetFromStockTable(datasetIdLocal)

        Log.d("test",readDataSet?.unit)

        val itemId = readDataSet?.id

        val itemName = view.findViewById<TextView>(R.id.etv_artNameEdit)
        itemName.text = readDataSet?.name

        val itemDesc = view.findViewById<TextView>(R.id.etv_artDescEdit)
        itemDesc.text = readDataSet?.description

        val itemPrice = view.findViewById<TextView>(R.id.etv_artPriceEdit)

        val price = readDataSet?.price.toString().toDouble()
        val dec = DecimalFormat("#,###.00")
        itemPrice.text = dec.format(price).toString() + " €"

        val itemContent = view.findViewById<TextView>(R.id.etv_artContentEdit)

        val amountToString = readDataSet?.content.toString()

        val decInt = DecimalFormat("###,###,###")

        itemContent.text = decInt.format(amountToString.toInt()).toString()//amountToString

        val itemUnit = readDataSet?.unit.toString()

        Log.d("test",itemUnit)


        val unitSpinner = view.findViewById<Spinner>(R.id.spinnerUnitEdit)

        options = resources.getStringArray(R.array.unitArray)

        val spinnerPosition: Int

        ArrayAdapter.createFromResource(view.context,R.array.unitArray,android.R.layout.simple_spinner_item).also {
                adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

             spinnerPosition = adapter.getPosition(itemUnit)

            unitSpinner.setSelection(spinnerPosition)

            unitSpinner.adapter = adapter
        }

        unitSpinner.setSelection(spinnerPosition,false)

        unitSpinner.onItemSelectedListener = this

        val btn_editArticle = view.findViewById<Button>(R.id.btn_editArticle)
        btn_editArticle.setOnClickListener({

            if(itemName.text.toString().length>0&&itemPrice.text.toString().length>0&&itemContent.text.toString().length>0) {
                var item = Item(itemName.text.toString(),
                    itemDesc.text.toString(),
                    itemPrice.text.split(" €")[0].replace(".","",true).replace(",","",true).toString().toDouble(),
                    itemContent.text.toString().toInt(),
                    itemUnit.toString())
                var db = DataBaseHandler(requireContext())
                if (itemId != null) {
                    item.id = itemId
                }
                Log.d("testid",item.id.toString())
                db.updateDataIntoStockitems(item) //Update where...

                var lastItemId = db.getLastAddedItem()?.id

                var stock = lastItemId?.let { it1 -> Stock(it1,1) }

                stock?.let { it1 -> db.insertDataIntoStockTable(it1) }

            } else{
                Toast.makeText(context,"Please fill in the data", Toast.LENGTH_LONG)
            }
        })

        return view
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

        itemUnit = itemUnit
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        Log.d("test2",options.get(position))

        itemUnit = options.get(position)
    }
}