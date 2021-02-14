package kotlinx.io

import kotlinx.io.internal.OutputFromOutputStream
import kotlinx.io.internal.OutputStreamFromOutput
import java.io.OutputStream

/**
 * Returns an [OutputStream] that uses the current [Output] as the destination.
 * Closing the resulting [OutputStream] will close the input.
 */
fun Output.asOutputStream(): OutputStream = OutputStreamFromOutput(this)

/**
 * Returns an [Output] that uses the current [OutputStream] as the destination.
 * Closing the resulting [Output] will close the input stream.
 */
fun OutputStream.asOutput(): Output = OutputFromOutputStream(this)
