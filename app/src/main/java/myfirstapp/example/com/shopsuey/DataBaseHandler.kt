package myfirstapp.example.com.shopsuey

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.text.FieldPosition

val DATABASE_NAME = "shopsueydb"
val TABLE_STOCK_ITEMS = Tablenames.Stockitems.toString()
val COL_NAME = "name"
val COL_DESCRIPTION = "description"
val COL_PRICE = "price"
val COL_ID = "id"
val COL_CONTENT = "content"
val COL_UNIT = "unit"

val TABLE_STOCK = Tablenames.Stock.toString()
val COL_ART_ID = "artid"
val COL_STOCK_AMOUNT="stockamount"
val COL_MINSTOCK_AMOUNT="minstock"

class DataBaseHandler(var context: Context): SQLiteOpenHelper(context, DATABASE_NAME,null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableStockitems = "CREATE TABLE "+ TABLE_STOCK_ITEMS+" ("+
                COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " VARCHAR(256)," +
                COL_DESCRIPTION + " VARCHAR(256)," +
                COL_PRICE + " DECIMAL(5,2)," +
                COL_CONTENT + " INTEGER," +
                COL_UNIT + " VARCHAR(3))"

        val createTableStock = "CREATE TABLE "+ TABLE_STOCK+" ("+
                COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_ART_ID + " INTEGER,"+
                COL_STOCK_AMOUNT + " INTEGER,"+
                COL_MINSTOCK_AMOUNT+ " INTEGER,"+
                " FOREIGN KEY ("+COL_ART_ID + ") REFERENCES "+ TABLE_STOCK_ITEMS +"("+ COL_ID+"))"


        db?.execSQL(createTableStockitems)
        db?.execSQL(createTableStock)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertDataIntoStockitems(item: Item) {
        val db = this.writableDatabase
        var cv=ContentValues()
        cv.put(COL_NAME,item.name)
        cv.put(COL_DESCRIPTION,item.description)
        cv.put(COL_PRICE,item.price)
        cv.put(COL_CONTENT,item.content)
        cv.put(COL_UNIT,item.unit)
        var result = db.insert(TABLE_STOCK_ITEMS,null,cv)

        if(result == -1.toLong()) {
            Toast.makeText(context,"failed to insertdata",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context,"Artikel eingefügt",Toast.LENGTH_SHORT).show()
        }
    }

    fun insertDataIntoStockTable(stock: Stock) {
        val db = this.writableDatabase
        var cv=ContentValues()
        cv.put(COL_ART_ID,stock.art_id)
        cv.put(COL_STOCK_AMOUNT,1)
        cv.put(COL_MINSTOCK_AMOUNT,0)
        var result = db.insert(TABLE_STOCK,null,cv)

        if(result == -1.toLong()) {
            Toast.makeText(context,"failed to insertdata",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context,"Artikel eingefügt",Toast.LENGTH_SHORT).show()
        }
    }

    fun readDataFromStockItems() : MutableList<Item>{
        var list : MutableList<Item> = ArrayList()

        var db = this.readableDatabase
        val query = "SELECT * FROM "+ TABLE_STOCK_ITEMS
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

    fun readDataSet(table:String,id: Int) : Item? {


        var db = this.readableDatabase
        val query = "SELECT * FROM "+ table+" WHERE ID="+id
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

    fun getLastAddedItem() : Item? {


        var db = this.readableDatabase
        val query = "SELECT * FROM "+ TABLE_STOCK_ITEMS+" ORDER BY ID DESC LIMIT 1"
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

    fun deleteData(table:String,id: Int){
        val db = this.writableDatabase


        db.delete(table, COL_ID+"=?", arrayOf((id).toString()))




        db.close()
    }
}