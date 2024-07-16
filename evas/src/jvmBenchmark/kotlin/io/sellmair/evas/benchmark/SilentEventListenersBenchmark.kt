@file:Suppress("unused")

package io.sellmair.evas.benchmark

import io.sellmair.evas.Event
import io.sellmair.evas.Events
import io.sellmair.evas.collectEventsAsync
import io.sellmair.evas.emit
import kotlinx.benchmark.*
import kotlinx.coroutines.*

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
@Warmup(iterations = 10, time = 500, timeUnit = BenchmarkTimeUnit.MILLISECONDS)
@Measurement(iterations = 20, time = 1, timeUnit = BenchmarkTimeUnit.SECONDS)
@State(Scope.Benchmark)
open class SilentEventListenersBenchmark {

    @Param("100", "1000")
    var silentListeners: Int = 0

    private lateinit var events: Events

    private lateinit var coroutineScope: CoroutineScope

    data object NeverEvent : Event

    data object EmittedEvent : Event

    @Setup
    fun prepare() {
        events = Events()
        coroutineScope = CoroutineScope(Dispatchers.Default + Job() + events)

        coroutineScope.collectEventsAsync<EmittedEvent>(start = CoroutineStart.UNDISPATCHED) {
            Blackhole.consumeCPU(1)
        }

        repeat(silentListeners) {
            coroutineScope.collectEventsAsync<NeverEvent>(start = CoroutineStart.UNDISPATCHED) {
                Blackhole.consumeCPU(1)
            }
        }
    }

    @TearDown
    fun cleanup() {
        coroutineScope.cancel()
    }

    @Benchmark
    fun benchmarkEmittingEvent() = runBlocking(events) {
        EmittedEvent.emit()
    }
}