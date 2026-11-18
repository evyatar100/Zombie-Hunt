import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;

public class Sound extends Thread
{
	enum type {GUN, THUNDER, PUNCH, RELOAD, AMMO, ZOMBIE, GUN_PUNCH, SIRAN};

	private static final Map<String, AudioData> CACHE = new HashMap<>();
	private static final String[] FILES = {
		"GunFire.wav", "Thunder.wav", "Punch.wav", "Reload.wav", "Ammo.wav",
		"Siran.wav", "GunPunch.wav", "Back_Sounds/Sound0.wav",
		"Back_Sounds/Sound1.wav", "Back_Sounds/Sound2.wav",
		"Back_Sounds/Sound3.wav", "Back_Sounds/Sound4.wav",
		"Back_Sounds/Sound5.wav", "Back_Sounds/Sound6.wav"
	};
	private final String soundFile;

	private static final class AudioData
	{
		final AudioFormat format;
		final byte[] samples;

		AudioData(AudioFormat format, byte[] samples)
		{
			this.format = format;
			this.samples = samples;
		}
	}

	public Sound(type soundType)
	{
		soundFile = switch (soundType)
		{
			case GUN -> "GunFire.wav";
			case THUNDER -> "Thunder.wav";
			case PUNCH -> "Punch.wav";
			case RELOAD -> "Reload.wav";
			case AMMO -> "Ammo.wav";
			case SIRAN -> "Siran.wav";
			case ZOMBIE -> "Back_Sounds/Sound" + new Random().nextInt(7) + ".wav";
			case GUN_PUNCH -> "GunPunch.wav";
		};

		if (BrowserAudio.enabled())
		{
			BrowserAudio.emit(soundFile);
			return;
		}
		setDaemon(true);
		start();
	}

	private static AudioData load(String file) throws Exception
	{
		try (InputStream resource = Resources.open("sounds/" + file);
			BufferedInputStream buffered = new BufferedInputStream(resource);
			AudioInputStream original = AudioSystem.getAudioInputStream(buffered))
		{
			AudioFormat input = original.getFormat();
			AudioFormat pcm = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
				input.getSampleRate(), 16, input.getChannels(), input.getChannels() * 2,
				input.getSampleRate(), false);
			try (AudioInputStream decoded = AudioSystem.getAudioInputStream(pcm, original))
			{
				return new AudioData(pcm, decoded.readAllBytes());
			}
		}
	}

	private static AudioData cached(String file) throws Exception
	{
		synchronized (CACHE)
		{
			AudioData data = CACHE.get(file);
			if (data == null)
			{
				data = load(file);
				CACHE.put(file, data);
			}
			return data;
		}
	}

	public static void preload()
	{
		if (BrowserAudio.enabled())
			return;
		for (String file : FILES)
		{
			try { cached(file); }
			catch (Exception e)
			{
				System.err.println("Could not load " + file + ": " + e);
			}
		}
	}

	public void run()
	{
		Clip clip = null;
		try
		{
			AudioData data = cached(soundFile);
			clip = AudioSystem.getClip();
			clip.open(data.format, data.samples, 0, data.samples.length);
			CountDownLatch finished = new CountDownLatch(1);
			clip.addLineListener(event -> {
				if (event.getType() == LineEvent.Type.STOP)
					finished.countDown();
			});
			clip.start();
			finished.await(Math.max(500, clip.getMicrosecondLength() / 1000 + 1000), TimeUnit.MILLISECONDS);
		}
		catch (Exception e)
		{
			if (e instanceof InterruptedException)
				Thread.currentThread().interrupt();
			System.err.println("Could not play " + soundFile + ": " + e);
		}
		finally
		{
			if (clip != null)
				clip.close();
		}
	}
}
