package net.nickpeters.cosmicbeat;
/**
 * 
 * @author Nick Peters
 * 
 */

public class Sprite {
	public final int resource_id;
	public final int fps;
	public final int rows;
	public final int cols;
	public final int numFrames;
	public final int size;

	public Sprite() {
		this(0, 0, 0, 0, 0, 0);
	}

	public Sprite(int id, int fps, int rows, int cols, int numFrames, int size) {
		this.resource_id = id;
		this.fps = fps;
		this.rows = rows;
		this.cols = cols;
		this.numFrames = numFrames;
		this.size = size;
	}
}
