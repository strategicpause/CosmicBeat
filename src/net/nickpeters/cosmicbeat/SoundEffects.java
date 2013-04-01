package net.nickpeters.cosmicbeat;
/**
 * 
 * @author Richard Galvan
 *
 */

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundEffects {
	private static MediaPlayer bg_Music = null;
	private List<MediaPlayer> beatList;
	private MediaPlayer laser_shoot = null;
	private MediaPlayer explode = null;
	private MediaPlayer randomBeat;
	private int randomNum;

	public SoundEffects(Context context) {
		laser_shoot = MediaPlayer.create(context, R.raw.laser_shoot);
		explode = MediaPlayer.create(context, R.raw.explode);
		// Add each beat to a list for random access.
		beatList = new ArrayList<MediaPlayer>();
		beatList.add(MediaPlayer.create(context, R.raw.beat1));
		beatList.add(MediaPlayer.create(context, R.raw.beat2));
		beatList.add(MediaPlayer.create(context, R.raw.beat3));
		beatList.add(MediaPlayer.create(context, R.raw.beat4));
		beatList.add(MediaPlayer.create(context, R.raw.beat5));
		beatList.add(MediaPlayer.create(context, R.raw.beat6));
		beatList.add(MediaPlayer.create(context, R.raw.beat7));
		beatList.add(MediaPlayer.create(context, R.raw.beat8));
		beatList.add(MediaPlayer.create(context, R.raw.beat9));
		beatList.add(MediaPlayer.create(context, R.raw.beat10));
	}

	public void PlayRandomBeat() {
		randomNum = Constants.RANDOM.nextInt(Constants.BEAT_LIST_SIZE);
		randomBeat = beatList.get(randomNum);
		randomBeat.seekTo(0);
		randomBeat.start();
	}

	public void playShoot() {
		laser_shoot.seekTo(0);
		laser_shoot.start();
	}

	public void playExplode() {
		explode.seekTo(0);
		explode.start();
	}

	/** Stop old mediaplayer and start new one */
	public void playBGMusic(Context context) {
		stopBGMusic(context);
		bg_Music = MediaPlayer.create(context, R.raw.beat);
		bg_Music.setLooping(true);
		bg_Music.start();
	}

	/** Stop the background music */
	public void stopBGMusic(Context context) {
		if (bg_Music != null) {
			bg_Music.stop();
			bg_Music.release();
			bg_Music = null;
		}
	}
}
