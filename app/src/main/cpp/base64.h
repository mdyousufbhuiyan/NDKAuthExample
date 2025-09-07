#pragma once
#include <stddef.h>
#ifdef __cplusplus
extern "C" {
#endif

size_t b64_encoded_length(size_t in_len);
size_t b64_decoded_length(const char* in, size_t in_len);
int b64_encode(const unsigned char* in, size_t in_len, char* out, size_t out_cap);
int b64_decode(const char* in, size_t in_len, unsigned char* out, size_t out_cap);

#ifdef __cplusplus
}
#endif
