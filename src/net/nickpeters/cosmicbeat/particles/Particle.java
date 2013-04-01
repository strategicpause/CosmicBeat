package net.nickpeters.cosmicbeat.particles;
/**
 * @author Nick Peters
 */
import net.nickpeters.cosmicbeat.Constants;
import net.nickpeters.cosmicbeat.Vector2D;
import android.content.Context;
import android.graphics.Canvas;

public class Particle {
	public byte state;
	public final double radius;
	public final double mass;
	protected final Vector2D velocity;
	public final Vector2D pos;
	protected final Context context;
	protected long lastTime;
	/** Initialized now so we don't have to later */
	private int side;
	/** Used in resolveCollision() */
	private Vector2D delta, mtd, normal;
	double length, vn;
	float im1, im2, k;
	
	public Particle(Context context, double radius, double mass) {
		this.context = context;
		this.pos = new Vector2D();
		this.velocity = new Vector2D();
		this.radius = radius;
		this.mass = mass;
		this.state = Constants.ALIVE;
		this.delta = new Vector2D();
		this.mtd = new Vector2D();
		this.normal = new Vector2D();
	}
	
	public void moveTo(Vector2D position) {
		moveTo(position.x, position.y);
	}
	
	public void moveTo(double x, double y) {
		pos.x = x;
		pos.y = y;
	}
	
	public void setVelocity(Vector2D vel) {
		setVelocity(vel.x, vel.y);
	}

	public void setVelocity(double velX, double velY) {
		velocity.x = velX;
		velocity.y = velY;
	}
	
	// http://cgp.wikidot.com/circle-to-circle-collision-detection
	public boolean collidesWith(Particle other) {
		if(state == Constants.ALIVE && other.state == Constants.ALIVE) {
			delta.set(other.pos);
			delta.sub(pos);
			return delta.length() < (other.radius + radius);
		}
		return false;
	}
	
	// http://stackoverflow.com/questions/345838/ball-to-ball-collision-detection-and-handling
	public void resolveCollision(Particle other) {
		delta.set(pos);
		delta.sub(other.pos);
		length = delta.length();
		// MTD - Minimum Translation Distance
		delta.mult((radius + other.radius - length) / length);
		mtd.set(delta);
		//IM - Inverse Mass
		im1 = 1 / (float)mass;
		im2 = 1 / (float)other.mass;
		// Move particles apart
		normal.set(mtd);
		normal.mult(im1 / (im1 + im2));
		pos.add(normal);
		normal.set(mtd);
		normal.mult(im2 / (im1 + im2));
		other.pos.sub(normal);
		// Impact Speed
		delta.set(velocity);
		delta.sub(other.velocity);
		normal.set(mtd);
		normal.normalize();
		vn = delta.dotProduct(normal);
		if(vn > 0) return;
		k = (float) ((-1.0f * vn) / (im1 + im2));
		mtd.mult(k);
		normal.set(mtd);
		normal.mult(im1);
		velocity.add(normal);
		normal.set(mtd);
		normal.mult(im2);
		other.velocity.sub(normal);
	}
		
	public void update(long now) {
		if (now < lastTime) return;
		// Make sure velocities are withing a certain range
		if(velocity.x > Constants.MAX_VELOCITY)
			velocity.x = Constants.MAX_VELOCITY;
		if(velocity.y > Constants.MAX_VELOCITY)
			velocity.y = Constants.MAX_VELOCITY;
		if(velocity.x < Constants.MIN_VELOCITY)
			velocity.x = Constants.MIN_VELOCITY;
		if(velocity.y < Constants.MIN_VELOCITY)
			velocity.y = Constants.MIN_VELOCITY;
		if(state == Constants.ALIVE)
			pos.add(velocity);
		lastTime = now;
	}

	public void draw(Canvas canvas) {
		canvas.drawCircle((float)pos.x, (float)pos.y, (float)radius, Constants.CIRCLE_PAINT);
	}
		
	public boolean disappearsFromEdge(int canvasWidth, int canvasHeight) {
		if (pos.x - radius > canvasWidth || pos.x + radius < 0)	// hit left or right side
			return true;
        if (pos.y - radius > canvasHeight || pos.y + radius < 0) // hit top or bottom
        	return true;
        return false;
	}
	
	public void reset(int canvasWidth, int canvasHeight) {
		/** Determine which side the object appears from
		 * 0 - Left
		 * 1 - Top
		 * 2 - Right
		 * 3 - Bottom
		 */
		state = Constants.ALIVE;
		side = Constants.RANDOM.nextInt(4);
		switch(side) {
		case 0:
			moveTo(0, Math.random()*(canvasHeight-radius));
			setVelocity(Math.random(), Math.random() * Math.pow(-1, Constants.RANDOM.nextInt() % 2));
			break;
		case 1:
			moveTo(Math.random() * canvasWidth, 0);
			setVelocity(Math.random() * Math.pow(-1, Constants.RANDOM.nextInt() % 2), Math.random());
			break;
		case 2:
			moveTo(canvasWidth, Math.random()*(canvasHeight));
			setVelocity(-1 * Math.random(), Math.random() * Math.pow(-1, Constants.RANDOM.nextInt() % 2));
			break;
		case 3:
			moveTo(Math.random()*(canvasWidth), canvasHeight);
			setVelocity(Math.random() * Math.pow(-1, Constants.RANDOM.nextInt() % 2), -1 * Math.random());
			break;
		}
	}
}