package px.seisen;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		//config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
		//set size to 800x480
		config.setWindowedMode(800, 480);

		config.setForegroundFPS(144);
		config.setTitle("Seisen");
		config.setIdleFPS(10);

		new Lwjgl3Application(new Seisen(), config);
	}
}
