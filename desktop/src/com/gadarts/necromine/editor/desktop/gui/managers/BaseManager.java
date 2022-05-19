package com.gadarts.necromine.editor.desktop.gui.managers;

import com.necromine.editor.MapRenderer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class BaseManager {
	private final MapRenderer mapRenderer;

}
