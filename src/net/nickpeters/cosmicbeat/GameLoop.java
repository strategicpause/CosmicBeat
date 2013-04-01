package net.nickpeters.cosmicbeat;

import java.util.LinkedList;
import java.util.List;

import net.nickpeters.cosmicbeat.particles.Asteroid;
import net.nickpeters.cosmicbeat.particles.BlackHole;
import net.nickpeters.cosmicbeat.particles.Laser;
import net.nickpeters.cosmicbeat.particles.Particle;
import net.nickpeters.cosmicbeat.particles.SmallAsteroid;
import net.nickpeters.cosmicbeat.particles.SpaceShip;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;

public class GameLoop extends Thread {
	private Context context;
	// The drawable to use as the background of the animation canvas
	private Bitmap mBackgroundImage;
	// Current height of the surface/canvas.
	private int mCanvasHeight = 320;
	// Current width of the surface/canvas.
	private int mCanvasWidth = 320;
	// Used to figure out elapsed time between frames
	private long mLastTime;
	// Indicate whether the surface has been created & is ready to draw
	private boolean mRun = false;
	// Handle to the surface manager object we interact with
	private final SurfaceHolder mSurfaceHolder;
	private final List<Particle> asteroids = new LinkedList<Particle>();
	private final List<SmallAsteroid> queue = new LinkedList<SmallAsteroid>();
	private final List<Laser> lasers = new LinkedList<Laser>();
	private final BlackHole blackHole;
	private final SpaceShip spaceShip;
	private int points;
	// Temp variables
	Particle p1, p2;
	private SmallAsteroid temp1, temp2;
	private Laser laser;

	// Game Notifications
	Toast GameOver;
	Toast extraLife;

	// Sounds effect object
	private SoundEffects soundFX;

	public GameLoop(SurfaceHolder surfaceHolder, Context context, Handler handler) {
		this.context = context;
		// get handles to some important objects
		mSurfaceHolder = surfaceHolder;
		Resources res = context.getResources();
		mBackgroundImage = BitmapFactory.decodeResource(res, R.drawable.background);
		// Initialize SoundFX object
		soundFX = new SoundEffects(context);
		blackHole = new BlackHole(context);
		spaceShip = new SpaceShip(context, mCanvasWidth, mCanvasHeight);
		// Pop-up messages for game events
		GameOver = Toast.makeText(context, Constants.GAME_OVER_MESSAGE,
				Toast.LENGTH_LONG);
		extraLife = Toast.makeText(context, Constants.EXTRA_LIFE_MESSAGE,
				Toast.LENGTH_LONG);
		for (int i = 0; i < Constants.NUM_ASTEROIDS; i++)
			asteroids.add(new Asteroid(context));
		points = 0;
	}

	public void doStart() {
		synchronized (mSurfaceHolder) {
			resetParticles();
			// set up timers
			mLastTime = System.currentTimeMillis() + 100;
		}
	}

	private void resetParticles() {
		int size = asteroids.size();
		for (int i = 0; i < size; i++) {
			if (asteroids.get(i) instanceof SmallAsteroid)
				Pool.returnAsteroid((SmallAsteroid) asteroids.remove(i));
			else
				asteroids.get(i).reset(mCanvasWidth, mCanvasHeight);
		}
	}

