package ru.vkdev.greentest.hashfunction

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import java.io.ByteArrayInputStream

class HashFunctionTest {

    private fun String.hexToByteArray(): ByteArray {
        return chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    }

    private fun ByteArray.toInputStream() = ByteArrayInputStream(this)

    @Test
    fun md5_emptyInput_returnsCorrectHash() = runBlocking {
        val input = "".toByteArray().toInputStream()
        val expected = "d41d8cd98f00b204e9800998ecf8427e".hexToByteArray()
        assertArrayEquals(expected, HashFunction.md5()(input))
    }

    @Test
    fun md5_hello_returnsCorrectHash() = runBlocking {
        val input = "hello".toByteArray().toInputStream()
        val expected = "5d41402abc4b2a76b9719d911017c592".hexToByteArray()
        assertArrayEquals(expected, HashFunction.md5()(input))
    }

    @Test
    fun md5_returns16Bytes() = runBlocking {
        val input = "test".toByteArray().toInputStream()
        assertEquals(16, HashFunction.md5()(input).size)
    }

    @Test
    fun md5_differentInputs_produceDifferentHashes() = runBlocking {
        val hash1 = HashFunction.md5()("input1".toByteArray().toInputStream())
        val hash2 = HashFunction.md5()("input2".toByteArray().toInputStream())
        assertArrayEquals(hash1, hash1)
        assertFalse(hash1.contentEquals(hash2))
    }

    @Test
    fun sha256_emptyInput_returnsCorrectHash() = runBlocking {
        val input = "".toByteArray().toInputStream()
        val expected = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855".hexToByteArray()
        assertArrayEquals(expected, HashFunction.sha256()(input))
    }

    @Test
    fun sha256_hello_returnsCorrectHash() = runBlocking {
        val input = "hello".toByteArray().toInputStream()
        val expected = "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824".hexToByteArray()
        assertArrayEquals(expected, HashFunction.sha256()(input))
    }

    @Test
    fun sha256_returns32Bytes() = runBlocking {
        val input = "test".toByteArray().toInputStream()
        assertEquals(32, HashFunction.sha256()(input).size)
    }

    @Test
    fun sha256_differentInputs_produceDifferentHashes() = runBlocking {
        val hash1 = HashFunction.sha256()("input1".toByteArray().toInputStream())
        val hash2 = HashFunction.sha256()("input2".toByteArray().toInputStream())
        assertArrayEquals(hash1, hash1)
        assertFalse(hash1.contentEquals(hash2))
    }

    @Test
    fun sha256_sameInput_returnsSameHash() = runBlocking {
        val inputBytes = "consistent".toByteArray()
        val hash1 = HashFunction.sha256()(inputBytes.toInputStream())
        val hash2 = HashFunction.sha256()(inputBytes.toInputStream())
        assertArrayEquals(hash1, hash2)
    }

    @Test
    fun md5_sameInput_returnsSameHash() = runBlocking {
        val inputBytes = "consistent".toByteArray()
        val hash1 = HashFunction.md5()(inputBytes.toInputStream())
        val hash2 = HashFunction.md5()(inputBytes.toInputStream())
        assertArrayEquals(hash1, hash2)
    }
}
