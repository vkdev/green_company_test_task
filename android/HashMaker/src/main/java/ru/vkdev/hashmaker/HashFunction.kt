package ru.vkdev.hashmaker

import java.security.MessageDigest

sealed interface HashFunction {
    operator fun invoke(input: ByteArray): ByteArray
}

object MD5HashFunction: HashFunction {
    override fun invoke(input: ByteArray): ByteArray {
        return MessageDigest.getInstance("MD5").digest(input)
    }
}

object Sha256HashFunction: HashFunction {
    override fun invoke(input: ByteArray): ByteArray {
        return MessageDigest.getInstance("SHA-256").digest(input)
    }
}

// У нас могут быть и свои функции.
// Если мы хотим сделать мультиплатформу, то убираем зависимость от java