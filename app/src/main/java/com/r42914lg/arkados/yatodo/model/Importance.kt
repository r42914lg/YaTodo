package com.r42914lg.arkados.yatodo.model

import java.lang.IllegalArgumentException

sealed class Importance  {
    abstract fun intRep() : Int

    companion object {
        const val LOW_CODE = 0
        const val DEFAULT_CODE = 1
        const val HIGH_CODE = 2
    }

    class Factory() {
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
}

object DEFAULT : Importance() {
    override fun intRep() = DEFAULT_CODE
}

object HIGH : Importance() {
    override fun intRep() = HIGH_CODE
}