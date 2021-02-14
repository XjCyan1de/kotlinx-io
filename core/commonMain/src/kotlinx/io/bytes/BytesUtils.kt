package kotlinx.io.bytes

import kotlinx.io.Input
import kotlinx.io.buffer.Buffer
import kotlinx.io.buffer.DEFAULT_BUFFER_SIZE
import kotlinx.io.buffer.DefaultBufferPool
import kotlinx.io.buffer.UnmanagedBufferPool
import kotlinx.io.pool.ObjectPool

internal fun poolOfBuffers(bufferSize: Int = DEFAULT_BUFFER_SIZE): ObjectPool<Buffer> =
    if (bufferSize == DEFAULT_BUFFER_SIZE) {
        DefaultBufferPool.Instance
    } else {
        DefaultBufferPool(bufferSize)
    }

internal fun unmanagedPoolOfBuffers(bufferSize: Int = DEFAULT_BUFFER_SIZE): ObjectPool<Buffer> =
    if (bufferSize == DEFAULT_BUFFER_SIZE) {
        UnmanagedBufferPool.Instance
    } else {
        UnmanagedBufferPool(bufferSize)
    }

internal fun Bytes.createInput(bufferSize: Int = DEFAULT_BUFFER_SIZE): Input = BytesInput(
    this, bufferSize
)