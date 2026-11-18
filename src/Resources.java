import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Resources
{
	private Resources() {}

	public static InputStream open(String name) throws IOException
	{
		InputStream bundled = Resources.class.getResourceAsStream("/assets/" + name);
		if (bundled != null)
			return bundled;

		// Keep the source-tree and Docker development builds working.
		return Files.newInputStream(Path.of("files", name));
	}
}
