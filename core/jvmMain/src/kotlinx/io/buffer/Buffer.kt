@file:Suppress("NOTHING_TO_INLINE")

package kotlinx.io.buffer

import java.nio.ByteBuffer
import java.nio.ByteOrder

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual inline class Buffer(val buffer: ByteBuffer) {
    actual inline val size: Int get() = buffer.limit()

    actual inline fun loadByteAt(index: Int): Byte = buffer.get(index)

    actual inline fun storeByteAt(index: Int, value: Byte) {
        buffer.put(index, value)
    }

    actual companion object {
        actual val EMPTY: Buffer = Buffer(ByteBuffer.allocate(0).order(ByteOrder.BIG_ENDIAN))
    }
}

/**
 * Wrap [array] into [Buffer] from [startIndex] to [endIndex].
 */
internal actual fun bufferOf(array: ByteArray, startIndex: Int, endIndex: Int): Buffer {
    return Buffer(ByteBuffer.wrap(array, startIndex, endIndex - startIndex))
}

internal actual fun Buffer.sameAs(other: Buffer): Boolean = buffer === other.buffer