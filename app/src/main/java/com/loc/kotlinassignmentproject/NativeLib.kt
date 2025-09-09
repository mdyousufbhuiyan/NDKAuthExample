package com.loc.kotlinassignmentproject

object NativeLib {
    init {
        System.loadLibrary("native-lib")
    }

    external fun encryptApiKey(apiKey: String): String

    /**
     * Example assembly scheme: parts returned from native are reversed strings to
     * make static analysis a bit harder. We reverse them back and assemble with salt.
     */
//    fun assembleRawApiKey(): String {
//        val p1 = StringBuilder(apiKeyPart1()).reverse().toString()
//        val p2 = StringBuilder(apiKeyPart2()).reverse().toString()
//        val salt = apiKeySalt().reversed()
//        return encryptedApiKey();
//    }
}