	@Override
	public void run() {
		// Initialize
		doStart();
		// Start game loop
		Canvas c = null;
		while (mRun) {
			try {
				c = mSurfaceHolder.lockCanvas(null);
				synchronized (mSurfaceHolder) {
					updatePhysics();
					doDraw(c);
				}
			} finally {
				// do this in a finally so that if an exception is thrown
				// during the above, we don't leave the Surface in an
				// inconsistent state
				if (c != null) {
					mSurfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}
	}

	/**
	 * Used to signal the thread whether it should be running or not.
	 * Passing true allows the thread to run; passing false will shut it
	 * down if it's already running. Calling start() after this was most
	 * recently called with false will result in an immediate shutdown.
	 * 
	 * @param b
	 *            true to run, false to shut down
	 */
	public void setRunning(boolean b) {
		mRun = b;
	}

	/* Callback invoked when the surface dimensions change. */
	public void setSurfaceSize(int width, int height) {
		// synchronized to make sure these all change atomically
		synchronized (mSurfaceHolder) {
			mCanvasWidth = width;
			mCanvasHeight = height;
			// don't forget to resize the background image
			mBackgroundImage = Bitmap.createScaledBitmap(mBackgroundImage,
					width, height, true);
			// Move the position of the spaceship to the middle.
			spaceShip.moveTo(width / 2, height / 2);
			// Move the position of the blackhole
			blackHole.moveTo(width / 4, height * (3 / 4.0));
		}
	}

	/**
	 * Draws the spaceship, score, and background to the provided Canvas.
	 */
	private void doDraw(Canvas canvas) {
		// Draw the background image. Operations on the Canvas accumulate
		// so this is like clearing the screen.
		canvas.drawBitmap(mBackgroundImage, 0, 0, null);
		// Draw the sprite (animation is handled within the AnimatedSprite
		// class)
		spaceShip.draw(canvas);
		blackHole.draw(canvas);
		for (int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).draw(canvas);
		}
		// Only fire laser if SpaceShip is ALIVE
		if (spaceShip.state == Constants.ALIVE) {
			for (int i = 0; i < lasers.size(); i++) {
				lasers.get(i).draw(canvas);
			}
		}
		canvas.drawText("Score: " + points, 5, 30, Constants.SCORE_PAINT);
		canvas.drawText("Lives: " + spaceShip.getNumLives(), 160, 30,
				Constants.SCORE_PAINT);
		mLastTime = System.currentTimeMillis();
	}

	/**
	 * Figures the game state (x, y, bounce, ...) based on the passage of
	 * realtime. Does not invalidate(). Called as part of the game loop).
	 */
	private void updatePhysics() {
		long now = System.currentTimeMillis();
		if (mLastTime > now)
			return;
		spaceShip.update(now);
		blackHole.update(now);
		// Update and see if any lasers disappear off of the screen
		for (int i = 0; i < lasers.size(); i++) {
			laser = lasers.get(i);
			laser.update(now);
			if (laser.disappearsFromEdge(mCanvasWidth, mCanvasHeight)) {
				Pool.returnLaser(lasers.remove(i));
			}
		}

		// Determine if there are any collisions
		// http://www.kirupa.com/developer/actionscript/multiple_collision2.htm
		int size = asteroids.size();
		for (int i = 0; i < size; i++) {
			p1 = asteroids.get(i);

			if (p1 instanceof SmallAsteroid) {
				((SmallAsteroid) p1).update(now, blackHole);
			} else {
				p1.update(now);
			}
			if (p1.disappearsFromEdge(mCanvasWidth, mCanvasHeight)) {
				p1.state = Constants.DEAD;
			}

			for (int j = 0; j < lasers.size(); j++) {
				laser = lasers.get(j);
				if (p1.collidesWith(laser)) {
					laserCollision(laser, p1);
					soundFX.PlayRandomBeat();
				}
			}

			if (p1.collidesWith(blackHole)) {
				blackHoleCollision(p1);
			}
			
			if (p1.collidesWith(spaceShip)) {
				spaceShip.explode();
				if (spaceShip.getNumLives() <= 0) {
					GameOver.show();
					setRunning(false);
				}
				// NOTE:Only play explosion sound effect once, can be
				// removed once
				// the empty space issue is resolved.
				if (spaceShip.state == Constants.EXPLODE) {
					soundFX.playExplode();
				}
			}
			for (int j = i + 1; j < size; j++) {
				p2 = asteroids.get(j);
				if (p1.collidesWith(p2)) {
					p1.resolveCollision(p2);
				}
			}
		}

		// Reset any dead particles
		for (int i = 0; i < asteroids.size(); i++) {
			p1 = asteroids.get(i);
			if (p1.state == Constants.DEAD) {
				if (p1 instanceof SmallAsteroid) {
					Pool.returnAsteroid((SmallAsteroid) asteroids.remove(i));
				} else {
					p1.reset(mCanvasWidth, mCanvasHeight);
				}
			}
		}
		// Reset any dead lasers
		for (int i = 0; i < lasers.size(); i++) {
			if (lasers.get(i).state == Constants.DEAD) {
				Pool.returnLaser(lasers.remove(i));
			}
		}
		// Add any particles into the main array from the queue
		asteroids.addAll(queue);
		queue.clear();
	}

	public void onTouchEvent(MotionEvent event) {
		// Handle laser rotation calculation here.
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (spaceShip.state == Constants.DEAD
					|| spaceShip.state == Constants.EXPLODE)
				return;
			// Reset laser if its not already at center of screen
			laser = Pool.getLaser(context);
			laser.reset(mCanvasWidth, mCanvasHeight);

			// Calculate angle between the line made by these two points
			// and the horizontal axis
			double run = event.getX() - spaceShip.pos.x;
			double rise = spaceShip.pos.y - event.getY();
			spaceShip.angle = Math.atan2(rise, run) * Constants.RAD_TO_DEG;
			laser.angle = Math.atan2(rise, run) * Constants.RAD_TO_DEG;
			if (spaceShip.angle < 0) {
				spaceShip.angle += 360;
				laser.angle += 360;
			}
			spaceShip.angle -= 90;
			laser.angle -= 90;

			// TEST: Set velocity to coordinates of tapped point on screen
			laser.setVelocity(run, rise);
			lasers.add(laser);
			soundFX.playShoot();
		}
	}

	// Returns true if we need to reset the laser
	public void laserCollision(Laser laser, Particle asteroid) {
		if (asteroid instanceof Asteroid) {
			temp1 = Pool.getAsteroid(context);
			temp2 = Pool.getAsteroid(context);
			((Asteroid) asteroid).explode(laser, temp1, temp2);
			queue.add(temp1);
			queue.add(temp2);
			laser.state = Constants.DEAD;
		} else if (asteroid instanceof SmallAsteroid) {
			asteroid.state = Constants.DEAD;
		}
	}

	public void blackHoleCollision(Particle asteroid) {
		if (asteroid instanceof SmallAsteroid) {
			asteroid.state = Constants.DEAD;
			points++;
			// Add a life for every 25 points scored
			if (points % 25 == 0) {
				spaceShip.incrementLives();
				extraLife.show();
			}
			// Add an asteroid to increase difficulty
			if (points % Constants.NEW_ASTEROID == 0) {
				asteroids.add(new Asteroid(context));
			}
		}
	}
}
