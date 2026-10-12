package com.example.lib

interface MissionListener {
    public abstract fun missionStart(minion: Minion): Unit
    public abstract fun missionProgress(): Unit
    public abstract fun missionComplete(minion: Minion, message: String): Unit
}