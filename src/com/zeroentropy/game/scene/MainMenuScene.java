package com.zeroentropy.game.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.opengl.util.GLState;

import com.zeroentropy.game.scene.SceneManager.SceneType;

/**
 * 
 * @author TAZUdesign02
 *
 */
public class MainMenuScene extends BaseScene implements
		IOnMenuItemClickListener {

	@Override
	public void createScene() {
		this.createBackground();
		this.createMenuChildScene();
	}

	@Override
	public void onBackKeyPressed() {
		System.exit(0);// ???
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub

	}

	private void createBackground() {
		attachChild(new Sprite(0, 0,
				resourcesManager.bg_title_region, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});
	}

	private MenuScene menuChildScene;
	private final int MENU_PLAY = 0;
//	private final int MENU_OPTIONS = 1;

	private void createMenuChildScene() {
		menuChildScene = new MenuScene(camera);
		menuChildScene.setPosition(0, 0);

		final IMenuItem playMenuItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_PLAY, resourcesManager.but_play_mainmenu_region,
						vbom), 1.1f, 1);
		// new ScaleMenuItemDecorator (IMenuItem pMenuItem, float
		// pSelectedScale, float pUnselectedScale)
//		final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(
//				new SpriteMenuItem(MENU_OPTIONS,
//						resourcesManager.options_region, vbom), 1.1f, 1);

		menuChildScene.addMenuItem(playMenuItem);
//		menuChildScene.addMenuItem(optionsMenuItem);

		menuChildScene.buildAnimations();
		menuChildScene.setBackgroundEnabled(false);

		playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY() + 200);
//		optionsMenuItem.setPosition(optionsMenuItem.getX(), optionsMenuItem.getY()+10);

		menuChildScene.setOnMenuItemClickListener(this);

		setChildScene(menuChildScene);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
		case MENU_PLAY:
			SceneManager.getInstance().loadGameScene(engine);
			return true;
//		case MENU_OPTIONS:
//			return true;
		default:
			return false;
		}
	}
	
}
