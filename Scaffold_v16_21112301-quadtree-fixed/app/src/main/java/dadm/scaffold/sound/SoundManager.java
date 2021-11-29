package dadm.scaffold.sound;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.HashMap;

public final class SoundManager {

	private static final int MAX_STREAMS = 10;
	private static final float DEFAULT_MUSIC_VOLUME = 0.8f;
	private static final String MUSIC_PREF_KEY = "Music";
	private static final String SOUNDS_PREF_KEY = "Sound";

	private SharedPreferences settingsAudioStart;
	private HashMap<GameEvent, Integer> soundsMap;
	
	private Context context;
	private SoundPool soundPool;
	private MediaPlayer bgPlayer;


	private boolean soundEnabled;
	private boolean musicEnabled;

	public SoundManager(Context context) {
		this.context = context;
		SharedPreferences prefs = this.context.getSharedPreferences("soundManager",0);
		settingsAudioStart = context.getSharedPreferences("soundManager",0);
				//PreferenceManager.getDefaultSharedPreferences(context);
		soundEnabled = prefs.getBoolean(SOUNDS_PREF_KEY, true);
		musicEnabled = prefs.getBoolean(MUSIC_PREF_KEY, true);

		loadSounds();
		loadMusic();
	}

	private void loadIfNeeded () {

		if (soundEnabled) {
			loadSounds();
		}
		if (musicEnabled) {
			loadMusic();
		}

	}
	public boolean getSoundStatus() {
		return soundEnabled;
	}
	public boolean getMusicStatus() {
		return musicEnabled;
	}


	public void toggleSoundStatus() {
		soundEnabled = !soundEnabled;
		if (soundEnabled) {
			loadSounds();
		}
		else {
			unloadSounds();
		}

		// Save it to preferences
		context.getSharedPreferences("soundManager",0).edit()
				.putBoolean(SOUNDS_PREF_KEY, soundEnabled)
				.commit();
	}

	public void toggleMusicStatus() {
		musicEnabled = !musicEnabled;
		if (musicEnabled) {
			loadMusic();
			resumeBgMusic();
		}
		else {
			unloadMusic();
		}
		// Save it to preferences
		context.getSharedPreferences("soundManager",0).edit()
				.putBoolean(MUSIC_PREF_KEY, musicEnabled)
				.commit();
	}




	private void loadEventSound(Context context, GameEvent event, String... filename) {
		try {
			AssetFileDescriptor descriptor = context.getAssets().openFd("sfx/" + filename[0]);
			int soundId = soundPool.load(descriptor, 1);
			soundsMap.put(event, soundId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void playSoundForGameEvent(GameEvent event) {
		Integer soundId = soundsMap.get(event);
		if (!soundEnabled) {
			return;
		}
		if (soundId != null) {
			soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
		}
	}
	public void pauseBgMusic() {
		if (musicEnabled) {
			bgPlayer.pause();
		}
	}
	public void resumeBgMusic() {
		if (musicEnabled) {
			bgPlayer.start();
		}


	}

	private void loadSounds() {

			createSoundPool();
			soundsMap = new HashMap<GameEvent, Integer>();
			loadEventSound(context, GameEvent.AsteroidHit, "Asteroid_explosion_1.wav");
			loadEventSound(context, GameEvent.SpaceshipHit, "Spaceship_explosion.wav");
			loadEventSound(context, GameEvent.LaserFired, "Laser_shoot.wav");
			loadEventSound(context, GameEvent.PowerUpHit, "powerup.mp3");


	}

	private void loadMusic() {

			try {
				// Important to not reuse it. It can be on a strange state
				bgPlayer = new MediaPlayer();
				AssetFileDescriptor afd = context.getAssets().openFd("sfx/cancionPou.mp3");
				bgPlayer.setDataSource(afd.getFileDescriptor(),
						afd.getStartOffset(), afd.getLength());
				bgPlayer.setLooping(true);
				bgPlayer.setVolume(DEFAULT_MUSIC_VOLUME, DEFAULT_MUSIC_VOLUME);
				bgPlayer.prepare();
				bgPlayer.start();
			}
			catch (IOException e) {
				e.printStackTrace();
			}

	}

	private void createSoundPool() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
		}
		else {
			AudioAttributes audioAttributes = new AudioAttributes.Builder()
					.setUsage(AudioAttributes.USAGE_GAME)
					.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
					.build();
			soundPool = new SoundPool.Builder()
					.setAudioAttributes(audioAttributes)
					.setMaxStreams(MAX_STREAMS)
					.build();
		}
	}

	private void unloadSounds() {
		soundPool.release();
		soundPool = null;
		soundsMap.clear();		
	}

	private void unloadMusic() {
		bgPlayer.stop();
		bgPlayer.release();
	}



}
