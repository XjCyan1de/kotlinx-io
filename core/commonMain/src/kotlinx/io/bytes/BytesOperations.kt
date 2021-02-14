package kotlinx.io.bytes

import kotlinx.io.Input
import kotlinx.io.buffer.DEFAULT_BUFFER_SIZE
import kotlinx.io.copyTo
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Create [BytesInput] with content from [block] using specified [bufferSize].
 */
fun buildInput(bufferSize: Int = DEFAULT_BUFFER_SIZE, block: BytesOutput.() -> Unit): BytesInput {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    return BytesOutput(bufferSize).apply {
        block()
    }.createInput()
}

/**
 * Read [BytesInput] of fixed [size].
 */
fun Input.readBytesInput(size: Int): BytesInput = buildInput {
    copyTo(this, size)
}