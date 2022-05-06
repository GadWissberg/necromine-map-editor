package com.gadarts.necromine.editor.desktop;

import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.editor.desktop.dialogs.DialogPane;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public final class GuiUtils {
	public static GalleryButton createTextureImageButton(final File assetsFolderLocation,
														 final Assets.SurfaceTextures texture) throws IOException {
		return createTextureImageButton(assetsFolderLocation, texture, null);
	}

	static GalleryButton createTextureImageButton(final File assetsFolderLocation,
												  final Assets.SurfaceTextures texture,
												  final ItemListener onClick) throws IOException {
		ImageIcon imageIcon = loadImage(assetsFolderLocation, texture);
		GalleryButton button = new GalleryButton(texture, imageIcon);
		button.setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
		Optional.ofNullable(onClick).ifPresent(click -> button.addItemListener(onClick));
		return button;
	}

	public static ImageIcon loadImage(final File assetsFolderLocation, final Assets.SurfaceTextures texture) throws IOException {
		String path = assetsFolderLocation.getAbsolutePath() + File.separator + texture.getFilePath();
		FileInputStream inputStream = new FileInputStream(path);
		ImageIcon imageIcon = new ImageIcon(ImageIO.read(inputStream));
		inputStream.close();
		return imageIcon;
	}

	public static JScrollPane createEntitiesGallery(final File assetsFolderLocation, final ItemListener onClick) {
		GridLayout layout = new GridLayout(0, 3);
		JPanel gallery = new JPanel(layout);
		JScrollPane jScrollPane = new JScrollPane(gallery);

		ButtonGroup buttonGroup = new ButtonGroup();
		Arrays.stream(Assets.SurfaceTextures.values()).forEach(texture -> {
			try {
				GalleryButton button = GuiUtils.createTextureImageButton(assetsFolderLocation, texture, onClick);
				buttonGroup.add(button);
				gallery.add(button);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		});

		jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		jScrollPane.setPreferredSize(new Dimension(jScrollPane.getPreferredSize().width, 480));
		return jScrollPane;
	}

	public static void openNewDialog(final Component parent, final DialogPane content) {
		JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent));

		dialog.setTitle(content.getDialogTitle());
		dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		dialog.setContentPane(content);
		dialog.setResizable(false);
		dialog.pack();
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		dialog.setVisible(true);
	}

	public static Component findByNameInPanel(final JPanel container, final String name) {
		return Arrays.stream(container.getComponents())
				.filter(component -> component.getName() != null && component.getName().equals(name))
				.findFirst().orElse(null);
	}
}
