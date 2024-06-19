package com.mariana.harmonia.utils

import java.security.MessageDigest

object HashUtils {
    fun sha512(input: String) = hashString("SHA-512", input)

    fun sha256(input: String) = hashString("SHA-256", input)

    fun sha1(input: String) = hashString("SHA-1", input)

    private fun hashString(type: String, input: String): String {
        val HEX_CHARS = "0123456789ABCDEF"
        val bytes = MessageDigest
            .getInstance(type)
            .digest(input.toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }

        return result.toString()
    }

    // Función para codificar una cadena a una cadena de números
    fun encodeStringToNumbers(s: String): String {
        return s.map { it.toInt().toString().padStart(3, '0') }.joinToString("")
    }

    // Función para decodificar una cadena de números a una cadena
    fun decodeNumbersToString(s: String): String {
        return s.chunked(3).map { it.toInt().toChar() }.joinToString("")
    }
}
