package myfirstapp.example.com.shopsuey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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


        val btnAddarticle = view.findViewById<Button>(R.id.btn_addArticle)

        btnAddarticle.setOnClickListener({
            if(etv_artName.toString().length>0&&etv_artPrice.text.toString().length>0) {
                var item = Item(etv_artName.text.toString(),etv_artDesc.text.toString(),etv_artPrice.text.toString().toDouble())
                var db = DataBaseHandler(requireContext())
                db.insertData(item)
            } else {
                Toast.makeText(context,"Bitte alles ausfüllen", Toast.LENGTH_SHORT)
            }
        })

        return view
    }
}