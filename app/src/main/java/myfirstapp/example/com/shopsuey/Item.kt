package myfirstapp.example.com.shopsuey


class Item {
    var id: Int = 0
    var name: String = ""
    var description: String = ""
    var price : Double = 0.0

    constructor(name:String,description: String,price:Double) {
        this.name=name
        this.description=description
        this.price=price
    }
    constructor()
}