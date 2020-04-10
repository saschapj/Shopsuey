package myfirstapp.example.com.shopsuey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_add_item.*

class AddItemFragment : Fragment() {



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
        addItemButton.setOnClickListener({
            if(itemName.text.toString().length>0&&itemPrice.text.toString().length>0) {
                var item = Item(itemName.text.toString()
                    ,itemDesc.text.toString(),
                    itemPrice.text.toString().toDouble())
                var db = DataBaseHandler(requireContext())
                db.insertData(item)
            } else{
                Toast.makeText(context,"Please fill in the data",Toast.LENGTH_LONG)
            }
        })
        return view
    }
}