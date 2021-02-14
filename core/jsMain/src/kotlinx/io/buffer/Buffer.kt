@file:Suppress("NOTHING_TO_INLINE")

package kotlinx.io.buffer

import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.DataView
import org.khronos.webgl.Int8Array

actual class Buffer(val view: DataView) {

    actual inline val size: Int get() = view.byteLength

    actual fun loadByteAt(index: Int): Byte = checked(index) {
        return view.getInt8(index)
    }

    actual fun storeByteAt(index: Int, value: Byte) = checked(index) {
        view.setInt8(index, value)
    }

    actual companion object {
        actual val EMPTY: Buffer = Buffer(DataView(ArrayBuffer(0)))
    }
}

/**
 * Wrap [array] into [Buffer] from [startIndex] to [endIndex].
 */
internal actual fun bufferOf(array: ByteArray, startIndex: Int, endIndex: Int): Buffer {
    val content = array as Int8Array
    val view = DataView(
        content.buffer, content.byteOffset + startIndex, endIndex - startIndex
    )
    return Buffer(view)
}

internal actual fun Buffer.sameAs(other: Buffer): Boolean = view == other.view