package ru.vkdev.greentest.hashfunction

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.security.MessageDigest

sealed interface HashFunction {

    suspend operator fun invoke(stream: InputStream): ByteArray

    companion object {

        fun md5(): HashFunction = MD5HashFunction

        fun sha256(): HashFunction = Sha256HashFunction
    }
}

internal abstract class JavaSecurityHashFunction(private val algorithm: String) : HashFunction {

    override suspend fun invoke(stream: InputStream): ByteArray {
        val digest = MessageDigest.getInstance(algorithm)
        val buffer = ByteArray(64 * 1024)
        var bytesRead: Int
        while (stream.read(buffer).also { bytesRead = it } != -1) {
            currentCoroutineContext().ensureActive()
            withContext(Dispatchers.Default) {
                digest.update(buffer, 0, bytesRead)
            }
        }
        return digest.digest()
    }
}

internal object MD5HashFunction : JavaSecurityHashFunction("MD5")

internal object Sha256HashFunction : JavaSecurityHashFunction("SHA-256")

// У нас могут быть и свои функции.
// Если мы хотим сделать мультиплатформу, то убираем зависимость от java