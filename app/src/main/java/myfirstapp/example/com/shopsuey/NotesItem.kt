package myfirstapp.example.com.shopsuey


class NotesItem {
    var id: Int = 0
    var name: String = ""
    var description: String = ""
    var price : Double = 0.0
    var content : Int = 0
    var unit : String =""
    var buyToggle: Int = 1

    constructor(name:String) {
        this.name=name

    }
    constructor()
}