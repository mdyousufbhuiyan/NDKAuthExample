............How it works (brief)...........

Kotlin constructs a small plaintext payload (epoch:METHOD:/path).
JNI passes plaintext to C.

C:
Generates a random 16-byte IV from /dev/urandom.
Pads plaintext with PKCS#7.

Encrypts with AES-256-CBC using a 32-byte key stored in native code.
Returns raw bytes IV || ciphertext.
Kotlin Base64-encodes those bytes → AuthKey.
OkHttp Interceptor injects:
AuthKey: <base64(IV||CT)>
Server (your side) Base64-decodes, splits IV/ciphertext, and decrypts with the same key.


...............how to run this project ...............>

just clone this project into your andorid studio and run
