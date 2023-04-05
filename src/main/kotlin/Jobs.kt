import kotlinx.coroutines.*

// this runs on main thread
fun main() = runBlocking(CoroutineName("Main")) {
    val job1 = this.launch(/* Inherits Main coroutine name */) {
        whoami()
        delay(500L)
        println("Job one done!")
    }
    val job2 = this.launch(CoroutineName("Coroutine Job Two")) {
        whoami()
        delay(1500)
        println("Job two done!")
    }
    val job3 = launch(CoroutineName("Cancellable Job")) {
        whoami()
        try {
            repeat(1000) {
                delay(500)
                println("Doing cancellable work ${1 + it}")
            }
        } finally {
            // can't start new coroutines or delay here
            println("Cancellable job - finally block always runs.")
            // unless it's NonCancellable
            withContext(NonCancellable) {
                // good for freeing resources up
                delay(1000)
                println("Cancellable job with NonCancellable cleanup runs.")
            }
        }
    }
    println("waiting ...\n")
    job1.join()
    job2.join()
    job3.cancelAndJoin() // suspend until a coroutine has finished cancellation
    println("\nall done!")
}

private fun CoroutineScope.whoami() {
    println("I am ${coroutineContext[CoroutineName]?.name} running on thread ${Thread.currentThread().name}")
}
