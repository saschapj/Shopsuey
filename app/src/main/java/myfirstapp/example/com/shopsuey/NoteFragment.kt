package myfirstapp.example.com.shopsuey

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class NoteFragment : Fragment(){



    lateinit var options : Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_add_note,container,false)


        val addItemButton = view.findViewById<Button>(R.id.btn_addNote)
        val itemName = view.findViewById<EditText>(R.id.etv_noteName)

        addItemButton.setOnClickListener({
            if(itemName.text.toString().length>0) {
                var item = NotesItem(itemName.text.toString())
                var db = DataBaseHandler(requireContext())
                db.insertDataIntoNoteTable(item)

                val fm = this.requireFragmentManager()
                val ft = fm.beginTransaction()
                val slf = ShoppingListFragment()
                ft.replace(R.id.fragment_container,slf)
                ft.commit()

            } else{
                Toast.makeText(context,"Please fill in the data",Toast.LENGTH_LONG)
            }
        })
        return view
    }

}