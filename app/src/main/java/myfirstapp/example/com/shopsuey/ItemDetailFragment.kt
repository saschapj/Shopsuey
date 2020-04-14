package myfirstapp.example.com.shopsuey

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.DecimalFormat

class ItemDetailFragment(datasetId: Int) : Fragment() {

    private val datasetIdLocal : Int

    init {
        datasetIdLocal = datasetId
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_item_detail,container,false)

        val db = DataBaseHandler(requireContext())

        val readDataSet = db.readDataSetFromStockItems(datasetIdLocal)

        val readDataStock = db.readDataSetFromStockTable(datasetIdLocal)

        Log.d("test",readDataSet?.unit)

        val itemName = view.findViewById<TextView>(R.id.tv_artdetailname)
        itemName.text = readDataSet?.name

        val itemDesc = view.findViewById<TextView>(R.id.tv_artdetaildescription)
        itemDesc.text = readDataSet?.description

        val itemPrice = view.findViewById<TextView>(R.id.tv_artdetailprice)

        val price = readDataSet?.price.toString().toDouble()
        val dec = DecimalFormat("#,###.00")
        itemPrice.text = dec.format(price).toString() + " €"

        val itemContent = view.findViewById<TextView>(R.id.tv_artdetailcontent)


        val amountToString = readDataSet?.content.toString()

        val decInt = DecimalFormat("###,###,###")

        itemContent.text = decInt.format(amountToString.toInt()).toString()//amountToString




//        itemContent.text = dec.format(itemContent).toString()

        val itemUnit = view.findViewById<TextView>(R.id.tv_artdetailunit)
        itemUnit.text = readDataSet?.unit.toString()

        val itemStock = view.findViewById<TextView>(R.id.tv_artdetailstock)
        itemStock.text = readDataStock?.stock.toString()

        val itemMinStock = view.findViewById<TextView>(R.id.tv_artdetailminstock)
        itemMinStock.text = readDataStock?.minStock.toString()

        val stockDecButton = view.findViewById<Button>(R.id.btn_stockDec)
        val stockIncButton = view.findViewById<Button>(R.id.btn_stockInc)
        val minStockDecButton = view.findViewById<Button>(R.id.btn_minStockDec)
        val minStockIncButton = view.findViewById<Button>(R.id.btn_minStockInc)

        stockDecButton.setOnClickListener({
            //db.decreaseStock(datasetIdLocal);
            itemStock.text = readDataStock?.stock.toString()
        })

        stockIncButton.setOnClickListener({
            var amount = itemStock.text.toString().toInt()
            amount++
            db.increaseStock(amount,datasetIdLocal);
            itemStock.text = amount.toString()

        })

        stockDecButton.setOnClickListener({
            var amount = itemStock.text.toString().toInt()
            amount--
            db.decreaseStock(amount,datasetIdLocal);
            itemStock.text = amount.toString()

        })

        minStockIncButton.setOnClickListener({
            var amount = itemMinStock.text.toString().toInt()
            amount++
            db.increaseMinStock(amount,datasetIdLocal);
            itemMinStock.text = amount.toString()

        })

            minStockDecButton.setOnClickListener({
            var amount = itemMinStock.text.toString().toInt()
            amount--
            db.decreaseMinStock(amount,datasetIdLocal);
            itemMinStock.text = amount.toString()

        })


        return view
    }
}