package myfirstapp.example.com.shopsuey

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

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

        val readDataSet = db.readDataSet(TABLE_STOCK_ITEMS,datasetIdLocal)

        Log.d("test",readDataSet?.unit)

        val itemName = view.findViewById<TextView>(R.id.tv_artdetailname)
        itemName.text = readDataSet?.name

        val itemDesc = view.findViewById<TextView>(R.id.tv_artdetaildescription)
        itemDesc.text = readDataSet?.description

        val itemPrice = view.findViewById<TextView>(R.id.tv_artdetailprice)
        itemPrice.text = readDataSet?.price.toString()

        val itemContent = view.findViewById<TextView>(R.id.tv_artdetailcontent)
        itemContent.text = "sdfko"
        Log.d("test2",readDataSet?.content.toString())
        itemContent.text = readDataSet?.content.toString()


        val itemUnit = view.findViewById<TextView>(R.id.tv_artdetailunit)
        itemUnit.text = "sdfkoasedfadse"
        itemUnit.text = readDataSet?.unit.toString()
        //itemUnit.text = readDataSet?.unit

        return view
    }
}