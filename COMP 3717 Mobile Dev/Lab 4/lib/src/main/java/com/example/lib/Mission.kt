package com.example.lib

abstract class Mission(
    protected val minion: Minion
) {

    init {
        println(minion.catchphrase)
    }

    public fun start(missionListener: MissionListener): Unit{
        missionListener.missionStart(minion)
        missionListener.missionProgress()
        missionListener.missionComplete(minion, reward(determineMissionTime()))
    }

    protected abstract fun determineMissionTime(): Int

    protected abstract fun reward(int: Int): String

}
