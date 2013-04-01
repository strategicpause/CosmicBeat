package net.nickpeters.cosmicbeat;

/**
 * This file is a heavily modified version of "AnimatedGameLoopView.java" from
 * COMP425 at CSUCI
 *
 */

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CosmicBeatView extends SurfaceView implements SurfaceHolder.Callback {
	private boolean retry;
	private GameLoop thread;
	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message m) {
			// process any messages
		}
	};

	public CosmicBeatView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// register our interest in hearing about changes to our surface
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		// create thread only; it's started in surfaceCreated()
		thread = new GameLoop(holder, context, handler);
		setFocusable(true); // make sure we get key events
	}

	/* Callback invoked when the surface dimensions change. */
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		thread.setSurfaceSize(width, height);
	}

	/*
	 * Callback invoked when the Surface has been created and is ready to be
	 * used.
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		// start the thread here so that we don't busy-wait in run()
		// waiting for the surface to be created
		thread.setRunning(true);
		thread.start();
	}

	/*
	 * Callback invoked when the Surface has been destroyed and must no longer
	 * be touched. WARNING: after this method returns, the Surface/Canvas must
	 * never be touched again!
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		// we have to tell thread to shut down & wait for it to finish, or else
		// it might touch the Surface after we return and explode
		retry = true;
		thread.setRunning(false);
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Pass the touch event on to our thread
		thread.onTouchEvent(event);
		return true;
	}
}