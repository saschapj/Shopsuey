package myfirstapp.example.com.shopsuey

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
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
val COL_BUY_TOGGLE = "buytoggle"

val TABLE_STOCK = Tablenames.Stock.toString()
val COL_ART_ID = "artid"
val COL_STOCK_AMOUNT="stockamount"
val COL_MINSTOCK_AMOUNT="minstock"

val TABLE_SHOPPINGLIST = Tablenames.Shoppinglist.toString()
val COL_AMOUNT = "amount"

val TABLE_NOTES = Tablenames.Notes.toString()

class DataBaseHandler(var context: Context): SQLiteOpenHelper(context, DATABASE_NAME,null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableStockitems = "CREATE TABLE "+ TABLE_STOCK_ITEMS+" ("+
                COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " VARCHAR(256)," +
                COL_DESCRIPTION + " VARCHAR(256)," +
                COL_PRICE + " DECIMAL(5,2)," +
                COL_CONTENT + " INTEGER," +
                COL_UNIT + " VARCHAR(3),"+
                COL_BUY_TOGGLE + " INTEGER)"

        val createTableStock = "CREATE TABLE "+ TABLE_STOCK+" ("+
                COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_ART_ID + " INTEGER,"+
                COL_STOCK_AMOUNT + " INTEGER,"+
                COL_MINSTOCK_AMOUNT+ " INTEGER,"+
                " FOREIGN KEY ("+COL_ART_ID + ") REFERENCES "+ TABLE_STOCK_ITEMS +"("+ COL_ID+"))"

        val createTableShoppingList = "CREATE TABLE "+ TABLE_SHOPPINGLIST+" ("+
                COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_ART_ID + " INTEGER,"+
                COL_AMOUNT + " INTEGER,"+
                " FOREIGN KEY ("+COL_ART_ID + ") REFERENCES "+ TABLE_STOCK_ITEMS +"("+ COL_ID+"))"

        val createTableNotes = "CREATE TABLE "+ TABLE_NOTES+" ("+
                COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " VARCHAR(256))"




        db?.execSQL(createTableStockitems)
        db?.execSQL(createTableStock)
        db?.execSQL(createTableShoppingList)
        db?.execSQL(createTableNotes)
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
        cv.put(COL_BUY_TOGGLE,item.buyToggle)
        var result = db.insert(TABLE_STOCK_ITEMS,null,cv)
        db.close()
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
        db.close()
        if(result == -1.toLong()) {
            Toast.makeText(context,"failed to insertdata",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context,"Artikel eingefügt",Toast.LENGTH_SHORT).show()
        }
    }

    fun insertDataIntoNoteTable(item: NotesItem) {
        val db = this.writableDatabase
        var cv=ContentValues()
        cv.put(COL_NAME,item.name)

        var result = db.insert(TABLE_NOTES,null,cv)
        db.close()
        if(result == -1.toLong()) {
            Toast.makeText(context,"failed to insert data",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context,"Artikel eingefügt",Toast.LENGTH_SHORT).show()
        }
    }

    fun readDataForNotesList() : MutableList<NotesItem> {

        var list: MutableList<NotesItem> = ArrayList()

        var db = this.readableDatabase

        val query = "select * from Notes"

        var result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var noteItem = NotesItem()

                noteItem.name = result.getString(result.getColumnIndex(COL_NAME)).toString()
                noteItem.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                list.add(noteItem)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }


    fun readDataForShoppingList() : MutableList<ShoppingListItem> {

        var list: MutableList<ShoppingListItem> = ArrayList()

        var db = this.readableDatabase

        val query =
            "select name,description,price,content,unit,(minstock-stockamount) as amount from Stock, Stockitems\n" +
                    "   where minstock>stockamount\n" +
                    "   and Stock.artid = Stockitems.id"+
                    "   and buytoggle=1"


        var result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var shoppingListItem = ShoppingListItem()
                //shoppingListItem.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                shoppingListItem.name = result.getString(result.getColumnIndex(COL_NAME)).toString()
                shoppingListItem.description =
                    result.getString(result.getColumnIndex(COL_DESCRIPTION)).toString()
                shoppingListItem.price =
                    result.getString(result.getColumnIndex(COL_PRICE)).toDouble()
                shoppingListItem.content =
                    result.getString(result.getColumnIndex(COL_CONTENT)).toInt()
                shoppingListItem.unit = result.getString(result.getColumnIndex(COL_UNIT)).toString()
                shoppingListItem.amount =
                    result.getString(result.getColumnIndex(COL_AMOUNT)).toInt()
                list.add(shoppingListItem)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
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
                item.buyToggle= result.getString(result.getColumnIndex(COL_BUY_TOGGLE)).toInt()
                list.add(item)
            }while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }



    /*fun readDataFromStockTable() : MutableList<Stock>{
        var list : MutableList<Stock> = ArrayList()

        var db = this.readableDatabase
        val query = "SELECT * FROM "+ TABLE_STOCK
        var result = db.rawQuery(query,null)

        if(result.moveToFirst()) {
            do{
                var stock = Stock()
                stock.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                stock.art_id = result.getString(result.getColumnIndex(COL_ART_ID)).toInt()
                stock.stock = result.getString(result.getColumnIndex(COL_STOCK_AMOUNT)).toInt()
                stock.minStock = result.getString(result.getColumnIndex(COL_MINSTOCK_AMOUNT)).toInt()
                list.add(stock)
            }while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }*/



    fun increaseStock(amount: Int,id : Int) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_STOCK_AMOUNT,amount)

        db.update(TABLE_STOCK,cv,"artid="+id,null)
        db.close()
    }

    fun decreaseStock(amount: Int,id : Int) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_STOCK_AMOUNT,amount)
        db.update(TABLE_STOCK,cv,"artid="+id,null)
        db.close()
    }



    fun increaseMinStock(amount: Int,id : Int) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_MINSTOCK_AMOUNT,amount)
        db.update(TABLE_STOCK,cv,"artid="+id,null)
        db.close()
    }

    fun decreaseMinStock(amount: Int,id : Int) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_MINSTOCK_AMOUNT,amount)
        db.update(TABLE_STOCK,cv,"artid="+id,null)
        db.close()
    }

    fun turnOnBuy(id:Int) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_BUY_TOGGLE,1)

        db.update(TABLE_STOCK_ITEMS,cv,"id="+id,null)
        db.close()
    }

    fun turnOffBuy(id:Int) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_BUY_TOGGLE,0)

        db.update(TABLE_STOCK_ITEMS,cv,"id="+id,null)
        db.close()
    }

    fun updateDataIntoStockitems(item: Item) {

        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME,item.name)
        cv.put(COL_DESCRIPTION,item.description)
        cv.put(COL_UNIT,item.unit)
        cv.put(COL_CONTENT,item.content)
        cv.put(COL_PRICE,item.price)

        Log.d("price",item.price.toString())
        Log.d("id",item.id.toString())

        db.update(TABLE_STOCK_ITEMS,cv,"id="+item.id,null)
        db.close()
    }

    fun readDataSetFromStockItems(id: Int) : Item? {


        var db = this.readableDatabase
        val query = "SELECT * FROM "+ TABLE_STOCK_ITEMS+" WHERE ID="+id
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
                item.buyToggle=result.getString(result.getColumnIndex(COL_BUY_TOGGLE)).toInt()
                return item
            }while (result.moveToNext())
        }
        result.close()
        db.close()
        return null
    }



    fun readDataSetFromStockTable(id: Int) : Stock? {


        var db = this.readableDatabase
        val query = "SELECT * FROM "+ TABLE_STOCK+" WHERE ARTID="+id
        var result = db.rawQuery(query,null)

        if(result.moveToFirst()) {
            do{
                var stock = Stock()
                stock.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                stock.art_id = result.getString(result.getColumnIndex(COL_ART_ID)).toInt()
                stock.stock = result.getString(result.getColumnIndex(COL_STOCK_AMOUNT)).toInt()
                stock.minStock = result.getString(result.getColumnIndex(COL_MINSTOCK_AMOUNT)).toInt()
                return stock
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