import java.awt.*
import java.awt.event.MouseEvent
import javax.imageio.ImageIO
import javax.swing.*

fun main() {
    println("Running")
    val app = Main()
    app.launch()
}

data class TransformState(var value: Boolean)

class Main {
    private val sound = SoundPlayer()

    private fun resizeIcon(originalIcon: ImageIcon, width: Int, height: Int): ImageIcon {
        val resizedImage = originalIcon.image.getScaledInstance(width, height, Image.SCALE_SMOOTH)
        return ImageIcon(resizedImage)
    }

    fun launch() {
        val frame = JFrame("ནེ་ལི་རྩེད་མོ་3dགསལ་ཆ་མཐོ་པོ2025")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.isResizable = false

        sound.playSound("/start.wav")

        val background = ImageIO.read(Main::class.java.getResourceAsStream("/background.png"))
        val title = ImageIcon(Main::class.java.getResource("/title.png"))
        val buttonImage = ImageIcon(Main::class.java.getResource("/button.png"))
        val buttonPressedImage = ImageIcon(Main::class.java.getResource("/button_pressed.png"))

        val gameButton = resizeIcon(buttonImage, 100, 50)
        val gameButtonPressed = resizeIcon(buttonPressedImage, 100, 50)

        val panel = object : JPanel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)
                g.drawImage(background, 0, 0, width, height, this)
            }

            override fun getPreferredSize(): Dimension {
                return Dimension(background.width, background.height)
            }
        }
        panel.layout = GridBagLayout()
        val box = Box.createVerticalBox()
        val titleLabel = JLabel(title)
        titleLabel.alignmentX = Component.CENTER_ALIGNMENT

        val button = JButton(gameButton)
        button.isBorderPainted = false
        button.isContentAreaFilled = false
        button.isFocusPainted = false
        button.isOpaque = false
        button.alignmentX = Component.CENTER_ALIGNMENT
        button.pressedIcon = gameButtonPressed

        button.addActionListener {
            sound.stopSound()
            frame.dispose()
            game()
        }

        box.add(titleLabel)
        box.add(Box.createRigidArea(Dimension(0, 20)))
        box.add(button)

        panel.add(box)
        frame.contentPane = panel
        frame.pack()
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
    }

    private fun game() {
        val nilaiTransformed = TransformState(false)

        val frame = JFrame("ནེ་ལི་རྩེད་མོ་3dགསལ་ཆ་མཐོ་པོ2025")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.isResizable = false

        val background = ImageIO.read(Main::class.java.getResourceAsStream("/dog.png"))
        val panel = BackgroundPanel(background)

        val polandball = resizeIcon(ImageIcon(Main::class.java.getResource("/ball.png")), 50, 50)
        val nilaiball = resizeIcon(ImageIcon(Main::class.java.getResource("/nilai.png")), 75, 75)

        val dragBall = JLabel(polandball)
        val dragBallX = background.width / 2 - -25 / 2
        val dragBallY = background.height / 2 - 100 / 2
        dragBall.setBounds(dragBallX, dragBallY, 100, 100)

        val nilai = JLabel(nilaiball)
        nilai.setBounds(30, 50, 80, 80)

        val timer = timerButton(panel, dragBall, nilai, nilaiTransformed)
        timer.revalidate()
        timer.repaint()
        panel.layout = null

        var offsetX = 0
        var offsetY = 0

        dragBall.addMouseListener(object : java.awt.event.MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                offsetX = e.x
                offsetY = e.y
            }
        })

        dragBall.addMouseMotionListener(object : java.awt.event.MouseMotionAdapter() {
            override fun mouseDragged(e: MouseEvent) {
                val newX = dragBall.x + e.x - offsetX
                val newY = dragBall.y + e.y - offsetY

                val minX = -25
                val maxX = panel.width - dragBall.width + 25
                val minY = 25
                val maxY = panel.height - dragBall.height + 25

                val clampedX = newX.coerceIn(minX, maxX)
                val clampedY = newY.coerceIn(minY, maxY)

                dragBall.setLocation(clampedX, clampedY)

                if (nilaiTransformed.value && dragBall.bounds.intersects(nilai.bounds)) {
                    sound.playSound("/bone_crack.wav")
                    frame.dispose()
                    gameover()
                }
            }
        })

        panel.add(dragBall)
        panel.add(nilai)
        panel.add(timer)

        frame.contentPane = panel
        frame.pack()
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
    }

    private fun timerButton(panel: BackgroundPanel, dragBall: JLabel, nilai: JLabel, nilaiTransformed: TransformState): JButton {
        val idleIcon = ImageIcon(Main::class.java.getResource("/timer_starter.png"))
        val frames = (15 downTo 0).map {
            ImageIcon(Main::class.java.getResource("/$it.png"))
        }

        val button = object : JButton(idleIcon) {
            override fun paintComponent(g: Graphics) {
                val g2d = g as Graphics2D
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

                val image = (icon as? ImageIcon)?.image ?: return

                val iconWidth = image.getWidth(this)
                val iconHeight = image.getHeight(this)
                val buttonWidth = width
                val buttonHeight = height

                val scale = minOf(buttonWidth.toDouble() / iconWidth, buttonHeight.toDouble() / iconHeight)
                val newWidth = (iconWidth * scale).toInt()
                val newHeight = (iconHeight * scale).toInt()

                val x = (buttonWidth - newWidth) / 2
                val y = (buttonHeight - newHeight) / 2

                g2d.drawImage(image, x, y, newWidth, newHeight, this)
            }
        }
        button.setBounds(0, 0, 61, 61)
        button.isBorderPainted = false
        button.isContentAreaFilled = false
        button.isFocusPainted = false
        button.isOpaque = false

        button.addActionListener {
            sound.playSound("/count.wav")
            button.isEnabled = false
            button.icon = frames[0]
            var frameIndex = 1

            val timer = Timer(1000) {
                if (frameIndex < frames.size) {
                    button.icon = frames[frameIndex]
                    if (frameIndex < frames.size - 1) sound.playSound("/count.wav")

                    if (frameIndex == frames.lastIndex) {
                        sound.playSound("/gong.wav")

                        val newBackground = ImageIO.read(Main::class.java.getResourceAsStream("/new_dog.png"))
                        panel.updateBackgroundImage(newBackground)

                        dragBall.icon = resizeIcon(ImageIcon(Main::class.java.getResource("/new_ball.png")), 50, 50)
                        nilai.icon = resizeIcon(ImageIcon(Main::class.java.getResource("/new_nilai.png")), 75, 75)

                        val timer = Timer(5000, null)
                        timer.addActionListener {
                            nilai.icon = resizeIcon(ImageIcon(Main::class.java.getResource("/distorted_nilai.png")), 130, 180)
                            sound.playSound("/flute.wav")

                            val newX = nilai.x + 50
                            val newY = nilai.y
                            nilai.setBounds(newX, newY, 130, 180)

                            nilai.revalidate()
                            nilai.repaint()
                            nilaiTransformed.value = true
                            timer.stop()
                        }
                        timer.isRepeats = false
                        timer.start()
                    }
                    frameIndex++
                } else {
                    (it.source as Timer).stop()
                    button.isVisible = false
                    button.isEnabled = false
                }
            }
            timer.start()
        }
        return button
    }

    private fun gameover() {
        val frame = JFrame("ནེ་ལི་རྩེད་མོ་3dགསལ་ཆ་མཐོ་པོ2025")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.isResizable = false

        sound.stopSound()
        sound.playSound("/death.wav")
        val background = ImageIO.read(Main::class.java.getResourceAsStream("/gameover.png"))
        val panel = object : JPanel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)
                g.drawImage(background, 0, 0, width, height, this)
            }

            override fun getPreferredSize(): Dimension {
                return Dimension(background.width, background.height)
            }
        }

        frame.contentPane = panel
        frame.pack()
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
    }
}