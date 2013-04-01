/**
 * 
 * @author Nick Peters
 *
 */
package net.nickpeters.cosmicbeat;

import java.util.LinkedList;

import net.nickpeters.cosmicbeat.particles.Laser;
import net.nickpeters.cosmicbeat.particles.SmallAsteroid;

import android.content.Context;

public class Pool {
	private static LinkedList<SmallAsteroid> asteroids = new LinkedList<SmallAsteroid>();
	private static LinkedList<Laser> lasers = new LinkedList<Laser>();

	public static SmallAsteroid getAsteroid(Context context) {
		if (asteroids.isEmpty()) {
			// Add two asteroids at a time if this is empty
			asteroids.add(new SmallAsteroid(context));
			asteroids.add(new SmallAsteroid(context));
		}
		return asteroids.removeFirst();
	}

	public static void returnAsteroid(SmallAsteroid asteroid) {
		asteroids.add(asteroid);
	}

	public static Laser getLaser(Context context) {
		if (lasers.isEmpty()) {
			// Add two lasers at a time if this is empty
			lasers.add(new Laser(context));
			lasers.add(new Laser(context));
		}
		return lasers.removeFirst();
	}

	public static void returnLaser(Laser laser) {
		lasers.add(laser);
	}
}