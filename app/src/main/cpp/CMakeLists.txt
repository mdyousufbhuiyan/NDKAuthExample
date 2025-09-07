cmake_minimum_required(VERSION 3.22.1)

project("kotlinassignmentproject")

add_library(
        native-crypto
        SHARED
        native-crypto.cpp
        base64.c
        vendor/tiny-aes-c/aes.c
)

target_include_directories(native-crypto PRIVATE
        ${CMAKE_CURRENT_SOURCE_DIR}
        ${CMAKE_CURRENT_SOURCE_DIR}/vendor/tiny-aes-c
)

find_library(log-lib log)

target_link_libraries(
        native-crypto
        ${log-lib}
)
