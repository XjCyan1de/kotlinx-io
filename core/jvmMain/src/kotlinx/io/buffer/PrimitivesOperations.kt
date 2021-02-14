@file:Suppress("NOTHING_TO_INLINE")

package kotlinx.io.buffer

import kotlinx.io.internal.toIntOrFail

actual inline fun Buffer.loadByteAt(index: Long): Byte = loadByteAt(index.toIntOrFail("index"))

actual inline fun Buffer.loadShortAt(offset: Int): Short {
    return buffer.getShort(offset)
}

actual inline fun Buffer.loadShortAt(offset: Long): Short {
    return buffer.getShort(offset.toIntOrFail("offset"))
}

actual inline fun Buffer.loadIntAt(offset: Int): Int {
    return buffer.getInt(offset)
}

actual inline fun Buffer.loadIntAt(offset: Long): Int {
    return buffer.getInt(offset.toIntOrFail("offset"))
}

actual inline fun Buffer.loadLongAt(offset: Int): Long {
    return buffer.getLong(offset)
}

actual inline fun Buffer.loadLongAt(offset: Long): Long {
    return buffer.getLong(offset.toIntOrFail("offset"))
}

actual inline fun Buffer.loadFloatAt(offset: Int): Float {
    return buffer.getFloat(offset)
}

actual inline fun Buffer.loadFloatAt(offset: Long): Float {
    return buffer.getFloat(offset.toIntOrFail("offset"))
}

actual inline fun Buffer.loadDoubleAt(offset: Int): Double {
    return buffer.getDouble(offset)
}

actual inline fun Buffer.loadDoubleAt(offset: Long): Double {
    return buffer.getDouble(offset.toIntOrFail("offset"))
}

actual inline fun Buffer.storeIntAt(offset: Int, value: Int) {
    buffer.putInt(offset, value)
}

actual inline fun Buffer.storeIntAt(offset: Long, value: Int) {
    storeIntAt(offset.toIntOrFail("offset"), value)
}

actual inline fun Buffer.storeShortAt(offset: Int, value: Short) {
    buffer.putShort(offset, value)
}

actual inline fun Buffer.storeShortAt(offset: Long, value: Short) {
    storeShortAt(offset.toIntOrFail("offset"), value)
}

actual inline fun Buffer.storeLongAt(offset: Int, value: Long) {
    buffer.putLong(offset, value)
}

actual inline fun Buffer.storeLongAt(offset: Long, value: Long) {
    storeLongAt(offset.toIntOrFail("offset"), value)
}

actual inline fun Buffer.storeFloatAt(offset: Int, value: Float) {
    buffer.putFloat(offset, value)
}

actual inline fun Buffer.storeFloatAt(offset: Long, value: Float) {
    storeFloatAt(offset.toIntOrFail("offset"), value)
}

actual inline fun Buffer.storeDoubleAt(offset: Int, value: Double) {
    buffer.putDouble(offset, value)
}

actual inline fun Buffer.storeDoubleAt(offset: Long, value: Double) {
    storeDoubleAt(offset.toIntOrFail("offset"), value)
}

actual inline fun Buffer.storeByteAt(index: Long, value: Byte): Unit =
    storeByteAt(index.toIntOrFail("index"), value)
