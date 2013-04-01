package net.nickpeters.cosmicbeat.particles;

import net.nickpeters.cosmicbeat.Constants;
import net.nickpeters.cosmicbeat.Vector2D;

/**
 * 
 * @author Nick Peters
 *
 */
import android.content.Context;
public class SmallAsteroid extends AnimatedParticle {
	private final Vector2D r = new Vector2D();
	private double gravity;

	public SmallAsteroid(Context context) {
		super(context, Constants.SA_RADIUS, Constants.SA_MASS,
				Constants.SA_SPRITE);
	}

	// Good resource for helping me figure this out:
	// http://faculty.uca.edu/clarenceb/HP_Tablet_PC/Project_Gravitational_Fields.pdf
	public void update(long now, BlackHole blackHole) {
		if (now < lastTime)
			return;
		r.x = pos.x - blackHole.pos.x;
		r.y = pos.y - blackHole.pos.y;
		gravity = -(Constants.GRAVITY * blackHole.mass)
				/ (r.x * r.x + r.y * r.y);
		velocity.x += gravity * r.x;
		velocity.y += gravity * r.y;
		super.update(now);
	}
}