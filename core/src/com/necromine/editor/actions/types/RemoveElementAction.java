package com.necromine.editor.actions.types;

import com.gadarts.necromine.model.MapNodeData;
import com.necromine.editor.GameMap;
import com.necromine.editor.MapManagerEventsNotifier;
import com.necromine.editor.actions.ActionAnswer;
import com.necromine.editor.actions.AnswerSubscriber;
import com.necromine.editor.actions.MappingAction;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.model.elements.PlacedElement;
import com.necromine.editor.model.elements.PlacedElements;
import com.necromine.editor.model.node.Node;

import java.util.List;
import java.util.stream.Collectors;

public class RemoveElementAction extends MappingAction implements AnswerSubscriber<PlacedElement> {
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
	public void execute(final MapManagerEventsNotifier eventsNotifier) {
		if (mode == EditModes.TILES) {
			removePlacedTile();
		} else {
			removePlacedObject(eventsNotifier);
		}
	}

	private void removePlacedTile() {
		MapNodeData mapNodeData = map.getNodes()[node.getRow()][node.getCol()];
		map.getNodes()[node.getRow()][node.getCol()] = null;
		placedElements.getPlacedTiles().remove(mapNodeData);
	}

	private void removePlacedObject(final MapManagerEventsNotifier eventsNotifier) {
		List<? extends PlacedElement> placedElementsList = this.placedElements.getPlacedObjects().get(mode);
		List<? extends PlacedElement> elementsInTheNode = placedElementsList.stream()
				.filter(placedElement -> placedElement.getNode().equals(node))
				.collect(Collectors.toList());
		if (elementsInTheNode.size() == 1) {
			placedElementsList.remove(elementsInTheNode.get(0));
		} else if (elementsInTheNode.size() > 1) {
			letUserDecide(eventsNotifier, elementsInTheNode);
		}
	}

	private void letUserDecide(final MapManagerEventsNotifier eventsNotifier,
							   final List<? extends PlacedElement> elementsInTheNode) {
		ActionAnswer<PlacedElement> answer = new ActionAnswer<>(this);
		eventsNotifier.nodeSelectedToSelectObjectsInIt(elementsInTheNode, answer);
	}

	@Override
	public boolean isProcess() {
		return false;
	}

	@Override
	public void onAnswerGiven(final PlacedElement data) {
		List<? extends PlacedElement> placedElementsList = this.placedElements.getPlacedObjects().get(mode);
		placedElementsList.remove(data);
		actionDone();
	}

}
