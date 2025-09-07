package com.loc.kotlinassignmentproject.crypto

class NativeCrypto {
    external fun encryptForAuthKey(plainText: ByteArray): ByteArray
}
