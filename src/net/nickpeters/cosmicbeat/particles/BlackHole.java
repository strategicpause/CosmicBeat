package net.nickpeters.cosmicbeat.particles;
/**
 * 
 * @author Nick Peters
 *
 */
import net.nickpeters.cosmicbeat.Constants;
import android.content.Context;

public class BlackHole extends AnimatedParticle {
	public BlackHole(Context context) {
		super(context, Constants.BH_RADIUS, Constants.BH_MASS, Constants.BH_SPRITE);
		this.pos.x = Constants.BH_POS_X;
		this.pos.y = Constants.BH_POS_Y;
	}
	
	@Override
	public void reset(int canvasWidth, int canvasHeight) {
		// Do nothing
	}
}
