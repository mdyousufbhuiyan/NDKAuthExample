#ifndef _AES_H_
#define _AES_H_

#include <stdint.h>

#define AES_BLOCKLEN 16 // AES block size in bytes

struct AES_ctx {
    uint8_t RoundKey[176];
};

void AES_init_ctx(struct AES_ctx* ctx, const uint8_t* key);
void AES_ECB_encrypt(const struct AES_ctx* ctx, uint8_t* buf);

#endif //_AES_H_
