#ifndef _AES_H_
#define _AES_H_

#include <stdint.h>
#include <stddef.h>

#define AES_BLOCKLEN 16 // 128-bit block

struct AES_ctx {
    uint8_t RoundKey[176]; // AES-128 only
    uint8_t Iv[AES_BLOCKLEN];
};

// Init functions
void AES_init_ctx(struct AES_ctx* ctx, const uint8_t* key);
void AES_init_ctx_iv(struct AES_ctx* ctx, const uint8_t* key, const uint8_t* iv);
void AES_ctx_set_iv(struct AES_ctx* ctx, const uint8_t* iv);

// CBC mode
void AES_CBC_encrypt_buffer(struct AES_ctx* ctx, uint8_t* buf, size_t length);
void AES_CBC_decrypt_buffer(struct AES_ctx* ctx, uint8_t* buf, size_t length);

#endif //_AES_H_
