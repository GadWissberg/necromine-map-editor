package com.necromine.editor.handlers;

import com.necromine.editor.model.GameMap;
import com.necromine.editor.model.elements.PlacedElements;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HandlersManagerRelatedData {
	private final GameMap map;
	private final PlacedElements placedElements;


}
