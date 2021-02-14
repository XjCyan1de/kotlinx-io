package kotlinx.io

import kotlinx.io.buffer.Buffer
import kotlinx.io.buffer.DefaultBufferPool
import kotlinx.io.pool.ObjectPool

class LambdaOutput(
    pool: ObjectPool<Buffer> = DefaultBufferPool.Instance,
    private val block: (source: Buffer, startIndex: Int, endIndex: Int) -> Unit
) : Output(pool) {
    override fun flush(source: Buffer, startIndex: Int, endIndex: Int): Unit = block(source, startIndex, endIndex)

    override fun closeSource() {}
}