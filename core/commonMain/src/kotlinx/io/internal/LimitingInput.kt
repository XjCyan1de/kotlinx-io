package kotlinx.io.internal

import kotlinx.io.Input
import kotlinx.io.buffer.Buffer
import kotlin.math.min

internal class LimitingInput(private val original: Input, private var bytesLeft: Long) : Input() {

    override fun fill(buffer: Buffer, startIndex: Int, endIndex: Int): Int {
        if (bytesLeft <= 0L) return 0

        val space = endIndex - startIndex
        val count = min(space.toLong(), bytesLeft).toInt()
        val result = original.readAvailableTo(buffer, startIndex, startIndex + count)
        bytesLeft -= result
        return result
    }

    override fun closeSource() = original.close()
}
