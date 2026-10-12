package com.example.lib

class Gather(minion: Minion): Mission(minion), Repeatable {

    override fun determineMissionTime(): Int{
        return (minion.backpackSize + minion.baseSpeed) * (0..4).random()
    }

    override fun reward(time: Int): String {
        return when (time) {
            in 10..21 -> "bronze"
            in 22..33 -> "silver"
            in 34..50 -> "gold"
            else -> "nothing"
        }
    }

    override fun repeat(int: Int, missionListener: MissionListener) {
        repeat(int) {
            start(missionListener)
        }
    }

}