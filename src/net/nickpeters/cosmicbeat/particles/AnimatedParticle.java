package net.nickpeters.cosmicbeat.particles;

/**
 * 
 * @author Nick Peters
 * 
 */

import net.nickpeters.cosmicbeat.Constants;
import net.nickpeters.cosmicbeat.R;
import net.nickpeters.cosmicbeat.Sprite;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class AnimatedParticle extends Particle {
	protected final int width, height, size;
	protected int curFrame;
	protected Rect dstRect, srcRect;
	private final Bitmap bitmap, explosion;
	private final int numFrames;
	private final int frameRate;
	private long lastFrameTime;
	private int spriteX, spriteY;

	public AnimatedParticle(Context context, double radius, double mass,
			Sprite sprite) {
		super(context, radius, mass);
		// Determine if the Bitmap already exists in the cache
		if (Constants.spriteCache.indexOfKey(sprite.resource_id) >= 0) {
			bitmap = Constants.spriteCache.get(sprite.resource_id);
		} else {
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					sprite.resource_id);
			Constants.spriteCache.put(sprite.resource_id, bitmap);
		}
		if (Constants.EXPLOSION_BITMAP == null) {
			Constants.EXPLOSION_BITMAP = BitmapFactory.decodeResource(
					context.getResources(), R.drawable.explosion);
			Constants.EXPLOSION_WIDTH = Constants.EXPLOSION_BITMAP.getWidth() / 5;
			Constants.EXPLOSION_HEIGHT = Constants.EXPLOSION_BITMAP.getHeight() / 5;
		}
		explosion = Constants.EXPLOSION_BITMAP;
		numFrames = sprite.numFrames;
		frameRate = sprite.fps;
		width = bitmap.getWidth() / sprite.cols;
		height = bitmap.getHeight() / sprite.rows;
		size = sprite.size; // Size of the destination bitmap that is drawn to
							// the screen
		setDestRect((int) pos.x, (int) pos.y, size, (int) radius);
		this.curFrame = 0;
		this.lastFrameTime = 0;
	}

	public int getNumFrame() {
		return numFrames;
	}

	public int getStartFrame() {
		return 0;
	}

	public int getCurrentFrame() {
		return curFrame;
	}

	public int getEndFrame() {
		return this.numFrames - 1;
	}

	@Override
	public void update(long now) {
		super.update(now);
		// Update Destination Rectangle
		if (now < lastTime)
			return;
		switch (state) {
		case Constants.ALIVE:
		case Constants.INVINCIBLE:
			setDestRect((int) pos.x, (int) pos.y, (int) radius, size);
			break;
		case Constants.EXPLODE:
			setDestRect((int) pos.x, (int) pos.y, Constants.EXPLOSION_RADIUS,
					Constants.EXPLOSION_SIZE);
			break;
		}
		updateAnimation(now);
	}

	protected void setSourceRect(int frame) {
		switch (state) {
		case Constants.ALIVE:
		case Constants.INVINCIBLE:
			spriteX = frame % numFrames;
			spriteY = frame / numFrames;
			setSourceRect(spriteX, spriteY, width, height);
			break;
		case Constants.EXPLODE:
			spriteX = frame % Constants.EXPLOSION_FRAMES;
			spriteY = frame / Constants.EXPLOSION_FRAMES;
			setSourceRect(spriteX, spriteY, Constants.EXPLOSION_WIDTH,
					Constants.EXPLOSION_HEIGHT);
			break;
		}
	}

	protected void setDestRect(int x, int y, int r, int s) {
		if (dstRect == null) {
			dstRect = new Rect(x - r, y - r, x + s - r, y + s - r);
		} else {
			dstRect.set(x - r, y - r, x + s - r, y + s - r);
		}
	}

	private void setSourceRect(int x, int y, int width, int height) {
		if (srcRect == null)
			srcRect = new Rect(width * x, height * y, width * x + width, height
					* y + height);
		else
			srcRect.set(width * x, height * y, width * x + width, height * y
					+ height);
	}

	protected void updateAnimation(long now) {
		switch (state) {
		case Constants.ALIVE:
		case Constants.INVINCIBLE:
			if ((now - lastFrameTime) < 1000 / frameRate)
				return;
			curFrame = (curFrame + 1) % numFrames;
			break;
		case Constants.EXPLODE:
			if ((now - lastFrameTime) < 1000 / Constants.EXPLOSION_FPS)
				return;
			if (curFrame < Constants.EXPLOSION_FRAMES - 1)
				curFrame++;
			else
				state = Constants.DEAD;
			break;
		default:
			return;
		}
		setSourceRect(curFrame);
		lastFrameTime = now;
	}

	@Override
	public void reset(int canvasWidth, int canvasHeight) {
		super.reset(canvasWidth, canvasHeight);
		curFrame = 0;
		setSourceRect(curFrame);
	}

	@Override
	public void draw(Canvas canvas) {
		switch (state) {
		case Constants.ALIVE:
		case Constants.INVINCIBLE:
			// super.draw(canvas);
			canvas.drawBitmap(bitmap, srcRect, dstRect, null);
			break;
		case Constants.EXPLODE:
			canvas.drawBitmap(explosion, srcRect, dstRect, null);
			break;
		}
	}
}