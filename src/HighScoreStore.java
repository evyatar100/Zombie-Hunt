import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

public final class HighScoreStore
{
	private HighScoreStore() {}

	public static Path path()
	{
		Path home = Path.of(System.getProperty("user.home"));
		String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
		if (os.contains("mac"))
			return home.resolve("Library/Application Support/Zombie Hunt/scores.dat");
		if (os.contains("win"))
		{
			String appData = System.getenv("APPDATA");
			Path base = appData == null || appData.isBlank()
				? home.resolve("AppData/Roaming") : Path.of(appData);
			return base.resolve("Zombie Hunt/scores.dat");
		}
		String xdgData = System.getenv("XDG_DATA_HOME");
		Path base = xdgData != null && Path.of(xdgData).isAbsolute()
			? Path.of(xdgData) : home.resolve(".local/share");
		return base.resolve("zombie-hunt/scores.dat");
	}

	public static int load()
	{
		Path current = path();
		if (Files.exists(current))
			return read(current);

		Path legacy = Path.of("files", "scores.dat");
		if (Files.exists(legacy))
		{
			int score = read(legacy);
			save(score);
			return score;
		}
		return 0;
	}

	private static int read(Path file)
	{
		try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(file)))
		{
			Object value = input.readObject();
			return value instanceof Integer ? Math.max(0, (Integer) value) : 0;
		}
		catch (IOException | ClassNotFoundException e)
		{
			System.err.println("Could not read high score from " + file + ": " + e);
			return 0;
		}
	}

	public static void save(int score)
	{
		Path target = path();
		Path temporary = null;
		try
		{
			Path directory = target.getParent();
			Files.createDirectories(directory);
			temporary = Files.createTempFile(directory, "scores-", ".tmp");
			try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(temporary)))
			{
				output.writeObject(Math.max(0, score));
			}
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
			System.err.println("Could not save high score to " + target + ": " + e);
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
