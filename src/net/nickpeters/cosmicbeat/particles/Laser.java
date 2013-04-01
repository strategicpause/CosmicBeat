package net.nickpeters.cosmicbeat.particles;

import net.nickpeters.cosmicbeat.Constants;
import android.content.Context;
import android.graphics.Canvas;

public class Laser extends AnimatedParticle {
	public double angle;
	public int respawnTime;
	private int canvasWidth, canvasHeight;
	//For Laser Animation
	public Laser(Context context) {
		super(context, Constants.LB_RADIUS, Constants.LB_MASS, Constants.LB_SPRITE);
	}
	
	public Laser(Context context, int canvasWidth, int canvasHeight) {
		super(context, Constants.LB_RADIUS, Constants.LB_MASS, Constants.LB_SPRITE);
		reset(canvasWidth, canvasHeight);
	}
	
	public void draw(Canvas canvas) {
		canvas.save();
		//Rotate canvas by the given angle
		canvas.rotate((float)-angle, (float)pos.x, (float)pos.y);
		super.draw(canvas);
		canvas.restore();
	}
	
	public void setVelocity(double velX, double velY)
	{
		super.setVelocity(velX, -velY);
		velocity.normalize();
		velocity.mult(5);
		state = Constants.ALIVE;
	}
	
	public boolean IsAtCenter()
	{
		if(this.pos.x == this.canvasWidth / 2 && this.pos.y == this.canvasHeight / 2){
			return true;
		}
		return false;
	}
	
	@Override
	public void reset(int canvasWidth, int canvasHeight) {
		angle = 0;
		this.pos.x = canvasWidth / 2;
		this.pos.y = canvasHeight / 2;
		this.canvasWidth = canvasWidth / 2;
		this.canvasHeight = canvasHeight / 2;
		state = Constants.DEAD;
	}
	
	@Override
	public void update(long now) {
		if (now < lastTime) return;
		if(state == Constants.ALIVE) {
			setDestRect((int)pos.x, (int)pos.y, (int)radius, size);
			pos.add(velocity);
		}
		updateAnimation(now);
		lastTime = now;
	}
}
