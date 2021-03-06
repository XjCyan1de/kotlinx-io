package kotlinx.io.bytes

import kotlinx.io.*
import kotlinx.io.text.readUtf8Line
import kotlinx.io.text.readUtf8String
import kotlinx.io.text.writeUtf8String
import kotlin.test.*

class BytesTest {
    private val bufferSizes = (1..64)

    @Test
    fun testEmptyBuildInput() {
        buildInput {
        }
    }

    @Test
    fun testEmptyCreateInput() {
        BytesOutput().createInput()
    }

    @Test
    fun testSmokeSingleBuffer() = bufferSizes.forEach { size ->
        val input = buildInput(size) {
            val array = ByteArray(2)
            array[0] = 0x11
            array[1] = 0x22
            writeByteArray(array)

            writeByte(0x12)
            writeUByte(0x82u)
            writeShort(0x3456)
            writeInt(0x789abcde)
            writeDouble(1.25)
            writeFloat(1.25f)
            writeLong(0x123456789abcdef0)
            writeLong(0x123456789abcdef0)

            writeUtf8String("OK\n")
        }

        assertEquals(2 + 2 + 2 + 4 + 8 + 4 + 8 + 8 + 3, input.remaining)

        val ba = ByteArray(2)
        input.readByteArray(ba)

        assertEquals(0x11, ba[0])
        assertEquals(0x22, ba[1])

        assertEquals(0x12, input.readByte())
        assertEquals(0x82u, input.readUByte())
        assertEquals(0x3456, input.readShort())
        assertEquals(0x789abcde, input.readInt())
        assertEquals(1.25, input.readDouble())
        assertEquals(1.25f, input.readFloat())

        val ll = (1..8).map { input.readByte().toInt() and 0xff }.joinToString()
        assertEquals("18, 52, 86, 120, 154, 188, 222, 240", ll)
        assertEquals(0x123456789abcdef0, input.readLong())

        assertEquals("OK", input.readUtf8Line())
    }

    @Test
    fun testSmokeMultiBuffer() {
        buildInput {
            writeByteArray(ByteArray(9999))
            writeByte(0x12)
            writeShort(0x1234)
            writeInt(0x12345678)
            writeDouble(1.25)
            writeFloat(1.25f)
            writeLong(0x123456789abcdef0)

            writeUtf8String("OK\n")
            val text = listOf(1, 2, 3).joinToString(separator = "|")
            writeUtf8String("$text\n")
        }.use {
            it.readByteArray(ByteArray(9999))
            assertEquals(0x12, it.readByte())
            assertEquals(0x1234, it.readShort())
            assertEquals(0x12345678, it.readInt())
            assertEquals(1.25, it.readDouble())
            assertEquals(1.25f, it.readFloat())
            assertEquals(0x123456789abcdef0, it.readLong())

            assertEquals("OK", it.readUtf8Line())
            assertEquals("1|2|3", it.readUtf8Line())
            assertTrue { it.exhausted() }
        }
    }

    @Test
    fun testSingleBufferSkipTooMuch() {
        buildInput {
            writeByteArray(ByteArray(9999))
        }.use { input ->
            input.readByteArray(ByteArray(9999))
            assertTrue { input.exhausted() }
        }
    }

    @Test
    fun testSingleBufferSkip() {
        buildInput {
            writeByteArray("ABC123\n".toByteArray0())
        }.use {
            it.readByteArray(ByteArray(3))
            assertEquals("123", it.readUtf8Line())
            assertTrue { it.exhausted() }
        }
    }

    @Test
    fun testSingleBufferSkipExact() {
        buildInput {
            writeByteArray("ABC123".toByteArray0())
        }.use {
            it.readByteArray(ByteArray(3))
            assertEquals("123", it.readUtf8String(3))
            assertTrue { it.exhausted() }
        }
    }

    @Test
    fun testSingleBufferSkipExactTooMuch() {
        buildInput {
            writeByteArray("ABC123".toByteArray0())
        }.apply {
            assertFailsWith<EOFException> {
                readByteArray(ByteArray(1000))
            }
            assertTrue { exhausted() }
        }
    }

    @Test
    fun testMultiBufferSkipTooMuch() {
        buildInput {
            writeByteArray(ByteArray(99999))
        }.apply {
            discardExact(99999)
            assertTrue { exhausted() }
        }

    }

    @Test
    fun testMultiBufferSkip() {
        buildInput {
            writeByteArray(ByteArray(99999))
            writeByteArray("ABC123\n".toByteArray0())
        }.apply {
            assertEquals(99999 + 7, remaining)
            readByteArray(ByteArray(99999 + 3))
            assertEquals(4, remaining)
            assertEquals("123", readUtf8Line())
            assertEquals(0, remaining)
            assertTrue { exhausted() }
        }
    }

    @Test
    fun testNextBufferBytesStealing() {
        buildInput {
            repeat(PACKET_BUFFER_SIZE + 3) {
                writeByte(1)
            }
        }.apply {
            readByteArray(ByteArray(PACKET_BUFFER_SIZE - 1))
            assertEquals(0x01010101, readInt())
            assertTrue { exhausted() }
        }
    }

    @Test
    fun testNextBufferBytesStealingFailed() {
        buildInput {
            repeat(PACKET_BUFFER_SIZE + 1) {
                writeByte(1)
            }
        }.apply {
            readByteArray(ByteArray(PACKET_BUFFER_SIZE - 1))

            try {
                readInt()
                fail()
            } catch (_: EOFException) {
            }
        }
    }

    @Test
    fun testReadByteEmptyPacket() {
        assertFailsWith<EOFException> {
            buildInput { }.apply {
                readInt()
            }
        }

        assertFailsWith<EOFException> {
            buildInput {
                writeInt(1)
            }.apply {
                readInt()
                readByte()
            }
        }
    }

    private fun String.toByteArray0(): ByteArray {
        val result = ByteArray(length)

        for (i in 0 until length) {
            val v = this[i].toInt() and 0xff
            if (v > 0x7f) fail()
            result[i] = v.toByte()
        }

        return result
    }

    companion object {
        const val PACKET_BUFFER_SIZE: Int = 4096
    }
}


