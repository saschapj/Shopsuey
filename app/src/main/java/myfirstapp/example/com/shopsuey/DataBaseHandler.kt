package myfirstapp.example.com.shopsuey

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DATABASE_NAME = "shopsueydb"
val TABLE_NAME = "StockItems"
val COL_NAME = "name"
val COL_DESCRIPTION = "description"
val COL_PRICE = "price"
val COL_ID = "id"

class DataBaseHandler(var context: Context): SQLiteOpenHelper(context, DATABASE_NAME,null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE "+ TABLE_NAME+ " ("+
                COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " VARCHAR(256)," +
                COL_DESCRIPTION + " VARCHAR(256)," +
                COL_PRICE + " DECIMAL(5,2))"

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
        var result = db.insert(TABLE_NAME,null,cv)

        if(result == -1.toLong()) {
            Toast.makeText(context,"failed to insertdata",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context,"Artikel eingefügt",Toast.LENGTH_SHORT).show()
        }
    }
}