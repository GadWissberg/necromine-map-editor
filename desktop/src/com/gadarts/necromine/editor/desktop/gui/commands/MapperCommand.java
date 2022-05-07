package com.gadarts.necromine.editor.desktop.gui.commands;

import com.gadarts.necromine.editor.desktop.gui.Managers;
import com.necromine.editor.MapRenderer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.event.ActionListener;

@RequiredArgsConstructor
@Getter
public abstract class MapperCommand implements ActionListener {
	private final MapRenderer mapRenderer;
	private final Managers managers;

}
