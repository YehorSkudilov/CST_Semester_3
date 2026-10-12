package com.example.lib

class MissionManager {
    public fun select(mission: Mission, missionListener: MissionListener): Unit {
        mission.start(missionListener)
    }

    public fun selectRepeatable(repeatable: Repeatable, int: Int, missionListener: MissionListener): Unit {
        repeatable.repeat(int, missionListener)
    }
}