import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayDeque;

// Publishes recent sound cues for the browser client. noVNC does not carry audio.
public final class BrowserAudio
{
	private static final int MAX_EVENTS = 64;
	private static final String output = System.getenv("ZH_AUDIO_EVENTS_FILE");
	private static final boolean enabled = "vnc".equalsIgnoreCase(System.getenv("ZH_DISPLAY_MODE"))
		&& output != null && !output.isEmpty();
	private static final ArrayDeque<String> events = new ArrayDeque<>();
	private static long sequence;
	private static boolean warned;

	private BrowserAudio() {}

	public static boolean enabled()
	{
		return enabled;
	}

	public static synchronized void emit(String soundFile)
	{
		if (!enabled)
			return;

		events.addLast(++sequence + "\t" + soundFile + "\n");
		if (events.size() > MAX_EVENTS)
			events.removeFirst();

		Path target = Paths.get(output);
		Path temporary = null;
		try
		{
			Path directory = target.toAbsolutePath().getParent();
			Files.createDirectories(directory);
			temporary = Files.createTempFile(directory, "zombie-audio-", ".tmp");
			Files.writeString(temporary, String.join("", events), StandardCharsets.UTF_8);
			try
			{
				Files.move(temporary, target, StandardCopyOption.ATOMIC_MOVE,
					StandardCopyOption.REPLACE_EXISTING);
			}
			catch (AtomicMoveNotSupportedException e)
			{
				Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e)
		{
			if (!warned)
			{
				System.err.println("Browser audio events could not be written: " + e);
				warned = true;
			}
		}
		finally
		{
			if (temporary != null)
			{
				try { Files.deleteIfExists(temporary); }
				catch (IOException ignored) {}
			}
		}
	}
}
