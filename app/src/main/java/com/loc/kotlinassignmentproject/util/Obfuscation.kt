package com.loc.kotlinassignmentproject.util

object Obfuscation {
    fun xorBytes(data: ByteArray, key: ByteArray): ByteArray {
        val out = ByteArray(data.size)
        for (i in data.indices) out[i] = (data[i].toInt() xor key[i % key.size].toInt()).toByte()
        return out
    }
}
