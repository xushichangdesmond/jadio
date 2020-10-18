//package powerdancer
//
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.map
//import org.springframework.core.io.buffer.DataBuffer
//import org.springframework.http.server.reactive.ServerHttpResponse
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//import reactor.core.publisher.Mono
//
//@RestController
//@RequestMapping("/")
//class JadioController(val streamer: Streamer) {
//    @GetMapping(produces = ["audio/wave"])
//    fun stream(response: ServerHttpResponse): Flow<DataBuffer> {
//        val factory = response.bufferFactory()
//        return streamer.flow().map {
//            if (it.length > 0)
//                factory.wrap(it.data)
//            else
//                factory.allocateBuffer(0)
//        }
//    }
//
//}