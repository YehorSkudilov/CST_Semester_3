package com.bcit.lib

const val VERSION_NUM: Double = 2.1
const val SLOGAN: String = "deliver with a frown"

fun main() {
    val streetNum: Int = 40
    val streetName: String = "some street"

    var fullAddress: String? =
    if(VERSION_NUM >= 1){
        "$streetNum $streetName, Glasgow, Scotland"
    } else{
        println("""Starting Beta Version...
...
...
...""")
        null
    }
    val serviceNameAndVersion: String = "Food Delivery Service: v%s"
    val message: String ="""           
      ${serviceNameAndVersion.format(VERSION_NUM)}
        
Welcome to best food delivery service
      
      Please deliver the food to:
      
      ${fullAddress?.uppercase()}
      
You are the best... ${SLOGAN.uppercase()}
      
...Tank You!
    """
    println(message)
}