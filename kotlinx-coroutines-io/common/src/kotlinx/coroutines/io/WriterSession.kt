package kotlinx.coroutines.io

import kotlinx.io.bits.Memory
import kotlinx.io.core.*
import kotlinx.io.core.internal.ChunkBuffer

/**
 * Await for [desiredSpace] will be available for write and invoke [block] function providing [Memory] instance and
 * the corresponding range suitable for wiring in the memory. The block function should return number of bytes were
 * written, possibly 0.
 *
 * Similar to [ByteReadChannel.read], this function may invoke block function with lesser memory range when the
 * specified [desiredSpace] is bigger that the buffer's capacity
 * or when it is impossible to represent all [desiredSpace] bytes as a single memory range
 * due to internal implementation reasons.
 */
@ExperimentalIoApi
suspend inline fun ByteWriteChannel.write(
    desiredSpace: Int = 1,
    block: (freeSpace: Memory, startOffset: Long, endExclusive: Long) -> Int
): Int {
    val buffer = requestWriteBuffer(desiredSpace) ?: Buffer.Empty
    var bytesWritten = 0
    try {
        bytesWritten = block(buffer.memory, buffer.writePosition.toLong(), buffer.limit.toLong())
        buffer.commitWritten(bytesWritten)
        return bytesWritten
    } finally {
        completeWriting(buffer, bytesWritten)
    }
}

@Suppress("DEPRECATION")
@Deprecated("Use writeMemory instead.")
interface WriterSession {
    fun request(min: Int): IoBuffer?
    fun written(n: Int)
    fun flush()
}

@Suppress("DEPRECATION")
@Deprecated("Use writeMemory instead.")
interface WriterSuspendSession : WriterSession {
    suspend fun tryAwait(n: Int)
}

@Suppress("DEPRECATION")
internal interface HasWriteSession {
    fun beginWriteSession(): WriterSuspendSession?
    fun endWriteSession(written: Int)
}

@PublishedApi
internal suspend fun ByteWriteChannel.requestWriteBuffer(desiredSpace: Int): Buffer? {
    val session = writeSessionFor()
    if (session != null) {
        val buffer = session.request(desiredSpace)
        if (buffer != null) {
            return buffer
        }

        return writeBufferSuspend(session, desiredSpace)
    }

    return writeBufferFallback(desiredSpace)
}

@PublishedApi
internal suspend fun ByteWriteChannel.completeWriting(buffer: Buffer, written: Int) {
    if (this is HasWriteSession) {
        endWriteSession(written)
        return
    }

    return completeWritingFallback(buffer)
}

@Suppress("DEPRECATION")
private suspend fun ByteWriteChannel.completeWritingFallback(buffer: Buffer) {
    if (buffer is IoBuffer) {
        writeFully(buffer)
        buffer.release(IoBuffer.Pool)
        return
    }

    throw UnsupportedOperationException("Only IoBuffer instance is supported.")
}

@Suppress("DEPRECATION")
private suspend fun ByteWriteChannel.writeBufferSuspend(session: WriterSuspendSession, desiredSpace: Int): Buffer? {
    session.tryAwait(desiredSpace)
    return session.request(desiredSpace) ?: session.request(1)
}

private fun ByteWriteChannel.writeBufferFallback(desiredSpace: Int): Buffer? {
    return ChunkBuffer.Pool.borrow().also { it.resetForWrite(); it.reserveEndGap(Buffer.ReservedSize) }
}

@Suppress("DEPRECATION", "NOTHING_TO_INLINE")
private inline fun ByteWriteChannel.writeSessionFor(): WriterSuspendSession? = when {
    this is HasWriteSession -> beginWriteSession()
    else -> null
}

