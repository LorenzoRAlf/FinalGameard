package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

public class MyGdxGame implements ApplicationListener {

	// Constant rows and columns of the sprite sheet
	private static final int FRAME_COLS = 4, FRAME_ROWS = 4;

	// Objects used
	Animation<TextureRegion> walkAnimation, downAnimation, leftAnimation, rightAnimation, upAnimation; // Must declare frame type (TextureRegion)
	Texture walkSheet, background, fireball;
	TextureRegion currentFrame;
	SpriteBatch spriteBatch;
	TextureRegion bgRegion;
	int bgX, bgY, x, y, terraX, terraY, test, fireballX, fireballY, fireballTimer, bulletDirection;
	boolean fireballShot = false;

	// A variable for tracking elapsed time for the animation
	float stateTime;

	@Override
	public void create() {

		// Load the sprite sheet as a Texture
		walkSheet = new Texture(Gdx.files.internal("terra_sheet.png"));

		// Use the split utility method to create a 2D array of TextureRegions. This is
		// possible because this sprite sheet contains frames of equal size and they are
		// all aligned.
		TextureRegion[][] tmp = TextureRegion.split(walkSheet,
				walkSheet.getWidth() / FRAME_COLS,
				walkSheet.getHeight() / FRAME_ROWS);

		// Place the regions into a 1D array in the correct order, starting from the top
		// left, going across first. The Animation constructor requires a 1D array.
		TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}
		TextureRegion[] down = new TextureRegion[4];
		for (int i = 0; i < FRAME_COLS; i++){
			down[i] = tmp[0][i];
		}
		TextureRegion[] left = new TextureRegion[4];
		for (int i = 0; i < FRAME_COLS; i++){
			left[i] = tmp[1][i];
		}
		TextureRegion[] right = new TextureRegion[4];
		for (int i = 0; i < FRAME_COLS; i++){
			right[i] = tmp[2][i];
		}
		TextureRegion[] up = new TextureRegion[4];
		for (int i = 0; i < FRAME_COLS; i++){
			up[i] = tmp[3][i];
		}

		// Initialize the Animation with the frame interval and array of frames
		walkAnimation = new Animation<TextureRegion>(0.1f, walkFrames);
		downAnimation = new Animation<TextureRegion>(0.1f,down);
		leftAnimation = new Animation<TextureRegion>(0.1f,left);
		rightAnimation = new Animation<TextureRegion>(0.1f,right);
		upAnimation = new Animation<TextureRegion>(0.1f,up);
		// Instantiate a SpriteBatch for drawing and reset the elapsed animation
		// time to 0
		spriteBatch = new SpriteBatch();
		stateTime = 0f;

		fireball = new Texture(Gdx.files.internal("fire_spell.png"));

		// bg
		background = new Texture(Gdx.files.internal("ff_snow.png"));
		background.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
		bgRegion = new TextureRegion(background,0,0);
		bgX = 5000;
		bgY = 5000;
		x=0;
		y=0;
		terraX = 300;
		terraY = 250;
		fireballTimer = 0;
		fireballShot = false;
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen
		stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
		bgRegion.setRegion(bgX,bgY,bgX,bgY);
		currentFrame = downAnimation.getKeyFrame(1, true);
		
		// Get current frame of animation for the current stateTime
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			test++;
			currentFrame = upAnimation.getKeyFrame(stateTime, true);
			y = y-5;
		} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			test++;
			currentFrame = leftAnimation.getKeyFrame(stateTime, true);
			x = x+5;
		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			test++;
			currentFrame = rightAnimation.getKeyFrame(stateTime, true);
			x = x-5;
		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			test++;
			currentFrame = downAnimation.getKeyFrame(stateTime, true);
			y = y+5;
		}

		spriteBatch.begin();
		spriteBatch.draw(bgRegion, x, y);
		spriteBatch.draw(currentFrame, terraX, terraY); // Draw current frame at (50, 50)
		if (!fireballShot) {
			fireballX = terraX+25;
			fireballY = terraY+20;
			if (Gdx.input.isKeyPressed(Input.Keys.A)){
				spriteBatch.draw(fireball, fireballX, fireballY);
				fireballShot = true;
				if (currentFrame == upAnimation.getKeyFrame(stateTime, true)) {
					bulletDirection=1;
				} else if (currentFrame == leftAnimation.getKeyFrame(stateTime, true)) {
					bulletDirection=2;
				} else if (currentFrame == rightAnimation.getKeyFrame(stateTime, true)) {
					bulletDirection=3;
				} else if (currentFrame == downAnimation.getKeyFrame(stateTime, true)) {
					bulletDirection=4;
				}
			}
		}
		if (fireballShot) {
			if (bulletDirection == 1) {
				fireballY = fireballY+10;
			} else if (bulletDirection == 2) {
				fireballX = fireballX-10;
			} else if (bulletDirection == 3) {
				fireballX = fireballX+10;
			} else if (bulletDirection == 4) {
				fireballY = fireballY-10;
			}
			if (fireballTimer < 20){
				fireballTimer++;
				spriteBatch.draw(fireball, fireballX, fireballY);
			} else if (fireballTimer == 20){
				fireballShot = false;
				fireballTimer = 0;
			}
		}
		spriteBatch.end();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() { // SpriteBatches and Textures must always be disposed
		spriteBatch.dispose();
		walkSheet.dispose();
	}
}