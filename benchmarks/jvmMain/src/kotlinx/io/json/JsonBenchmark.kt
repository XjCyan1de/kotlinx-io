package kotlinx.io.json

import kotlinx.benchmark.*
import kotlinx.io.*
import kotlinx.io.json.data.*
import kotlinx.io.json.utils.*
import kotlinx.serialization.*
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.Setup
import java.util.concurrent.*
import kotlin.reflect.*

/**
 * ./gradlew :kotlinx-io-benchmarks:jvmBenchmark -PbenchmarkName="JsonBenchmark"
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@OptIn(ExperimentalStdlibApi::class, ImplicitReflectionSerializer::class)
class JsonBenchmark {
    @Param("canada", "citm", "twitter")
    var datasetName: String = ""

    // Enable to full comparison
    @Param("io"/*, "default", "gson"*/)
    var jsonName: String = ""

    private lateinit var data: Any
    private lateinit var content: String
    private lateinit var json: JsonAdapter
    private lateinit var type: KType
    private lateinit var clazz: Class<*>

    class Dataset(val content: String, val type: KType, val clazz: Class<*>, val data: Any)

    @Setup
    fun setup() {
        val dataset = JsonStreamBenchmark.datasets[datasetName]!!

        json = JsonStreamBenchmark.jsons[jsonName]!!
        content = dataset.content
        type = dataset.type
        clazz = dataset.clazz
        data = dataset.data
    }

    /**
     * Canada
     *
     * kotlinx.io.json.JsonBenchmark.benchmarkRead | datasetName=canada, jsonName=io
     * Success: 59.194 ±(99.9%) 0.238 ms/op [Average]
     * Success: 52.861 ±(99.9%) 1.172 ms/op [Average]
     * Success: 46.614 ±(99.9%) 1.863 ms/op [Average]
     * Success: 45.039 ±(99.9%) 0.309 ms/op [Average]
     *
     * kotlinx.io.json.JsonBenchmark.benchmarkRead | datasetName=canada, jsonName=default
     * Success: 35.438 ±(99.9%) 3.842 ms/op [Average]
     *
     * kotlinx.io.json.JsonBenchmark.benchmarkRead | datasetName=canada, jsonName=gson
     * Success: 38.080 ±(99.9%) 2.733 ms/op [Average]
     *
     * Citm
     *
     * kotlinx.io.json.JsonBenchmark.benchmarkRead | datasetName=citm, jsonName=io
     * Success: 23.625 ±(99.9%) 3.009 ms/op [Average]
     * Success: 16.571 ±(99.9%) 0.356 ms/op [Average]
     * Success: 17.569 ±(99.9%) 0.307 ms/op [Average]
     * Success: 16.536 ±(99.9%) 1.264 ms/op [Average]
     *
     * kotlinx.io.json.JsonBenchmark.benchmarkRead | datasetName=citm, jsonName=default
     * Success: 5.133 ±(99.9%) 0.358 ms/op [Average]
     *
     * kotlinx.io.json.JsonBenchmark.benchmarkRead | datasetName=citm, jsonName=gson
     * Success: 4.566 ±(99.9%) 0.332 ms/op [Average]
     *
     * Twitter
     *
     * kotlinx.io.json.JsonBenchmark.benchmarkRead | datasetName=twitter, jsonName=io
     * Success: 9.468 ±(99.9%) 0.629 ms/op [Average]
     * Success: 8.097 ±(99.9%) 0.488 ms/op [Average]
     * Success: 8.134 ±(99.9%) 0.329 ms/op [Average]
     * Success: 7.305 ±(99.9%) 0.728 ms/op [Average]
     *
     * kotlinx.io.json.JsonBenchmark.benchmarkRead | datasetName=twitter, jsonName=default
     * Success: 2.731 ±(99.9%) 0.206 ms/op [Average]
     *
     * kotlinx.io.json.JsonBenchmark.benchmarkRead | datasetName=twitter, jsonName=gson
     * Success: 2.071 ±(99.9%) 0.105 ms/op [Average]
     */
    @Benchmark
    fun benchmarkRead() = json.parse(content, type, clazz)

    /**
     * Canada
     *
     * kotlinx.io.json.JsonBenchmark.benchmarkWrite | datasetName=canada, jsonName=io
     * Success: 47.396 ±(99.9%) 4.178 ms/op [Average]
     * Success: 51.640 ±(99.9%) 2.426 ms/op [Average]
     * Success: 48.465 ±(99.9%) 1.109 ms/op [Average]
     * Success: 50.734 ±(99.9%) 2.778 ms/op [Average]
     *
     * kotlinx.io.json.JsonBenchmark.benchmarkWrite | datasetName=canada, jsonName=default
     * Success: 39.998 ±(99.9%) 1.937 ms/op [Average]
     *
     * kotlinx.io.json.JsonBenchmark.benchmarkWrite | datasetName=canada, jsonName=gson
     * Success: 41.044 ±(99.9%) 0.419 ms/op [Average]
     *
     * Citm
     *
     * kotlinx.io.json.JsonBenchmark.benchmarkWrite | datasetName=citm, jsonName=io
     * Success: 12.392 ±(99.9%) 0.769 ms/op [Average]
     * Success: 14.929 ±(99.9%) 0.237 ms/op [Average]
     * Success: 6.167 ±(99.9%) 0.075 ms/op [Average]
     * Success: 6.099 ±(99.9%) 0.226 ms/op [Average]
     *
     * kotlinx.io.json.JsonBenchmark.benchmarkWrite | datasetName=citm, jsonName=default
     * Success: 2.824 ±(99.9%) 0.332 ms/op [Average]
     *
     * kotlinx.io.json.JsonBenchmark.benchmarkWrite | datasetName=citm, jsonName=gson
     * Success: 4.598 ±(99.9%) 0.452 ms/op [Average]
     *
     * Twitter
     *
     * kotlinx.io.json.JsonBenchmark.benchmarkWrite | datasetName=twitter, jsonName=io
     * Success: 10.641 ±(99.9%) 0.565 ms/op [Average]
     * Success: 12.278 ±(99.9%) 0.757 ms/op [Average]
     * Success: 4.640 ±(99.9%) 0.049 ms/op [Average]
     * Success: 5.408 ±(99.9%) 0.288 ms/op [Average]
     *
     * kotlinx.io.json.JsonBenchmark.benchmarkWrite | datasetName=twitter, jsonName=default
     * Success: 1.470 ±(99.9%) 0.095 ms/op [Average]
     *
     * kotlinx.io.json.JsonBenchmark.benchmarkWrite | datasetName=twitter, jsonName=gson
     * Success: 2.445 ±(99.9%) 0.209 ms/op [Average]
     */
    @Benchmark
    fun benchmarkWrite() = json.encode(data, type)

    companion object {
        val canada = Resource("canada.json").readText()
        val citmCatalog = Resource("citm_catalog.json").readText()
        val twitter = Resource("twitter.json").readText()

        val jsons = mapOf(
            "io" to KxJson,
            "default" to SerializationJson,
            "gson" to GsonJson
        )

        val canadaObject = ioJson.parse<Canada>(canada)
        val citmObject = ioJson.parse<CitmCatalog>(citmCatalog)
        val twitterObject = ioJson.parse<Twitter>(twitter)

        val datasets = mapOf(
            "canada" to Dataset(canada, typeOf<Canada>(), Canada::class.java, canadaObject),
            "citm" to Dataset(citmCatalog, typeOf<CitmCatalog>(), CitmCatalog::class.java, citmObject),
            "twitter" to Dataset(twitter, typeOf<Twitter>(), Twitter::class.java, twitterObject)
        )
    }
}
