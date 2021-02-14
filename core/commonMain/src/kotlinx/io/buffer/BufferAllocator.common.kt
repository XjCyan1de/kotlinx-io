package kotlinx.io.buffer

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

interface BufferAllocator {
    fun allocate(size: Int): Buffer
    fun free(instance: Buffer)
}

expect object PlatformBufferAllocator : BufferAllocator

/**
 * Allocates the buffer of the given [size], executes the given [block] function on this buffer
 * and then frees it correctly whether an exception is thrown or not.
 */
inline fun <T> BufferAllocator.borrow(size: Int, block: (buffer: Buffer) -> T): T {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }

    val buffer = allocate(size)

    try {
        return block(buffer)
    } finally {
        free(buffer)
    }
}