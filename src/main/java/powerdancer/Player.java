package powerdancer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class Player implements MulticastReceiver.Listener, AutoCloseable {

    SourceDataLine line;
    byte[] buffer;
    byte encodedSampleRate;
    byte bitSize;

    static private char[] hexArray = "0123456789ABCDEF".toCharArray();

    static String header(byte[] b){
        char[] hexChars = new char[10];
        for (int i = 0; i < 5; i++) {
            int v = b[i];

            hexChars[i * 2] = hexArray[(v & 0xF0) >> 4];
            hexChars[i * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    static int decodeSampleRate(byte encodedSampleRate) {
        if (encodedSampleRate == (byte)-127) {
            return 44100;
        } else if (encodedSampleRate == (byte)2) {
            return 96000;
        }
        return 44100;
    }
    static AudioFormat audioFormat(int sampleRate, int bitSize) {
        return new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                sampleRate,
                bitSize,
                2,
                ((bitSize + 7) / 8) * 2,
                sampleRate,
                false);
    }

    @Override
    public void onInit(byte[] buffer, int length) {
        try {
            this.buffer = buffer;

            encodedSampleRate = buffer[0];
            bitSize = buffer[1];

            line = AudioSystem.getSourceDataLine(audioFormat(decodeSampleRate(encodedSampleRate), bitSize));
            line.open();
            line.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        onPacket(length);
    }

    @Override
    public void onPacket(int length) {
        line.write(buffer, 5, length - 5);
    }

    @Override
    public void close() throws Exception {
        line.stop();
        line.close();
    }

    public static void main(String[] args) {
        new MulticastReceiver(new Player(), args.length > 0? args[0]: null).run();
    }
}
