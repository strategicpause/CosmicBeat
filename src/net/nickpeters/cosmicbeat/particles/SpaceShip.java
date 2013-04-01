package net.nickpeters.cosmicbeat.particles;
/**
 * 
 * @author Nick Peters
 *
 */

import net.nickpeters.cosmicbeat.Constants;
import android.content.Context;
import android.graphics.Canvas;

public class SpaceShip extends AnimatedParticle {
	public double angle;
	public long respawnTime;
	public byte tempState;
	private int numLives;

	public SpaceShip(Context context, int canvasWidth, int canvasHeight) {
		super(context, Constants.SS_RADIUS, Constants.SS_MASS,
				Constants.SS_SPRITE);
		angle = 0;
		this.pos.x = canvasWidth / 2;
		this.pos.y = canvasHeight / 2;
		this.numLives = Constants.DEFAULT_LIVES;
		this.respawnTime = Constants.DEFAULT_RESPAWN_TIME;
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.save();
		// Rotate canvas by the given angle
		canvas.rotate((float) -angle, (float) pos.x, (float) pos.y);
		super.draw(canvas);
		canvas.restore();
	}

	@Override
	public void update(long now) {
		super.update(now);
		if (state == Constants.DEAD) {
			if (now - respawnTime > Constants.RESPAWN_TIME) {
				// Want to keep Space Ship temporarily invincible to allow
				// asteroids to move out of its space.
				this.state = Constants.INVINCIBLE;
				respawnTime = now;
			}
		} else if (state == Constants.INVINCIBLE) {
			if (now - respawnTime > Constants.RESPAWN_TIME) {
				this.state = Constants.ALIVE;
			}
		}
	}

	@Override
	protected void updateAnimation(long now) {
		tempState = state;
		super.updateAnimation(now);
		if (tempState == Constants.EXPLODE && state == Constants.DEAD) {
			respawnTime = now;
		}
	}

	public void explode() {
		if (state == Constants.ALIVE) {
			// Only decrement lives if they're greater than 0
			if (this.numLives > 0) {
				decrementLives();
			}
			state = Constants.EXPLODE;
		}
	}

	public int getNumLives() {
		return numLives;
	}

	private void decrementLives() {
		numLives--;
	}

	public void incrementLives() {
		numLives++;
	}

	@Override
	public void reset(int canvasWidth, int canvasHeight) {
		// Do nothing
	}
}
