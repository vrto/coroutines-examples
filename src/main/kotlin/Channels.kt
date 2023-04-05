import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun main(): Unit = coroutineScope {
    // sending to the channel, this closes the channel automatically
    val channel = produce(capacity = 5) {
        repeat(5) {
            delay(200)
            send(it)
            println("$it sent")
        }
        channel.close()
    }

    // receiving from the channel, consume until closed
    launch {
        // consumeEach can only be used when the channel has a single receiver
        channel.consumeEach { received ->
            println("Received $received")
            delay(500)
        }
    }
}