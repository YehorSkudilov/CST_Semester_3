package com.example.lib

fun main() {
    val missionManager = MissionManager()

    val missionListener = object : MissionListener {
        override fun missionStart(minion: Minion) {
            println("The ${minion.race} has started the mission")
        }

        override fun missionProgress() {
            for(i in 1..3){
                println("...")
            }
        }

        override fun missionComplete(minion: Minion, message: String) {
            println("The ${minion.race} has returned with: $message\n")
        }
    }

    print("Choose a minion (1 = Dwarf, 2 = Elf): ")
    val minion = when (readln().trim()) {
        "2" -> Elf()
        else -> Dwarf()
    }

    missionManager.select(Hunt(minion), missionListener)
    missionManager.selectRepeatable(Gather(minion), 3, missionListener)
}
