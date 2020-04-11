package myfirstapp.example.com.shopsuey


class Item {
    var id: Int = 0
    var name: String = ""
    var description: String = ""
    var price : Double = 0.0
    var content : Int = 0
    var unit : String =""

    constructor(name:String,description: String,price:Double,content:Int,unit:String) {
        this.name=name
        this.description=description
        this.price=price
        this.content=content
        this.unit=unit

    }
    constructor()
}