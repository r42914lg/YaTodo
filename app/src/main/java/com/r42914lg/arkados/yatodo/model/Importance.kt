package com.r42914lg.arkados.yatodo.model

import java.lang.IllegalArgumentException

sealed class Importance  {
    abstract fun intRep() : Int
    abstract fun stringRep() : String

    companion object {
        const val LOW_CODE = 0
        const val DEFAULT_CODE = 1
        const val HIGH_CODE = 2
        const val LOW_STR = "Low"
        const val DEFAULT_STR = "Default"
        const val HIGH_STR = "!! High"
    }

    class Factory() {
        fun parseFromStr(str: String): Importance =
            when (str) {
                LOW_STR -> LOW
                DEFAULT_STR -> DEFAULT
                HIGH_STR -> HIGH
                else -> {
                    throw IllegalArgumentException(" Check Importance class")
                }
            }
        fun parseFromInt(int: Int): Importance =
            when (int) {
                LOW_CODE -> LOW
                DEFAULT_CODE -> DEFAULT
                HIGH_CODE -> HIGH
                else -> {
                    throw IllegalArgumentException(" Check Importance class")
                }
            }
    }
}

object LOW : Importance() {
    override fun intRep() = LOW_CODE
    override fun stringRep() = LOW_STR
}

object DEFAULT : Importance() {
    override fun intRep() = DEFAULT_CODE
    override fun stringRep() = DEFAULT_STR
}

object HIGH : Importance() {
    override fun intRep() = HIGH_CODE
    override fun stringRep() = HIGH_STR
}