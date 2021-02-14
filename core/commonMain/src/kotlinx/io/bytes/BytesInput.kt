package kotlinx.io.bytes

import kotlinx.io.Input
import kotlinx.io.buffer.Buffer
import kotlinx.io.buffer.DEFAULT_BUFFER_SIZE

/**
 * In memory byte source created from [BytesOutput] or by using [buildInput].
 *
 * [BytesInput] isn't pooled so it's safe to throw it away.
 */
class BytesInput internal constructor(
    bytes: Bytes,
    bufferSize: Int = DEFAULT_BUFFER_SIZE
) : Input(bytes, unmanagedPoolOfBuffers(bufferSize)) {

    /**
     * Count of remaining bytes in [BytesInput].
     */
    val remaining: Int
        get() = remainingCacheSize()

    override fun fill(buffer: Buffer, startIndex: Int, endIndex: Int): Int {
        return 0
    }

    override fun closeSource() {
    }
}

internal fun Bytes.createInput(): BytesInput = BytesInput(this)