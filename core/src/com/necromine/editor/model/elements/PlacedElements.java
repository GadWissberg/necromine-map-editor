package com.necromine.editor.model.elements;

import com.gadarts.necromine.model.MapNodeData;
import com.necromine.editor.mode.EditModes;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class PlacedElements {
    private final Set<MapNodeData> placedTiles = new HashSet<>();
    private final Map<EditModes, List<? extends PlacedElement>> placedObjects = new HashMap<>();
}
