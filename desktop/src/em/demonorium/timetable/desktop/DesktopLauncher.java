package em.demonorium.timetable.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import em.demonorium.timetable.Core;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 1920 / 3;
		config.width = 1080 / 3;
		config.resizable = false;
		new LwjglApplication(new Core(Core.SystemType.DESKTOP), config);
	}
}
