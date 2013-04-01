package net.nickpeters.cosmicbeat;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.SparseArray;

/**
 * 
 * @author Nick Peters
 * 
 */
public class Constants {
	public static final Paint CIRCLE_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
	public static final Paint LASER_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
	public static final Paint SCORE_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
	static {
		CIRCLE_PAINT.setColor(Color.WHITE);
		LASER_PAINT.setColor(Color.RED);
		SCORE_PAINT.setTextSize(32f);
		SCORE_PAINT.setColor(Color.YELLOW);
	}

	public static final double RAD_TO_DEG = 180 / Math.PI;
	public static final int NEW_ASTEROID = 10;
	public static final int NUM_ASTEROIDS = 7;
	public static final int RESPAWN_TIME = 1000;
	public static final int INVINCIBLE_TIME = 2000;

	public static final int BEAT_LIST_SIZE = 10;

	public static final SparseArray<Bitmap> spriteCache = new SparseArray<Bitmap>();

	public static final Random RANDOM = new Random();
	public static final byte ALIVE = 0;
	public static final byte EXPLODE = 1;
	public static final byte DEAD = 2;
	public static final byte INVINCIBLE = 3;
	public static final double MAX_VELOCITY = 1;
	public static final double MIN_VELOCITY = -1;

	/** Game States **/
	public final static int GAME_OVER = 1;
	public final static int GAME_PAUSE = 2;
	public final static int GAME_RUNNING = 3;

	/** Game Notifications **/
	public static final CharSequence GAME_OVER_MESSAGE = "Game Over";
	public static final CharSequence EXTRA_LIFE_MESSAGE = "Survival Bonus: 1+Extra Life";

	/** Explosion Constants **/
	public static Bitmap EXPLOSION_BITMAP = null;
	public final static int EXPLOSION_FRAMES = 25;
	public final static int EXPLOSION_FPS = 25;
	public static int EXPLOSION_WIDTH;
	public static int EXPLOSION_HEIGHT;
	public final static int EXPLOSION_RADIUS = 15;
	public final static int EXPLOSION_SIZE = EXPLOSION_RADIUS * 2;

	/** Asteroid Constants **/
	public final static double A_RADIUS = 25;
	public final static double A_MASS = 2;
	public final static int A_FPS = 5;
	public final static int A_ROWS = 5;
	public final static int A_COLS = 5;
	public final static Sprite A_SPRITE = new Sprite(R.drawable.asteroid,
			A_FPS, A_ROWS, A_COLS, A_COLS, (int) A_RADIUS * 2);

	/** Small Astroid Constants **/
	public final static double SA_RADIUS = 9;
	public final static double SA_MASS = 1;
	public final static int SA_FPS = 6;
	public final static int SA_ROWS = 6;
	public final static int SA_COLS = 6;
	public final static Sprite SA_SPRITE = new Sprite(R.drawable.smallasteroid,
			SA_FPS, SA_ROWS, SA_COLS, SA_COLS, (int) SA_RADIUS * 3);

	/** Black Hole Constants **/
	public final static double BH_RADIUS = 15;
	public final static double BH_MASS = 3;
	public final static int BH_FPS = 8;
	public final static int BH_ROWS = 4;
	public final static int BH_COLS = 4;
	public final static double BH_POS_X = 50;
	public final static double BH_POS_Y = 300;
	public final static double GRAVITY = 0.5;
	public final static Sprite BH_SPRITE = new Sprite(R.drawable.blackhole,
			BH_FPS, BH_ROWS, BH_COLS, BH_COLS, (int) BH_RADIUS * 3);

	/** Space Ship Constants **/
	public final static int DEFAULT_LIVES = 3;
	public final static int DEFAULT_RESPAWN_TIME = 0;
	public final static double SS_RADIUS = 30;
	public final static double SS_MASS = 1;
	public final static int SS_FPS = 3;
	public final static int SS_ROWS = 1;
	public final static int SS_COLS = 3;
	public final static Sprite SS_SPRITE = new Sprite(R.drawable.spaceship,
			SS_FPS, SS_ROWS, SS_COLS, SS_COLS, (int) SS_RADIUS * 2);

	/** Laser Constants **/
	public final static double LB_RADIUS = 10;
	public final static double LB_MASS = 1;
	public final static int LB_FPS = 3;
	public final static int LB_ROWS = 1;
	public final static int LB_COLS = 1;
	public final static Sprite LB_SPRITE = new Sprite(R.drawable.green_laser,
			LB_FPS, LB_ROWS, LB_COLS, LB_COLS, (int) LB_RADIUS * 2);

	/** Laser Constants **/
	public final static double L_RADIUS = 15;
	public final static double L_MASS = 2;

	/** Explosion Constants **/
	public final static double EX_RADIUS = 30;
	public final static double EX_MASS = 1;
	public final static int EX_FPS = 25;
	public final static int EX_FRAMES = 25;
	public final static int EX_ROWS = 5;
	public final static int EX_COLS = 5;
	public final static Sprite EX_SPRITE = new Sprite(R.drawable.explosion,
			EX_FPS, EX_ROWS, EX_COLS, 5, (int) EX_RADIUS * 2);
}