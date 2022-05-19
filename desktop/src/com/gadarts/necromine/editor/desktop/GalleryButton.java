package com.gadarts.necromine.editor.desktop;

import com.gadarts.necromine.assets.Assets;
import lombok.Getter;

import javax.swing.*;
import javax.swing.plaf.metal.MetalToggleButtonUI;
import java.awt.*;

@Getter
public class GalleryButton extends JToggleButton {
	private static final Color COLOR_SELECTED = Color.decode("0x14a9ff");
	private static final Color BACKGROUND_COLOR = Color.decode("0x45433B");
	private Assets.SurfaceTextures textureDefinition;

	public GalleryButton(final Assets.SurfaceTextures textureDefinition, final ImageIcon imageIcon) {
		super(imageIcon);
		this.textureDefinition = textureDefinition;
		defineButton(textureDefinition);
	}

	private void defineButton(final Assets.SurfaceTextures textureDefinition) {
		String name = textureDefinition.getName();
		setBorderPainted(false);
		setToolTipText(name);
		setUI(new MetalToggleButtonUI() {
			@Override
			protected Color getSelectColor() {
				return COLOR_SELECTED;
			}
		});
		setBackground(BACKGROUND_COLOR);
	}

	public void applyTexture(final Assets.SurfaceTextures texture, final ImageIcon imageIcon) {
		setIcon(imageIcon);
		textureDefinition = texture;
	}
}
