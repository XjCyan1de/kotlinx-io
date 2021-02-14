package kotlinx.io

import kotlinx.io.internal.InputFromInputStream
import kotlinx.io.internal.InputStreamFromInput
import java.io.InputStream

/**
 * Returns an [InputStream] that uses the current [Input] as an underlying source of data.
 * Closing the resulting [InputStream] will close the input.
 */
fun Input.asInputStream(): InputStream = InputStreamFromInput(this)

/**
 * Returns an [Input] that uses the current [InputStream] as an underlying source of data.
 * Closing the resulting [Input] will close the input stream.
 */
fun InputStream.asInput(): Input = InputFromInputStream(this)
