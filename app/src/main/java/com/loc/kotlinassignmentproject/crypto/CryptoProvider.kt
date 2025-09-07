package com.loc.kotlinassignmentproject.crypto

object CryptoProvider {
    init {
        System.loadLibrary("native-crypto") // Load your .so
    }

    val engine = NativeCrypto()
}
