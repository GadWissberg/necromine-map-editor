package com.necromine.editor;

import com.necromine.editor.mode.EditModes;
import com.necromine.editor.model.PlacedElement;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class PlacedElements {
    private final Set<MapNode> placedTiles = new HashSet<>();
    private final Map<EditModes, List<? extends PlacedElement>> placedObjects = new HashMap<>();
}
