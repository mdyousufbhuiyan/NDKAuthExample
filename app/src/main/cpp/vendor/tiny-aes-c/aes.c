#include "aes.h"
#include <string.h>

// Minimal AES implementation placeholders
// Replace with actual AES encrypt/decrypt in CBC mode for production

void AES_init_ctx(struct AES_ctx* ctx, const uint8_t* key) {
    // Copy the key into RoundKey for demo (replace with real key expansion)
    memcpy(ctx->RoundKey, key, 16);
}

void AES_ctx_set_iv(struct AES_ctx* ctx, const uint8_t* iv) {
    memcpy(ctx->Iv, iv, AES_BLOCKLEN);
}

void AES_init_ctx_iv(struct AES_ctx* ctx, const uint8_t* key, const uint8_t* iv) {
    AES_init_ctx(ctx, key);
    AES_ctx_set_iv(ctx, iv);
}

// Simple placeholder CBC encrypt: XOR with IV
void AES_CBC_encrypt_buffer(struct AES_ctx* ctx, uint8_t* buf, size_t length) {
    for (size_t i = 0; i < length && i < AES_BLOCKLEN; ++i) {
        buf[i] ^= ctx->Iv[i];
    }
}

void AES_CBC_decrypt_buffer(struct AES_ctx* ctx, uint8_t* buf, size_t length) {
    for (size_t i = 0; i < length && i < AES_BLOCKLEN; ++i) {
        buf[i] ^= ctx->Iv[i];
    }
}
