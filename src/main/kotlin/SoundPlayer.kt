import java.io.BufferedInputStream
import java.io.InputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

class SoundPlayer {
    private val clips = mutableListOf<Clip>()

    fun playSound(path: String) {
        try {
            val inputStream: InputStream =
                SoundPlayer::class.java.getResourceAsStream(path)
                    ?: throw IllegalArgumentException("Sound not found: $path")
            val bufferedIn = BufferedInputStream(inputStream)
            val audioInput = AudioSystem.getAudioInputStream(bufferedIn)
            val clip: Clip = AudioSystem.getClip()
            clip.open(audioInput)
            clip.start()
            clips.add(clip)

            clip.addLineListener {
                if (it.type == javax.sound.sampled.LineEvent.Type.STOP) {
                    clip.close()
                    clips.remove(clip)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopSound() {
        for (clip in clips) {
            if (clip.isRunning) {
                clip.stop()
            }
            clip.close()
        }
        clips.clear()
    }
}