package com.andyrobo.audio;

import processing.core.PApplet;
import android.content.Context;
import android.media.AudioManager;
import android.telephony.ServiceState;
import android.util.Log;

public class AudioControl {
	
	private static AudioManager audioManager;
	
	private static final String TAG = "AndyAudioControl";
	
	public void getAudioControl(PApplet parent) {
		audioManager = (AudioManager) parent.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamSolo(AudioManager.STREAM_MUSIC, true);
		Log.i(TAG, "Running solo");
	}
	
	public void relinquishAudioControl(PApplet parent) {
		audioManager = (AudioManager) parent.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
		Log.i(TAG, "Releasing control over Audio");
	}
	
	public void setMaxVolume(PApplet parent) {
		audioManager = (AudioManager) parent.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
		Log.i(TAG, "Loud and clear");
	}
	
// Yet to verify whether call state is actually being modified 
	public void setCallsOff(PApplet parent) {
		ServiceState serviceState = new ServiceState();
		serviceState.setStateOff();
		Log.i(TAG, "Setting calls off");
	}
	
	public void setCallsOn(PApplet parent) {
		ServiceState serviceState = new ServiceState();
		serviceState.setState(ServiceState.STATE_IN_SERVICE);
		Log.i(TAG, "Setting calls back on");
	}
	

}
