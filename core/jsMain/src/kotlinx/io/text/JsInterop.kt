package kotlinx.io.text

import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.ArrayBufferView

internal external class TextDecoder(encoding: String, options: dynamic = definedExternally) {
    val encoding: String

    fun decode(): String
    fun decode(buffer: ArrayBuffer): String
    fun decode(buffer: ArrayBuffer, options: dynamic): String
    fun decode(buffer: ArrayBufferView): String
    fun decode(buffer: ArrayBufferView, options: dynamic): String
}
