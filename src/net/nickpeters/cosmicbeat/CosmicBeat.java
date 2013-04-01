package net.nickpeters.cosmicbeat;

import net.nickpeters.cosmicbeat.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * @author Nick Peters
 *
 */
public class CosmicBeat extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //Start BG_Music
        Music.play(this, R.raw.beat);
    }
    
    @Override
	protected void onPause() {
		Music.stop(this);
		super.onPause();
	}
}