package com.necromine.editor.handlers;

import com.necromine.editor.GameMap;
import com.necromine.editor.model.elements.PlacedElements;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ActionHandlerRelatedData {
    private final GameMap map;
    private final PlacedElements placedElements;

}
