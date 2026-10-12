package com.example.lib

fun main() {
    val missionManager = MissionManager()

    val missionListener = object : MissionListener {
        override fun missionStart(minion: Minion) {
            println("The ${minion.race} has started the mission")
        }

        override fun missionProgress() {
            println("...")
        }

        override fun missionComplete(minion: Minion, message: String) {
            println("The ${minion.race} has returned with: $message")
        }
    }

    missionManager.select(Hunt(Elf()), missionListener)
    missionManager.selectRepeatable(Gather(Dwarf()), 3, missionListener)
}
