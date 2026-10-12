package com.example.lib

class Elf: Minion() {
    override val race: String = "Elf"
    override val baseHealth: Int = 2
    override val baseSpeed: Int = 8
    override val backpackSize: Int = 3
    override val catchphrase = "My arrows never miss!"
}