package com.bcit.lib

fun main() {
    val historicalEvents = mapOf(
        1492 to "Christopher Columbus discovers America",
        1601 to "William Shakespeare writes Hamlet",
        1632 to "Galileo discovered the acceleration of gravity on Earth to be 9.8m/s",
        1838 to "Roughly 9.46 trillion km, the light-year is first used as a measurement in astronomy",
        2020 to "Covid 19 Pandemic"
    )

    historicalEvents.forEach { (year, event) ->
        println("$year: $event")
    }
    println("")

    val anonymousPrint = fun(eventIndex: Int) {
        println(historicalEvents[eventIndex])
    }

    val lambdaPrint: (String) -> Unit = { event -> println(event) }

    fun displayEvent(eventIndex: Int, printFunction: (Int) -> Unit) {
        printFunction(eventIndex)
    }

    fun displayEvent(eventIndex: String, printFunction: (String) -> Unit) {
        printFunction(eventIndex)
    }

    fun getEvent(event: Int): String {
        return historicalEvents.get(event) ?: ""
    }

    anonymousPrint(1492)
    lambdaPrint(historicalEvents.get(1601)!!)
    displayEvent(1632, anonymousPrint)
    displayEvent(getEvent(1838), ::println)
    println(getEvent(2020))
}