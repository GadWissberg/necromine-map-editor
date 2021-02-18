package com.necromine.editor.actions.types;

import com.necromine.editor.EditModes;
import com.necromine.editor.GameMap;
import com.necromine.editor.MapNode;
import com.necromine.editor.Node;
import com.necromine.editor.PlacedElements;
import com.necromine.editor.actions.MappingAction;
import com.necromine.editor.model.PlacedElement;

import java.util.List;

public class RemoveElementAction extends MappingAction {
    private final PlacedElements placedElements;
    private final Node node;
    private final EditModes mode;

    public RemoveElementAction(final GameMap map,
                               final PlacedElements placedElements,
                               final Node node,
                               final EditModes mode) {
        super(map);
        this.placedElements = placedElements;
        this.node = node;
        this.mode = mode;
    }

    @Override
    protected void execute() {
        if (mode == EditModes.TILES) {
            removePlacedTile();
        } else {
            removePlacedObject();
        }
    }

    private void removePlacedTile() {
        MapNode mapNode = map.getTiles()[node.getRow()][node.getCol()];
        map.getTiles()[node.getRow()][node.getCol()] = null;
        placedElements.getPlacedTiles().remove(mapNode);
    }

    private void removePlacedObject() {
        List<? extends PlacedElement> placedElementsList = this.placedElements.getPlacedObjects().get(mode);
        placedElementsList.stream()
                .filter(placedElement -> placedElement.getNode().equals(node))
                .findFirst()
                .ifPresent(placedElementsList::remove);
    }

    @Override
    public boolean isProcess() {
        return false;
    }
}
