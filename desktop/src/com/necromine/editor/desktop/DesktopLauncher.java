package com.necromine.editor.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.necromine.editor.NecromineMapEditor;

import java.awt.*;

public class DesktopLauncher {
	private static final String WINDOW_HEADER = "Necromine Map Editor";
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	public static void main(final String[] arg) {
		EventQueue.invokeLater(() -> {
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			handleScreen(config);
			NecromineMapEditor listener = new NecromineMapEditor();
			LwjglAWTCanvas lwjgl = new LwjglAWTCanvas(listener, config);
			new MapperWindow(WINDOW_HEADER, lwjgl);
		});
	}

	private static void handleScreen(final LwjglApplicationConfiguration config) {
		config.width = WIDTH;
		config.height = HEIGHT;
	}
}
