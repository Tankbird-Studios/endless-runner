package com.tankbird.endlessrunner

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class Main : Game() {

    lateinit var batch: SpriteBatch
    lateinit var viewport: FitViewport

    override fun create() {
        batch = SpriteBatch()
        viewport = FitViewport(8f, 5f)

        screen = FirstScreen(this)
    }

    override fun render() {
        super.render()
    }

    override fun dispose() {
        screen.dispose()
        batch.dispose()
    }
}
