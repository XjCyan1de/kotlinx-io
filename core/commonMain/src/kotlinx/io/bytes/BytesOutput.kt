package kotlinx.io.bytes

import kotlinx.io.Output
import kotlinx.io.buffer.Buffer
import kotlinx.io.buffer.DEFAULT_BUFFER_SIZE
import kotlinx.io.buffer.compact

/**
 * Create unlimited [Output] stored in memory.
 * In advance to [Output] you can check [size] and create [BytesInput] with [createInput].
 * [BytesOutput] isn't using any pools and shouldn't be closed.
 */
class BytesOutput(
    private val bufferSize: Int = DEFAULT_BUFFER_SIZE
) : Output(unmanagedPoolOfBuffers(bufferSize)) {
    private val bytes = Bytes()

    /**
     * Size of content.
     */
    val size: Int get() = bytes.size() + size()

    /**
     * Create [BytesInput] from this [Output].
     * This can be called multiple times and. It always returns independent [BytesInput] without copying of underline buffers.
     * The buffers will be copied on demand.
     *
     * [BytesOutput] is safe to append content after [createInput]. It won't change any already created [BytesInput].
     */
    fun createInput(): BytesInput {
        if (size() > 0) {
            flush()
        }

        return BytesInput(bytes.snapshot(), bufferSize)
    }

    override fun flush(source: Buffer, startIndex: Int, endIndex: Int) {
        if (startIndex > 0) {
            source.compact(startIndex, endIndex)
        }
        bytes.append(source, endIndex)
    }

    override fun flushConsuming(source: Buffer, startIndex: Int, endIndex: Int): Boolean {
        flush(source, startIndex, endIndex)
        return true
    }

    override fun closeSource() {
        // No source to close.
    }
}
