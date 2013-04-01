package net.nickpeters.cosmicbeat.particles;
/**
 * 
 * @author Richard Galvan
 *
 */
import net.nickpeters.cosmicbeat.Constants;
import android.content.Context;

public class Asteroid extends AnimatedParticle {

	public Asteroid(Context context) {
		super(context, Constants.A_RADIUS, Constants.A_MASS, Constants.A_SPRITE);
	}

	public void explode(Laser other, SmallAsteroid p1, SmallAsteroid p2) {
		state = Constants.EXPLODE;
		curFrame = 0;
		setSourceRect(curFrame);
		p1.state = Constants.ALIVE;
		p2.state = Constants.ALIVE;
		p1.moveTo(pos);
		p2.moveTo(pos);
		p1.setVelocity(other.velocity);
		p2.setVelocity(other.velocity);
		if (this.velocity.dotProduct(other.velocity) < 0)
			p2.velocity.x *= -1;
		else
			p2.velocity.y *= -1;
	}
}