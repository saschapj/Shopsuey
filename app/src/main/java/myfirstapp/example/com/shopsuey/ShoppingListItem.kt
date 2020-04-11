package myfirstapp.example.com.shopsuey

class ShoppingListItem {
    var id: Int = 0
    var name: String = ""
    var description: String = ""
    var price : Double = 0.0
    var content : Int = 0
    var unit : String =""
    var amount = 0

    constructor(name:String,description: String,price:Double,content:Int,unit:String,amount:Int) {
        this.name=name
        this.description=description
        this.price=price
        this.content=content
        this.unit=unit
        this.amount = amount
    }
    constructor()
}
