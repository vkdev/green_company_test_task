package ru.vkdev.greentest.hashfunction

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class HashFunctionTest {

    private fun String.hexToByteArray(): ByteArray {
        return chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    }

    @Test
    fun md5_emptyInput_returnsCorrectHash() {
        val input = "".toByteArray()
        val expected = "d41d8cd98f00b204e9800998ecf8427e".hexToByteArray()
        assertArrayEquals(expected, MD5HashFunction(input))
    }

    @Test
    fun md5_hello_returnsCorrectHash() {
        val input = "hello".toByteArray()
        val expected = "5d41402abc4b2a76b9719d911017c592".hexToByteArray()
        assertArrayEquals(expected, MD5HashFunction(input))
    }

    @Test
    fun md5_returns16Bytes() {
        val input = "test".toByteArray()
        assertEquals(16, MD5HashFunction(input).size)
    }

    @Test
    fun md5_differentInputs_produceDifferentHashes() {
        val hash1 = MD5HashFunction("input1".toByteArray())
        val hash2 = MD5HashFunction("input2".toByteArray())
        assertArrayEquals(hash1, hash1)
        assertFalse(hash1.contentEquals(hash2))
    }

    @Test
    fun sha256_emptyInput_returnsCorrectHash() {
        val input = "".toByteArray()
        val expected = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855".hexToByteArray()
        assertArrayEquals(expected, Sha256HashFunction(input))
    }

    @Test
    fun sha256_hello_returnsCorrectHash() {
        val input = "hello".toByteArray()
        val expected = "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824".hexToByteArray()
        assertArrayEquals(expected, Sha256HashFunction(input))
    }

    @Test
    fun sha256_returns32Bytes() {
        val input = "test".toByteArray()
        assertEquals(32, Sha256HashFunction(input).size)
    }

    @Test
    fun sha256_differentInputs_produceDifferentHashes() {
        val hash1 = Sha256HashFunction("input1".toByteArray())
        val hash2 = Sha256HashFunction("input2".toByteArray())
        assertArrayEquals(hash1, hash1)
        assertFalse(hash1.contentEquals(hash2))
    }

    @Test
    fun sha256_sameInput_returnsSameHash() {
        val input = "consistent".toByteArray()
        assertArrayEquals(Sha256HashFunction(input), Sha256HashFunction(input))
    }

    @Test
    fun md5_sameInput_returnsSameHash() {
        val input = "consistent".toByteArray()
        assertArrayEquals(MD5HashFunction(input), MD5HashFunction(input))
    }
}
