package myfirstapp.example.com.shopsuey

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.text.FieldPosition

val DATABASE_NAME = "shopsueydb"
val TABLE_NAME = "StockItems"
val COL_NAME = "name"
val COL_DESCRIPTION = "description"
val COL_PRICE = "price"
val COL_ID = "id"
val COL_CONTENT = "content"
val COL_UNIT = "unit"
//angaben ml-gr-stueck
//angabe inhal)sauce zb 700 ml=
//angabe rest vorhanden )zb 30 pro cent oder 0,4 oder 1,6 oder wie auch immer

class DataBaseHandler(var context: Context): SQLiteOpenHelper(context, DATABASE_NAME,null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE "+ TABLE_NAME+ " ("+
                COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " VARCHAR(256)," +
                COL_DESCRIPTION + " VARCHAR(256)," +
                COL_PRICE + " DECIMAL(5,2)," +
                COL_CONTENT + " INTEGER," +
                COL_UNIT + " VARCHAR(3))"

        db?.execSQL(createTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertData(item: Item) {
        val db = this.writableDatabase
        var cv=ContentValues()
        cv.put(COL_NAME,item.name)
        cv.put(COL_DESCRIPTION,item.description)
        cv.put(COL_PRICE,item.price)
        cv.put(COL_CONTENT,item.content)
        cv.put(COL_UNIT,item.unit)
        var result = db.insert(TABLE_NAME,null,cv)

        if(result == -1.toLong()) {
            Toast.makeText(context,"failed to insertdata",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context,"Artikel eingefügt",Toast.LENGTH_SHORT).show()
        }
    }

    fun readData() : MutableList<Item>{
        var list : MutableList<Item> = ArrayList()

        var db = this.readableDatabase
        val query = "SELECT * FROM "+ TABLE_NAME
        var result = db.rawQuery(query,null)

        if(result.moveToFirst()) {
            do{
                var item = Item()
                item.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                item.name = result.getString(result.getColumnIndex(COL_NAME)).toString()
                item.description = result.getString(result.getColumnIndex(COL_DESCRIPTION)).toString()
                item.price = result.getString(result.getColumnIndex(COL_PRICE)).toDouble()
                item.content = result.getString(result.getColumnIndex(COL_CONTENT)).toInt()
                item.unit = result.getString(result.getColumnIndex(COL_UNIT)).toString()
                list.add(item)
            }while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    fun readDataSet(id: Int) : Item? {


        var db = this.readableDatabase
        val query = "SELECT * FROM "+ TABLE_NAME+" WHERE ID="+id
        var result = db.rawQuery(query,null)

        if(result.moveToFirst()) {
            do{
                var item = Item()
                item.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                item.name = result.getString(result.getColumnIndex(COL_NAME)).toString()
                item.description = result.getString(result.getColumnIndex(COL_DESCRIPTION)).toString()
                item.price = result.getString(result.getColumnIndex(COL_PRICE)).toDouble()
                item.content = result.getString(result.getColumnIndex(COL_CONTENT)).toInt()
                item.unit = result.getString(result.getColumnIndex(COL_UNIT)).toString()
                return item
            }while (result.moveToNext())
        }
        result.close()
        db.close()
        return null
    }

    fun deleteData(id: Int){
        val db = this.writableDatabase


        db.delete(TABLE_NAME, COL_ID+"=?", arrayOf((id).toString()))




        db.close()
    }
}