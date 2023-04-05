import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

// flows are essentially coroutine-powered cold streams
suspend fun main() {
    // flow is factory func, emit "returns" a value from flow
    val valuesFlow = flow {
        repeat(5) {
            delay(500)
            emit("Value $it")
        }
    }

    coroutineScope {
        val job = launch {
            valuesFlow
                .onStart { println("Starting flow") }
                .filter {
                    val kept = (0..1).random() == 1
                    println("Keeping $it? -> $kept")
                    kept
                }
                .map(String::uppercase)
                .collect() {
                    println("Collected: $it")
                }
        }

        launch {
            delay(2100) // fifth value will never be collected
            println("I got enough, canceling")
            job.cancel()
        }
    }
}