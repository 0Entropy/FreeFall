package com.zeroentropy.game.scene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.zeroentropy.game.GameActivity;
import com.zeroentropy.game.ResourcesManager;
import com.zeroentropy.game.scene.SceneManager.SceneType;
import com.zeroentropy.game.util.ZeroConstants;

public abstract class BaseScene extends Scene implements ZeroConstants {

	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	protected Engine engine;
	protected GameActivity activity;
	protected ResourcesManager resourcesManager;
	protected VertexBufferObjectManager vbom;
	protected BoundCamera camera;

	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------

	public BaseScene() {
		this.resourcesManager = ResourcesManager.getInstance();
		this.engine = resourcesManager.engine;
		this.activity = resourcesManager.activity;
		this.vbom = resourcesManager.vbom;
		this.camera = resourcesManager.camera;
		createScene();
	}

	// ---------------------------------------------
	// ABSTRACTION
	// ---------------------------------------------

	public abstract void createScene();

	public abstract void onBackKeyPressed();

	public abstract SceneType getSceneType();

	public abstract void disposeScene();
	
	// ---------------------------------------------
	// TEST
	// ---------------------------------------------
	
	//public abstract void onAccelerationChanged(AccelerationData pAccelerationData);
}
