package com.necromine.editor.handlers.action;

import com.necromine.editor.model.GameMap;
import com.necromine.editor.model.elements.PlacedElements;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ActionHandlerRelatedData {
    private final GameMap map;
    private final PlacedElements placedElements;

}
