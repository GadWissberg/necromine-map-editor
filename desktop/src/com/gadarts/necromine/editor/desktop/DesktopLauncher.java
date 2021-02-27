package com.gadarts.necromine.editor.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.necromine.editor.MapEditor;

import java.awt.*;

public class DesktopLauncher {
	private static final String WINDOW_HEADER = "Necromine Map Editor";
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	public static void main(final String[] arg) {
		EventQueue.invokeLater(() -> {
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			config.samples = 3;
			config.width = WIDTH;
			config.height = HEIGHT;
			MapEditor mapManager = new MapEditor(WIDTH, HEIGHT);
			LwjglAWTCanvas lwjgl = new LwjglAWTCanvas(mapManager, config);
			MapperGui gui = new MapperGui(WINDOW_HEADER, lwjgl, mapManager);
			mapManager.subscribeForEvents(gui);
		});
	}

}
