package kotlinx.io.text

import kotlinx.io.ByteArrayInput
import kotlinx.io.buffer.DEFAULT_BUFFER_SIZE
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class LinesTest {

    @Test
    fun testLines() {
        testLines("first\nsecond", listOf("first", "second"))
        testLines("first\nsecond\n", listOf("first", "second"))
        testLines("first\r\nsecond", listOf("first", "second"))
        testLines("first\nsecond\r\n", listOf("first", "second"))
    }

    @Test
    fun testBufferBound() {
        val hugeString = "a".repeat(DEFAULT_BUFFER_SIZE + 1)
        val hugeString2 = "b".repeat(DEFAULT_BUFFER_SIZE + 1)
        testLines(hugeString + "\n" + hugeString2, listOf(hugeString, hugeString2))
    }

    @Test
    fun testLineSeparatorOnBufferBound() {
        val hugeString = "a".repeat(DEFAULT_BUFFER_SIZE - 1)
        testLines("$hugeString\r\ncontent", listOf(hugeString, "content"))
    }

    @Test
    fun testEmptyStringBefore() {
        testLines("\r\nabc", listOf("", "abc"))
        testLines("\nabc", listOf("", "abc"))
        testLines("\n\nabc", listOf("", "", "abc"))
    }

    @Test
    fun testEmptyInput() {
        val input = ByteArrayInput(ByteArray(0))
        input.forEachUtf8Line {
            fail() // Unreached
        }
    }

    private fun testLines(input: String, expectedLines: List<String>) {
        val source = input.encodeToByteArray()
        testLineByLine(source, expectedLines)
        testReadLines(source, expectedLines)
        testForEachLine(source, expectedLines)
    }

    private fun testLineByLine(data: ByteArray, expectedLines: List<String>) {
        val input = ByteArrayInput(data)
        val result = ArrayList<String>()
        while (!input.exhausted()) {
            result += input.readUtf8Line()
        }
        assertEquals(expectedLines, result)
    }

    private fun testReadLines(data: ByteArray, expectedLines: List<String>) {
        val input = ByteArrayInput(data)
        val result = input.readUtf8Lines()
        assertEquals(expectedLines, result)
    }

    private fun testForEachLine(data: ByteArray, expectedLines: List<String>) {
        val input = ByteArrayInput(data)
        val result = ArrayList<String>()
        input.forEachUtf8Line {
            result += it
        }
        assertEquals(expectedLines, result)
    }
}
