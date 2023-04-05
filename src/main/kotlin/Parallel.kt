import kotlinx.coroutines.*
import java.lang.System.currentTimeMillis

// this is a go-to way of parallelizing work
// runs on Dispatchers.Default thread pool
suspend fun main() = coroutineScope {
    val start = currentTimeMillis()
    println("Starting at time ${start - currentTimeMillis()}")

    val resultA = async {
        whoami()
        delay(500)
        println("Returning from A at time ${currentTimeMillis() - start}")
        "a"
    }
    val resultB = async {
        whoami()
        delay(1000)
        println("Returning from B at time ${currentTimeMillis() - start}")
        "b"
    }

    println(resultA.await())
    println(resultB.await())
    println("Finished at time ${currentTimeMillis() - start}") // 1000-ish, not 1500-ish
}

private fun CoroutineScope.whoami() {
    println("I am ${coroutineContext[CoroutineName]?.name} running on thread ${Thread.currentThread().name}")
}