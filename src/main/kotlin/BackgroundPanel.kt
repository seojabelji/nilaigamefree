import java.awt.*
import javax.swing.*
import java.awt.image.BufferedImage

class BackgroundPanel(initialBackground: BufferedImage) : JPanel() {
    private var background: BufferedImage = initialBackground

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        g.drawImage(background, 0, 0, width, height, this)
    }

    override fun getPreferredSize(): Dimension {
        return Dimension(background.width, background.height)
    }

    fun updateBackgroundImage(newImage: BufferedImage) {
        background = newImage
        repaint()
    }
}
