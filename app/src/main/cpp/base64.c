#include "base64.h"
#include <stdint.h>
#include <string.h>

static const char ENC[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
static int8_t DEC[256];

__attribute__((constructor))
static void init_dec() {
    for (int i=0;i<256;i++) DEC[i]=-1;
    for (int i=0;i<64;i++) DEC[(int)ENC[i]]=i;
    DEC[(int)'=']=0;
}

size_t b64_encoded_length(size_t in_len) { return ((in_len + 2) / 3) * 4; }

size_t b64_decoded_length(const char* in, size_t in_len) {
    size_t pad = 0;
    if (in_len >= 1 && in[in_len-1]=='=') pad++;
    if (in_len >= 2 && in[in_len-2]=='=') pad++;
    return (in_len/4)*3 - pad;
}

int b64_encode(const unsigned char* in, size_t in_len, char* out, size_t out_cap) {
    size_t need = b64_encoded_length(in_len);
    if (out_cap < need+1) return -1;
    size_t i=0, j=0;
    while (i < in_len) {
        uint32_t v = in[i++] << 16;
        if (i < in_len) v |= in[i++] << 8;
        if (i < in_len) v |= in[i++];
        out[j++] = ENC[(v >> 18) & 63];
        out[j++] = ENC[(v >> 12) & 63];
        out[j++] = (i-1 > in_len) ? '=' : ENC[(v >> 6) & 63];
        out[j++] = (i   > in_len) ? '=' : ENC[v & 63];
    }
    out[j] = '\0';
    return (int)j;
}

int b64_decode(const char* in, size_t in_len, unsigned char* out, size_t out_cap) {
    if (in_len % 4) return -1;
    size_t j=0;
    for (size_t i=0; i<in_len; i+=4) {
        int8_t a=DEC[(int)in[i]], b=DEC[(int)in[i+1]],
                c=DEC[(int)in[i+2]], d=DEC[(int)in[i+3]];
        if (a<0 || b<0 || c<0 || d<0) return -1;
        uint32_t v=(a<<18)|(b<<12)|(c<<6)|d;
        if (j+3 > out_cap) return -1;
        out[j++] = (v >> 16) & 0xFF;
        if (in[i+2] != '=') out[j++] = (v >> 8) & 0xFF;
        if (in[i+3] != '=') out[j++] = v & 0xFF;
    }
    return (int)j;
}
