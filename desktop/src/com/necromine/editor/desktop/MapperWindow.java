package com.necromine.editor.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import org.lwjgl.openal.AL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MapperWindow extends JFrame {
	private final LwjglAWTCanvas lwjgl;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;

	public MapperWindow(final String header, final LwjglAWTCanvas lwjgl)  {
		super(header);
		this.lwjgl = lwjgl;
		defineMapperWindow(lwjgl.getCanvas());
	}

	private void defineMapperWindow(final Canvas canvas) {
		defineMapperWindowAttributes();
		JPanel mainPanel = new JPanel(new BorderLayout());
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JPanel(), canvas);
		mainPanel.add(splitPane);
		getContentPane().add(mainPanel);
	}

	private void defineMapperWindowAttributes() {
		defineWindowClose();
		setSize(WIDTH, HEIGHT);
		setLocationByPlatform(true);
		setVisible(true);
		setResizable(false);
	}

	private void defineWindowClose() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				lwjgl.stop();
				AL.destroy();
				e.getWindow().dispose();
			}
		});
	}
}
