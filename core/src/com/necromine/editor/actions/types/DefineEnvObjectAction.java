package com.necromine.editor.actions.types;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.necromine.editor.GameMap;
import com.necromine.editor.MapManagerEventsNotifier;
import com.necromine.editor.actions.MappingAction;
import com.necromine.editor.model.elements.PlacedEnvObject;

public class DefineEnvObjectAction extends MappingAction {
	private final static Vector3 auxVector = new Vector3();
	private final float height;
	private final PlacedEnvObject element;

	public DefineEnvObjectAction(final GameMap map, final PlacedEnvObject element, final float height) {
		super(map);
		this.element = element;
		this.height = height;
	}

	@Override
	public void execute(final MapManagerEventsNotifier eventsNotifier) {
		element.setHeight(height);
		Matrix4 transform = element.getModelInstance().transform;
		Vector3 position = transform.getTranslation(auxVector);
		transform.setTranslation(position.x, height, position.z);
		actionDone();
	}

	@Override
	public boolean isProcess() {
		return false;
	}
}
