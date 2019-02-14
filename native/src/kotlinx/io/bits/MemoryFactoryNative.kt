@file:Suppress("NOTHING_TO_INLINE")

package kotlinx.io.bits

import kotlinx.cinterop.*
import platform.posix.*
import kotlin.contracts.*

/**
 * Execute [block] of code providing a temporary instance of [Memory] view of this byte array range
 * starting at the specified [offset] and having the specified bytes [length].
 * By default, if neither [offset] nor [length] specified, the whole array is used.
 * An instance of [Memory] provided into the [block] should be never captured and used outside of lambda.
 */
actual inline fun <R> ByteArray.useMemory(offset: Int, length: Int, block: (Memory) -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    return usePinned { pinned ->
        val memory = when {
            isEmpty() && offset == 0 && length == 0 -> Memory.Empty
            else -> Memory(pinned.addressOf(offset), length.toLong())
        }

        block(memory)
    }
}

/**
 * Create an instance of [Memory] view for memory region starting at
 * the specified [pointer] and having the specified [size] in bytes.
 */
inline fun Memory.Companion.of(pointer: CPointer<*>, size: size_t): Memory {
    require(size.convert<ULong>() <= Long.MAX_VALUE.convert<ULong>()) {
        "At most ${Long.MAX_VALUE} (kotlin.Long.MAX_VALUE) bytes range is supported."
    }

    return of(pointer, size.convert())
}

/**
 * Create an instance of [Memory] view for memory region starting at
 * the specified [pointer] and having the specified [size] in bytes.
 */
inline fun Memory.Companion.of(pointer: CPointer<*>, size: Int): Memory {
    return Memory(pointer.reinterpret(), size.toLong())
}

/**
 * Create an instance of [Memory] view for memory region starting at
 * the specified [pointer] and having the specified [size] in bytes.
 */
inline fun Memory.Companion.of(pointer: CPointer<*>, size: Long): Memory {
    return Memory(pointer.reinterpret(), size)
}

/**
 * Allocate memory range having the specified [size] in bytes and provide an instance of [Memory] view for this range.
 * Please note that depending of the placement type (e.g. scoped or global) this memory instance may require
 * explicit release using [free] on the same placement.
 * In particular, instances created inside of [memScoped] block do not require to be released explicitly but
 * once the scope is leaved, all produced instances should be discarded and should be never used after the scope.
 * On the contrary instances created using [nativeHeap] do require release via [nativeHeap.free].
 */
fun NativePlacement.allocMemory(size: Long): Memory {
    return Memory(allocArray(size), size)
}

/**
 * Release resources that the [memory] instance is holding.
 * This function should be only used for memory instances that are produced by [allocMemory] function
 * otherwise an undefined behaviour may occur including crash or data corruption.
 */
fun NativeFreeablePlacement.free(memory: Memory) {
    free(memory.pointer)
}

