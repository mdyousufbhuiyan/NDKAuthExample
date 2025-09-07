#include <jni.h>
#include <string>
#include <vector>
#include <fcntl.h>
#include <unistd.h>
#include <android/log.h>
extern "C" {
#include "vendor/tiny-aes-c/aes.h"
}
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,"NativeCrypto",__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,"NativeCrypto",__VA_ARGS__)

// ---- Key storage ----
// For demo: a 32-byte AES-256 key and a 16-byte salt baked in native.
// In production, you should:
// 1) Wrap this key with Android Keystore (AES key generated/stored under TEE).
// 2) Consider splitting key parts across native + remote derivation to reduce static extraction risk.
static const uint8_t MASTER_KEY[32] = {
        0x83,0x51,0x2A,0xD9,0x44,0xBE,0x10,0x7C,0xA1,0x9E,0x53,0x2F,0x6D,0xC4,0x38,0x99,
        0x11,0x2B,0x7E,0x4F,0x55,0x6A,0xE0,0x19,0x77,0xAF,0x02,0x3C,0x5D,0x8E,0xBA,0xCB
};

// PKCS7 padding for CBC
static std::vector<uint8_t> pkcs7_pad(const uint8_t* data, size_t len, size_t block=16) {
    size_t pad = block - (len % block);
    std::vector<uint8_t> out(len + pad);
    memcpy(out.data(), data, len);
    memset(out.data()+len, (int)pad, pad);
    return out;
}

// Generate 16B IV from /dev/urandom
static bool get_random_iv(uint8_t iv[16]) {
    int fd = open("/dev/urandom", O_RDONLY);
    if (fd < 0) return false;
    ssize_t r = read(fd, iv, 16);
    close(fd);
    return r == 16;
}

// JNI: encrypt (IV || AES-256-CBC(plaintext)), returns raw bytes
extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_loc_kotlinassignmentproject_crypto_NativeCrypto_encryptForAuthKey(
        JNIEnv* env, jobject , jbyteArray plaintext_) {
    jsize in_len = env->GetArrayLength(plaintext_);
    std::vector<uint8_t> in(in_len);
    env->GetByteArrayRegion(plaintext_, 0, in_len, reinterpret_cast<jbyte*>(in.data()));

    uint8_t iv[16];
    if (!get_random_iv(iv)) {
        LOGE("IV generation failed");
        return nullptr;
    }

    // pad
    auto padded = pkcs7_pad(in.data(), in.size(), 16);
    size_t blocks = padded.size() / 16;

    // CBC encrypt
    struct AES_ctx ctx;
    AES_init_ctx_iv(&ctx, MASTER_KEY, iv);

    // AES_CBC_encrypt_buffer mutates in-place
    AES_CBC_encrypt_buffer(&ctx, padded.data(), (uint32_t)padded.size());

    // output = IV || ciphertext
    std::vector<uint8_t> out(16 + padded.size());
    memcpy(out.data(), iv, 16);
    memcpy(out.data()+16, padded.data(), padded.size());

    jbyteArray arr = env->NewByteArray((jsize)out.size());
    env->SetByteArrayRegion(arr, 0, (jsize)out.size(), reinterpret_cast<jbyte*>(out.data()));
    return arr;
}
