import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

/** Uses a cursor warp that does not synthesize mouse motion events on macOS. */
public final class MacMouse
{
	private static boolean available = load();

	private MacMouse() {}

	private static boolean load()
	{
		if (!System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("mac"))
			return false;
		try
		{
			Path code = Path.of(MacMouse.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			Path library = (Files.isDirectory(code) ? code : code.getParent()).resolve("libzombie_mouse.dylib");
			System.load(library.toString());
			return true;
		}
		catch (URISyntaxException | SecurityException | UnsatisfiedLinkError e)
		{
			System.err.println("Native Mac mouse capture unavailable: " + e);
			return false;
		}
	}

	public static boolean warp(int x, int y)
	{
		if (!available)
			return false;
		int error = warpCursor(x, y);
		if (error != 0)
		{
			available = false;
			System.err.println("Native Mac mouse warp failed: " + error);
			return false;
		}
		return true;
	}

	private static native int warpCursor(int x, int y);
}
