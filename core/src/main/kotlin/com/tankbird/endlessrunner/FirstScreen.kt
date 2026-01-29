package com.tankbird.endlessrunner

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.ScreenUtils

/** First screen of the application. Displayed after the application is created.  */
class FirstScreen(private val game: Main) : Screen {

    private val backgroundTexture: Texture = Texture("background.png")

    override fun show() {
        // Prepare your screen here.
    }

    override fun render(delta: Float) {
        draw()
    }

    private fun draw() {
        ScreenUtils.clear(Color.BLACK)

        game.viewport.apply()
        game.batch.projectionMatrix = game.viewport.camera?.combined

        game.batch.begin()

        val worldWidth = game.viewport.worldWidth
        val worldHeight = game.viewport.worldHeight
        game.batch.draw(backgroundTexture, 0f, 0f, worldWidth, worldHeight)

        game.batch.end()
    }

    override fun resize(width: Int, height: Int) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if (width <= 0 || height <= 0) return

        game.viewport.update(width, height, true)
    }

    override fun pause() {
        // Invoked when your application is paused.
    }

    override fun resume() {
        // Invoked when your application is resumed after pause.
    }

    override fun hide() {
        // This method is called when another screen replaces this one.
    }

    override fun dispose() {
        backgroundTexture.dispose()
    }
}
