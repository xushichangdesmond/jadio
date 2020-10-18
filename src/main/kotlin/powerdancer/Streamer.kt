//package powerdancer
//
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.reactive.asFlow
//import org.sheinbergon.aac.encoder.AACAudioEncoder
//import org.sheinbergon.aac.encoder.AACAudioOutput
//import org.sheinbergon.aac.encoder.WAVAudioInput
//import org.sheinbergon.aac.encoder.util.AACEncodingProfile
//import org.slf4j.LoggerFactory
//import org.springframework.core.io.buffer.DataBuffer
//import org.springframework.stereotype.Component
//import reactor.core.publisher.BufferOverflowStrategy
//import reactor.core.publisher.EmitterProcessor
//import reactor.core.publisher.Flux
//import reactor.core.publisher.FluxSink
//import java.net.DatagramPacket
//import java.util.function.Consumer
//import javax.sound.sampled.*
//
//@Component
//class Streamer: AutoCloseable{
//    companion object {
//        val logger = LoggerFactory.getLogger(Streamer::class.java)
//    }
//
//    val processor = EmitterProcessor.create<DatagramPacket>()
//    val sink = processor.sink(FluxSink.OverflowStrategy.DROP)
//    val dummySubscriber = processor
//        .doOnError { logger.error("error in dummy subscriber", it) }
//        .subscribe()
////    val inLine: TargetDataLine
////    val job: Job
//
//    override fun close() {
//        dummySubscriber.dispose()
////        job.cancel()
////        inLine.stop()
////        inLine.close()
//    }
//
//    fun flow(): Flow<DatagramPacket> =
//        processor.onBackpressureBuffer(
//            2000,
//            BufferOverflowStrategy.ERROR
//        ).asFlow()
//
//    init {
//
//        AudioSystem.getMixerInfo().forEach {
//            println(it.name)
//        }
//
//        val encoder = AACAudioEncoder.builder()
//            .afterBurner(false)
//            .channels(2)
//            .sampleRate(44100)
//            .profile(AACEncodingProfile.AAC_LC)
//            .build()
//
////        val inMixer = AudioSystem.getMixerInfo().filter { it.name.equals("Port Hi-Fi Cable Output (VB-Audio Hi") }
//        val inMixer = AudioSystem.getMixerInfo().filter { it.name.equals("Hi-Fi Cable Output (VB-Audio Hi") }
//            .map { AudioSystem.getMixer(it) }
//            .first()
//
//        inMixer.targetLineInfo.forEach {
//            if (it is DataLine.Info) {
//                it.formats.forEach {
//                    println("format " + it)
//                }
//            }
//        }
//
////        val inPort = inMixer.targetLineInfo.filter {
////            it.lineClass.equals(Port::class.java)
////        }.map {
////            inMixer.getLine(it)
////        }.first() as Port
//
////        println(inPort)
//
//        val audioFormat = AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 88200F, 24, 2,
//            ((24 + 7) / 8) * 2,
//            88200F
//            , false)
////        val audioFormat = AudioFormat(88200F, 24, 2, true, false)
//
//
//        AudioSystem.getTargetFormats(AudioFormat.Encoding.PCM_SIGNED, audioFormat).forEach {
//            println(it)
//            println(it.sampleRate)
//        }
//
////        inLine = AudioSystem.getLine(DataLine.Info(TargetDataLine::class.java, audioFormat)) as TargetDataLine
//
////        inLine = AudioSystem.getTargetDataLine(audioFormat)
////        inLine = inMixer.targetLineInfo.filter {
////            it.lineClass.equals(TargetDataLine::class.java)
////        }.map {
////            inMixer.getLine(it)
////        }.first() as TargetDataLine
//
////        println(inLine.format.channels)
////        println(inLine.format.encoding)
////        println(inLine.format.sampleRate)
////        println(inLine.format.sampleSizeInBits)
////        println(inLine.format.isBigEndian)
////
////
////        inLine.open(audioFormat, audioFormat.frameSize * 5000)
////        inLine.start()
//
////        job = GlobalScope.launch {
////            val buf = ByteArray(audioFormat.frameSize * 16)
////            while(true) {
////                val len = inLine.read(buf, 0, buf.size)
////                sink.next(encoder.encode(WAVAudioInput.pcms16le(buf, len)))
////            }
////        }
//
//
//        val outMixer = AudioSystem.getMixerInfo().filter { it.name.equals("Laser Proj (NVIDIA High Definition Audio)") }
//            .map {
//                AudioSystem.getMixer(it)
//            }
//            .first()
//        val outLine = outMixer.sourceLineInfo.filter {
//            it.lineClass.equals(SourceDataLine::class.java)
//        }.map {outMixer.getLine(it)}.first() as SourceDataLine
//        outLine.open(AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100F, 16, 2,
//            ((16 + 7) / 8) * 2,
//            44100F
//            , false))
//        outLine.start()
//
//        MulticastReceiver.consumer = object : Consumer<DatagramPacket> {
//            override fun accept(t: DatagramPacket) {
////                sink.next(encoder.encode(WAVAudioInput.pcms16le(t.data, t.length)))
////                println(t.data.contentToString())
//                outLine.write(t.data, t.offset+5, t.length-5)
//                sink.next(t)
//            }
//        }
//    }
//}