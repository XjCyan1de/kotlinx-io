package kotlinx.io

/**
 * Closeable resource.
 */
actual fun interface Closeable {
    actual fun close()
}

@PublishedApi
internal actual fun Throwable.addSuppressedInternal(other: Throwable) {
}
