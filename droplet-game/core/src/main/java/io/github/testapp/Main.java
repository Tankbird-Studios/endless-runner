package io.github.testapp;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
    Texture backgroundTexture;
    Texture bucketTexture;
    Texture dropTexture;
    Sound dropSound;
    Music music;
    SpriteBatch spriteBatch;
    FitViewport viewport;
    Sprite bucketSprite;
    Vector2 touchPos;
    Array<Sprite> dropSprites;
    float dropTimer;
    Rectangle bucketRectangle;
    Rectangle dropRectangle;

    @Override
    public void create() {
        backgroundTexture = new Texture("kaer morhen lake.png");
        bucketTexture = new Texture("geralt.png");
        dropTexture = new Texture("cat-potion-witcher-3.png");
        // Textures.
        dropSound = Gdx.audio.newSound(Gdx.files.internal("Drink.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("SilverForMonsters....mp3"));
        // Sound and music.
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);
        bucketSprite = new Sprite(bucketTexture);
        bucketSprite.setSize(1, 1);
        touchPos = new Vector2();
        dropSprites = new Array<>();
        bucketRectangle = new Rectangle();
        dropRectangle = new Rectangle();
        music.setLooping(true);
        music.setVolume(.5f);
        music.play();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;
        viewport.update(width, height, true); //centers camera if true
        // Resize your application here. The parameters represent the new window size.
    }

    @Override
    public void render() {
        // organized into 3 methods
        input();
        logic();
        draw();
    }

    private void input() {
        float speed = 5f;
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bucketSprite.translateX(speed * delta); //move bucket to the right
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bucketSprite.translateX (-speed * delta); //move bucket to the left
        }

        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY()); // where da touch at?
            viewport.unproject(touchPos); //convert the units to the world units
            bucketSprite.setCenterX(touchPos.x);//change the horizontally centered position of the Geralt
        }
    }


    private void logic() {
        //store the worldWidth and worldHeight as local variables for brevity
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float bucketWidth = bucketSprite.getWidth();
        float bucketHeight = bucketSprite.getHeight();

        //clamp x to values between 0 and worldWidth
        bucketSprite.setX(MathUtils.clamp(bucketSprite.getX(), 0, worldWidth - bucketWidth));

        float delta = Gdx.graphics.getDeltaTime();
        //apply the bucket position and size to the bucketRectangle
        bucketRectangle.set(bucketSprite.getX(), bucketSprite.getY(), bucketWidth, bucketHeight);

        //loop for each drop
        for (int i = dropSprites.size - 1; i >= 0; i--) {
            Sprite dropSprite = dropSprites.get(i); //get the sprite from the list
            float dropWidth = dropSprite.getWidth();
            float dropHeight = dropSprite.getHeight();

            dropSprite.translateY(-2f * delta);
            //apply the drop position and size to the dropRectangle
            dropRectangle.set(dropSprite.getX(), dropSprite.getY(), dropWidth, dropHeight);

            //if the top of the drop goes below the bottom of the view, removes it
            if (dropSprite.getY() < -dropHeight) dropSprites.removeIndex(i);
            else if (bucketRectangle.overlaps(dropRectangle)) {
                dropSprites.removeIndex(i); //remove the drop
                dropSound.play(); //plays the drop sound
            }
        }

        dropTimer += delta; //adds the current delta to the timer
        if (dropTimer> 1f) { //check if it has been more than a second
            dropTimer = 0; //reset the timer
            createDroplet(); //create le droplet
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        spriteBatch.draw(backgroundTexture, 0,0, worldWidth, worldHeight); // draw the background
        bucketSprite.draw(spriteBatch);
        for (Sprite dropSprite : dropSprites) {
            dropSprite.draw(spriteBatch);
        }
        spriteBatch.end();
    }
    private void createDroplet() {
        //local variables for convenience
        float dropWidth = 1;
        float dropHeight = 1;
        float worldHeight = viewport.getWorldHeight();
        float worldWidth = viewport.getWorldWidth();

        //create the drop sprite
        Sprite dropSprite = new Sprite(dropTexture);
        dropSprite.setSize(dropWidth, dropHeight);
        dropSprite.setX(MathUtils.random(0f, worldWidth - dropWidth)); //randomize the drop's position
        dropSprite.setY(worldHeight);
        dropSprites.add(dropSprite); //add it to the list
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

}
