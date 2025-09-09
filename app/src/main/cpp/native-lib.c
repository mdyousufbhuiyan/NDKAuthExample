#include <jni.h>
#include <string.h>
#include <stdint.h>
#include <stdlib.h>
#include <android/log.h>
#include "aes.h"

#define AES_BLOCK_SIZE 16
#define LOG_TAG "NativeAES"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

// 128-bit AES key (16 bytes)
static const uint8_t aes_key[16] = {
        0x60, 0x3d, 0xeb, 0x10, 0x15, 0xca, 0x71, 0xbe,
        0x2b, 0x73, 0xae, 0xf0, 0x85, 0x7d, 0x77, 0x81
};

JNIEXPORT jstring JNICALL
Java_com_loc_kotlinassignmentproject_NativeLib_encryptApiKey(JNIEnv *env, jobject thiz, jstring apiKey) {
    const char *inputStr = (*env)->GetStringUTFChars(env, apiKey, 0);
    size_t len = strlen(inputStr);

    // Pad the input to be multiple of AES_BLOCK_SIZE
    size_t padded_len = ((len + AES_BLOCK_SIZE - 1) / AES_BLOCK_SIZE) * AES_BLOCK_SIZE;
    uint8_t *input = calloc(padded_len, sizeof(uint8_t));
    memcpy(input, inputStr, len);  // Copy original, padded with zeros

    // Initialize AES context
    struct AES_ctx ctx;
    AES_init_ctx(&ctx, aes_key);

    // Encrypt block-by-block
    for (size_t i = 0; i < padded_len; i += AES_BLOCK_SIZE) {
        AES_ECB_encrypt(&ctx, input + i);
    }

    // Convert encrypted data to hex string
    char *hexStr = calloc(padded_len * 2 + 1, sizeof(char));
    for (size_t i = 0; i < padded_len; i++) {
        sprintf(hexStr + i * 2, "%02x", input[i]);
    }

    (*env)->ReleaseStringUTFChars(env, apiKey, inputStr);
    free(input);

    jstring result = (*env)->NewStringUTF(env, hexStr);
    free(hexStr);

    return result;
}
