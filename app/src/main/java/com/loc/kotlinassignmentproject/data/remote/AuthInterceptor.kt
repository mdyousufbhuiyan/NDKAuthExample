package com.loc.kotlinassignmentproject.data.remote

import android.util.Base64
import android.util.Log
import com.loc.kotlinassignmentproject.data.crypto.CryptoStore
import okhttp3.Interceptor
import okhttp3.Response
import kotlin.math.log

/**
 * Interceptor that reads the API key (decrypted by EncryptedSharedPreferences)
 * and adds it as a header to outbound requests.
 */
class AuthInterceptor(private val cryptoStore: CryptoStore) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val key = cryptoStore.getString(KEY_NAME) ?: ""
        val encodedKey = Base64.encodeToString(
            key.toByteArray(Charsets.UTF_8),
            Base64.NO_WRAP
        )
        val request = original.newBuilder()
            .addHeader(
                API_HEADER_NAME, encodedKey
            )
            .build()


        //   val decryptedText = decryptAesEcb(key, aesKey)

        //  Log.e("DecryptedKey", "..............decryptedText...$decryptedText......'")
        Log.e(
            "AuthInterceptor",
            "..............ke...${key}......encodedKey.......${encodedKey}......."
        )
        return chain.proceed(request)
    }

    companion object {
        const val API_HEADER_NAME = "X-Api-Key"
        const val KEY_NAME = "api_key_plain_at_rest_encrypted"
    }
//
//    val aesKey = byteArrayOf(
//        0x60, 0x3d, 0xeb.toByte(), 0x10, 0x15, 0xca.toByte(), 0x71, 0xbe.toByte(),
//        0x2b, 0x73, 0xae.toByte(), 0xf0.toByte(), 0x85.toByte(), 0x7d, 0x77, 0x81.toByte()
//    )
//
//    fun hexToBytes(hex: String): ByteArray {
//        val len = hex.length
//        val result = ByteArray(len / 2)
//        for (i in 0 until len step 2) {
//            result[i / 2] = ((hex.substring(i, i + 2)).toInt(16)).toByte()
//        }
//        return result
//    }
//
//    fun decryptAesEcb(encryptedHex: String, key: ByteArray): String {
//        val cipher = javax.crypto.Cipher.getInstance("AES/ECB/NoPadding")
//        val secretKey = javax.crypto.spec.SecretKeySpec(key, "AES")
//        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, secretKey)
//
//        val encryptedBytes = hexToBytes(encryptedHex)
//        val decryptedBytes = cipher.doFinal(encryptedBytes)
//
//        return decryptedBytes.toString(Charsets.UTF_8).trim('\u0000')
//    }

}
