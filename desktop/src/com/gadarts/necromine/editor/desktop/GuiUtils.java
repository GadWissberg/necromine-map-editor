package com.gadarts.necromine.editor.desktop;

import com.gadarts.necromine.assets.Assets;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public final class GuiUtils {
	static GalleryButton createTextureImageButton(final File assetsFolderLocation,
												  final Assets.FloorsTextures texture,
												  final ItemListener onClick) throws IOException {
		String path = assetsFolderLocation.getAbsolutePath() + File.separator + texture.getFilePath();
		FileInputStream inputStream = new FileInputStream(path);
		ImageIcon imageIcon = new ImageIcon(ImageIO.read(inputStream));
		GalleryButton button = new GalleryButton(texture, imageIcon);
		button.setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
		button.addItemListener(onClick);
		inputStream.close();
		return button;
	}
}
