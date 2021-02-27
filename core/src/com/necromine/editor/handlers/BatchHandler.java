package com.necromine.editor.handlers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import lombok.Getter;

@Getter
public class BatchHandler implements Disposable {
	private static final int DECALS_POOL_SIZE = 200;
	private static final Vector3 auxVector3_1 = new Vector3();
	private ModelBatch modelBatch;
	private DecalBatch decalBatch;

	public void renderDecal(final Decal decal, final Camera camera) {
		decal.lookAt(auxVector3_1.set(decal.getPosition()).sub(camera.direction), camera.up);
		decalBatch.add(decal);
	}

	void createBatches(final Camera camera) {
		CameraGroupStrategy groupStrategy = new CameraGroupStrategy(camera);
		this.decalBatch = new DecalBatch(DECALS_POOL_SIZE, groupStrategy);
		this.modelBatch = new ModelBatch();
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		decalBatch.dispose();
	}
}
