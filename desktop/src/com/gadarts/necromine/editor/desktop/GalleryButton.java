package com.gadarts.necromine.editor.desktop;

import com.gadarts.necromine.assets.Assets;
import lombok.Getter;

import javax.swing.*;
import javax.swing.plaf.metal.MetalToggleButtonUI;
import java.awt.*;

@Getter
public class GalleryButton extends JToggleButton {
	private static final Color COLOR_SELECTED = Color.decode("0x14a9ff");
	private static final int WIDTH = 75;
	private static final int HEIGHT = 25;
	private final Assets.FloorsTextures textureDefinition;

	public GalleryButton(final Assets.FloorsTextures textureDefinition, final ImageIcon imageIcon) {
		super(imageIcon);
		this.textureDefinition = textureDefinition;
		defineButton(textureDefinition);
	}

	private void defineButton(final Assets.FloorsTextures textureDefinition) {
		String name = textureDefinition.getName();
		setText(name);
		setBorder(BorderFactory.createEtchedBorder());
		setHorizontalTextPosition(JButton.CENTER);
		setVerticalTextPosition(JButton.BOTTOM);
		setToolTipText(name);
		setUI(new MetalToggleButtonUI() {
			@Override
			protected Color getSelectColor() {
				return COLOR_SELECTED;
			}
		});
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}

}
