package kotlinx.io.buffer

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BufferTest {

    @Test
    fun testUsePointer() {
        val array = ByteArray(10)
        val buffer = bufferOf(array, 1, 8)

        var executed = false

        buffer.usePointer { bufferPointer ->
            array.usePinned { arrayPointer ->
                executed = true
                assertEquals(arrayPointer.addressOf(1), bufferPointer)
            }
        }

        assertTrue(executed)
    }
}