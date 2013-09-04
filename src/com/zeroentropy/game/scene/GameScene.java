package com.zeroentropy.game.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.modifier.SingleValueSpanEntityModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.animator.AlphaMenuAnimator;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.modifier.ease.EaseBackIn;
import org.andengine.util.modifier.ease.EaseBackInOut;
import org.andengine.util.modifier.ease.EaseBackOut;
import org.andengine.util.preferences.SimplePreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.zeroentropy.game.ResourcesManager;
import com.zeroentropy.game.handler.world.IScoreboard;
import com.zeroentropy.game.scene.SceneManager.SceneType;
import com.zeroentropy.game.scene.background.Clouds;
import com.zeroentropy.game.world.World;

/**
 * 
 * @author TAZUdesign02
 * @since 2013-02-04-2020
 */
public class GameScene extends BaseScene implements IScoreboard,
		IOnSceneTouchListener, IAccelerationListener, IOnMenuItemClickListener {

	// private int score = 0;

	// private PhysicsWorld physicsWorld;
	// ---------------------------------------------
	// LEVEL LOADER
	// ---------------------------------------------
	// private static final String TAG_ENTITY = "entity";
	// private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	// private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	// private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";
	//
	// private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1 =
	// "platform1";
	// private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2 =
	// "platform2";
	// private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3 =
	// "platform3";
	// private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN =
	// "coin";

	// ---------------------------------------------
	// PLAYER
	// ---------------------------------------------
	// private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER =
	// "player";

	// public IPlayer mPlayer;// = new IPlayer(vbom);

	@Override
	public void createScene() {
		engine.enableAccelerationSensor(activity, this);

		createBackground();

		// createPhysics();
		// createMenuScene();

		loadLevel(1);

		// attachParticleSystem();

		// createRectangle();

		setOnSceneTouchListener(this);

		// createScoreboard();
		createScoreHUD();
	}

	@Override
	public void onBackKeyPressed() {
		// SceneManager.getInstance().loadMenuScene(engine);
		final int pState = mWorld.getState();
		if (pState == World.STATE_START || pState == World.STATE_RESUME
				|| pState == World.STATE_RESTART) {
			mWorld.setState(World.STATE_PAUSE);
		}

	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene() {
		engine.disableAccelerationSensor(activity);
		camera.setHUD(null);
		camera.setChaseEntity(null);

	}

	// private Rectangle myRectangle;
	// private void createRectangle(){
	// myRectangle = new Rectangle(100f, 100f, 250f, 600f, new
	// HighPerformanceLinearGradientRectangleVertexBufferObject(
	// new Color(1.0f, 0, 0), new Color(0, 1.0f, 0),
	// LinearGradientDirection.TOPRIGHT_TO_BOTTOMLEFT,
	// ResourcesManager.getInstance().vbom));//ResourcesManager.getInstance().vbom);
	// // myRectangle.setShaderProgram(MyShaderProgram.getInstance());
	// this.attachChild(myRectangle);
	//
	// }
	// -----------------------------------------------

	final private float mParallaxChangePerSecond = -10.0f;

	private void createBackground() {

		final Sprite pSkySprite = new Sprite(0, 0,
				ResourcesManager.getInstance().mSkyRegion_0, vbom);

		pSkySprite.attachChild(new Sprite(0, 800, ResourcesManager
				.getInstance().mSkyRegion_1, vbom));

		pSkySprite.attachChild(new Sprite(0, 1600, ResourcesManager
				.getInstance().mSkyRegion_2, vbom));

		pSkySprite.attachChild(new Sprite(0, 2400, ResourcesManager
				.getInstance().mSkyRegion_3, vbom));

		final Background Sky = new Background() {

			// private float mParallaxFactor = -2.0f;

			@Override
			public void onUpdate(final float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				mParallaxValue += mParallaxChangePerSecond * pSecondsElapsed;
			}

			@Override
			public void onDraw(final GLState pGLState, final Camera pCamera) {

				pGLState.pushModelViewGLMatrix();
				{
					final float cameraHeight = pCamera.getHeight();
					final float shapeHeightScaled = 3200;// this.pSkySprite.getWidthScaled();
					float baseOffset = mParallaxValue % shapeHeightScaled;

					while (baseOffset > 0) {
						baseOffset -= shapeHeightScaled;
					}
					pGLState.translateModelViewGLMatrixf(0, baseOffset, 0);

					float currentMaxX = baseOffset;

					do {
						pSkySprite.onDraw(pGLState, pCamera);
						pGLState.translateModelViewGLMatrixf(0,
								shapeHeightScaled, 0);
						currentMaxX += shapeHeightScaled;
					} while (currentMaxX < cameraHeight);
				}
				pGLState.popModelViewGLMatrix();

			}

		};

		this.setBackground(Sky);

	}

	private SingleValueSpanEntityModifier mBackgroundModifier;
	private float mParallaxValue;

	public void updateBackground() {
		final float duration = .8f;// .8f;
		final float scale_x_fromValue = mParallaxValue;
		final float scale_x_toValue = mParallaxValue - 800;
		if (mBackgroundModifier == null) {
			mBackgroundModifier = new SingleValueSpanEntityModifier(duration,
					scale_x_fromValue, scale_x_toValue,
					EaseBackInOut.getInstance()) {// EaseBackOut.getInstance()

				@Override
				protected void onSetInitialValue(final IEntity pItem,
						final float pValue) {

					// mRectangle.setScaleX(pValue);

				}

				@Override
				protected void onSetValue(final IEntity pItem,
						final float pPercentageDone, float pValue) {

					// mRectangle.setScaleX(pValue);
					mParallaxValue = pValue;
				}

				@Override
				public SingleValueSpanEntityModifier deepCopy()
						throws org.andengine.util.modifier.IModifier.DeepCopyNotSupportedException {
					return null;
				}

			};
		} else {
			mBackgroundModifier.reset(duration, scale_x_fromValue,
					scale_x_toValue);
		}
		this.registerEntityModifier(mBackgroundModifier);
	}

	/*
	 * private void createPhysics() { physicsWorld = new
	 * FixedStepPhysicsWorld(60, new Vector2(SensorManager.GRAVITY_EARTH,
	 * SensorManager.GRAVITY_EARTH), false); // new FixedStepPhysicsWorld(60,
	 * new Vector2(0, -17), false); this.registerUpdateHandler(physicsWorld); }
	 */

	private World mWorld;

	// private Player mPlayer;

	private void loadLevel(int levelID) {

		this.attachChild(new Clouds(3, 2, 6));

		this.attachChild(mWorld = new World(this));

		this.attachChild(new Clouds(1, 2, 4));

		mWorld.setState(World.STATE_CREATE);
		// mPlayer = new IPlayer();
		// this.attachChild(mPlayer);
		/*
		 * final LevelLoader levelLoader = new LevelLoader();
		 * 
		 * levelLoader.setAssetBasePath("level/");
		 * 
		 * final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0,
		 * 0.01f, 0.5f);
		 * 
		 * levelLoader.registerEntityLoader(LevelConstants.TAG_LEVEL, new
		 * IEntityLoader() {
		 * 
		 * @Override public IEntity onLoadEntity(final String pEntityName, final
		 * Attributes pAttributes) { final int width =
		 * SAXUtils.getIntAttributeOrThrow( pAttributes,
		 * LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH); final int height =
		 * SAXUtils.getIntAttributeOrThrow( pAttributes,
		 * LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
		 * 
		 * camera.setBounds(0, 0, width, height); // here we set // camera
		 * bounds camera.setBoundsEnabled(true);
		 * 
		 * return GameScene.this; } });
		 * 
		 * levelLoader.registerEntityLoader(TAG_ENTITY, new IEntityLoader() {
		 * 
		 * @Override public IEntity onLoadEntity(final String pEntityName, final
		 * Attributes pAttributes) { final int x =
		 * SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
		 * final int y = SAXUtils.getIntAttributeOrThrow(pAttributes,
		 * TAG_ENTITY_ATTRIBUTE_Y);
		 * 
		 * final String type = SAXUtils.getAttributeOrThrow(pAttributes,
		 * TAG_ENTITY_ATTRIBUTE_TYPE);
		 * 
		 * final Sprite levelObject;
		 * 
		 * if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1)) {
		 * levelObject = new Sprite(x, y, resourcesManager.platform1_region,
		 * vbom); PhysicsFactory.createBoxBody(physicsWorld, levelObject,
		 * BodyType.StaticBody, FIXTURE_DEF).setUserData( "platform1"); } else
		 * if (type .equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2)) {
		 * levelObject = new Sprite(x, y, resourcesManager.platform2_region,
		 * vbom); final Body body = PhysicsFactory.createBoxBody( physicsWorld,
		 * levelObject, BodyType.StaticBody, FIXTURE_DEF);
		 * body.setUserData("platform2");
		 * physicsWorld.registerPhysicsConnector(new PhysicsConnector(
		 * levelObject, body, true, false)); } else if (type
		 * .equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3)) { levelObject =
		 * new Sprite(x, y, resourcesManager.platform3_region, vbom); final Body
		 * body = PhysicsFactory.createBoxBody( physicsWorld, levelObject,
		 * BodyType.StaticBody, FIXTURE_DEF); body.setUserData("platform3");
		 * physicsWorld.registerPhysicsConnector(new PhysicsConnector(
		 * levelObject, body, true, false)); } else if
		 * (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN)) { levelObject =
		 * new Sprite(x, y, resourcesManager.coin_region, vbom) {
		 * 
		 * @Override protected void onManagedUpdate(float pSecondsElapsed) {
		 * super.onManagedUpdate(pSecondsElapsed);
		 * 
		 * } }; levelObject.registerEntityModifier(new LoopEntityModifier( new
		 * ScaleModifier(.5f, 1, 1.2f)));
		 * 
		 * } /* else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER)) {
		 * player = new IPlayer(x, y, vbom, camera, physicsWorld) {
		 * 
		 * @Override public void onDie() { } }; levelObject = player; } else {
		 * throw new IllegalArgumentException(); }
		 * 
		 * levelObject.setCullingEnabled(true);
		 * 
		 * return levelObject; } });
		 * 
		 * // levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + //
		 * levelID + ".lvl"); try {
		 * levelLoader.loadLevelFromAsset(activity.getAssets(), "level_" +
		 * levelID + ".lvl"); } catch (final IOException e) { Debug.e(e); }
		 */
	}

	// private int touchCount = 1;
	// private boolean isFirstTouch = false;

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// if (pSceneTouchEvent.isActionUp())
		// scoreText.setText("" + pSceneTouchEvent.getX());
		// if (mWorld.mPlayer != null)
		// mWorld.mPlayer.setState(touchCount++ % 2 + 1);
		// mWorld.mSupplys.attachSupply(0);
		// return false;
		return false;
	}

	// private float mGravity;

	@Override
	public void onAccelerationChanged(AccelerationData pAccelerationData) {

		// final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX(),
		// pAccelerationData.getY());
		// this.physicsWorld.setGravity(gravity);
		// mGravity = pAccelerationData.getX();
		// scoreText.setText("G: " + pAccelerationData.getX());
		if (mWorld != null && mWorld.mPlayer != null) {
			mWorld.mPlayer.setGravity(pAccelerationData.getX());
			// healthText.setText(
			// // "PEACH: "
			// // + mWorld.mPlayer.mPeach
			// // +
			// "InkSize: ");// + mWorld.mInks.size());// score);

			// peachText.setText("DosSize: " + Ink.mDisposedInks.size());//
			// mWorld.mPlayer.getDY());//
			// (int)mWorld.getLevel());//mWorld.getSupplysNum());//
		}
		// Vector2Pool.recycle(gravity);

	}

	@Override
	public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {

	}

	private MenuScene mReadyMenuScene;
	private MenuScene mPauseMenuScene;
	private MenuScene mLoseMenuScene;

	private MoveYModifier mMenuInModifier;
	private MoveYModifier mMenuOutModifier;

	// private final int MENU_READY = 0;
	// private final int MENU_PAUSE = 1;
	// private final int MENU_LOSE = 2;

	private final int BUT_EXIT = 0;
	private final int BUT_INTRO = 1;
	private final int BUT_PAUSE = 2;
	private final int BUT_PLAY = 3;
	private final int BUT_RESUME = 4;
	private final int BUT_RESTART = 5;

	// in LoseMenu, use TextMenuItem to show Score and BestScore...
	// and A SpriteMenuItem to show a supid...
	// Here is their IDs
	private final int ITEM_SCORE = BUT_RESTART + 1;
	private final int ITEM_BEST = ITEM_SCORE + 1;
	private final int ITEM_SCORE_TITLE = ITEM_BEST + 1;

	public void showReadyMenu() {
		// showMenu(MENU_READY);
		if (mReadyMenuScene == null) {
			createReadyMenuScene();
		}
		setChildScene(mReadyMenuScene);
		showMenu();
	}

	public void showPauseMenu() {
		// showMenu(MENU_PAUSE);
		if (mPauseMenuScene == null) {
			createPauseMenuScene();
		}
		setChildScene(mPauseMenuScene);
		showMenu();
	}

	public void showLoseMenu() {
		// showMenu(MENU_LOSE);
		// if (mLoseMenuScene == null) {
		hideScoreHUD();

		createLoseMenuScene();
		// }
		setChildScene(mLoseMenuScene);
		showMenu();
	}

	private void hideScoreHUD() {
		if (mScoreHUD != null) {
			if (mMenuOutModifier == null) {
				createHideMenuModifier();
			} else
				mMenuOutModifier.reset();
			mScoreHUD.registerEntityModifier(mMenuOutModifier);
		}
	}

	private void showScoreHUD() {
		if (mScoreHUD != null) {
			mChildScene.setPosition(0, SCENE_HEIGHT);
			// setChildScene(pMenuScene);

			if (mMenuInModifier == null) {
				createShowMenuModifier();
			} else
				mMenuInModifier.reset();

			mScoreHUD.registerEntityModifier(mMenuInModifier);
		}
	}

	public void hideMenu() {
		if (mChildScene != null) {
			if (mMenuOutModifier == null) {
				createHideMenuModifier();
			} else
				mMenuOutModifier.reset();
			mChildScene.registerEntityModifier(mMenuOutModifier);
		}
	}

	private void showMenu() {
		if (mChildScene != null) {
			mChildScene.setPosition(0, SCENE_HEIGHT);
			// setChildScene(pMenuScene);

			if (mMenuInModifier == null) {
				createShowMenuModifier();
			} else
				mMenuInModifier.reset();

			mChildScene.registerEntityModifier(mMenuInModifier);
		}
	}

	//
	// private void showMenu(final int pMenuType) {
	// // MenuScene pMenuScene = null;
	// switch (pMenuType) {
	// case MENU_READY:
	// if (mReadyMenuScene == null) {
	// createReadyMenuScene();
	// }
	// setChildScene(mReadyMenuScene);
	// break;
	// case MENU_PAUSE:
	// if (mPauseMenuScene == null) {
	// createPauseMenuScene();
	// }
	// setChildScene(mPauseMenuScene);
	// break;
	// case MENU_LOSE:
	// if (mLoseMenuScene == null) {
	// createLoseMenuScene();
	// }
	// setChildScene(mLoseMenuScene);
	// break;
	// default:
	// break;
	// }
	// mChildScene.setPosition(0, SCENE_HEIGHT);
	// // setChildScene(pMenuScene);
	//
	// if (mMenuInModifier == null) {
	// createShowMenuModifier();
	// } else
	// mMenuInModifier.reset();
	// mChildScene.registerEntityModifier(mMenuInModifier);
	// }

	private void createShowMenuModifier() {
		mMenuInModifier = new MoveYModifier(.8f, SCENE_HEIGHT, 0,
				EaseBackOut.getInstance());
	}

	private void createHideMenuModifier() {
		mMenuOutModifier = new MoveYModifier(.8f, 0, -SCENE_HEIGHT,
				EaseBackIn.getInstance());
	}

	private void createReadyMenuScene() {
		mReadyMenuScene = new MenuScene(camera);
		// mReadyMenuScene.setPosition(0, SCENE_HEIGHT);

		final IMenuItem playMenuItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(BUT_PLAY, resourcesManager.mPlayBut, vbom),
				1.1f, 1);
		final IMenuItem introMenuItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(BUT_INTRO, resourcesManager.mIntroBut, vbom),
				1.1f, 1);

		mReadyMenuScene.addMenuItem(playMenuItem);
		mReadyMenuScene.addMenuItem(introMenuItem);

		mReadyMenuScene.buildAnimations();
		mReadyMenuScene.setBackgroundEnabled(false);

		playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY() - 10);
		introMenuItem.setPosition(introMenuItem.getX(),
				introMenuItem.getY() + 10);

		mReadyMenuScene.setOnMenuItemClickListener(this);

		// setChildScene(mReadyMenuScene);
	}

	private void createPauseMenuScene() {
		mPauseMenuScene = new MenuScene(camera);
		// mPauseMenuScene.setPosition(0, SCENE_HEIGHT);

		final IMenuItem resumeMenuItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(BUT_RESUME, resourcesManager.mPlayBut, vbom),
				1.1f, 1);
		// new ScaleMenuItemDecorator (IMenuItem pMenuItem, float
		// pSelectedScale, float pUnselectedScale)
		final IMenuItem introMenuItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(BUT_INTRO, resourcesManager.mIntroBut, vbom),
				1.1f, 1);

		mPauseMenuScene.addMenuItem(resumeMenuItem);
		mPauseMenuScene.addMenuItem(introMenuItem);

		mPauseMenuScene.buildAnimations();
		mPauseMenuScene.setBackgroundEnabled(false);

		resumeMenuItem.setPosition(resumeMenuItem.getX(),
				resumeMenuItem.getY() - 10);
		introMenuItem.setPosition(introMenuItem.getX(),
				introMenuItem.getY() + 10);

		mPauseMenuScene.setOnMenuItemClickListener(this);

		// setChildScene(mPauseMenuScene);
	}

	private TextMenuItem mScoreTextMenuItem, mBestTextMenuItem;

	private void createLoseMenuScene() {
		if (mActulScore > getBestScore()) {
			// mBestScore = mActulScore;
			// mPreferences.edit().putInt(PREFERENCES_BEST_SCORE_ID,
			// mBestScore).commit();
			setBestScore(mActulScore);
		}

		mLoseMenuScene = new MenuScene(camera);
		mLoseMenuScene.setMenuAnimator(new AlphaMenuAnimator(1.0f));
		// mLoseMenuScene.setPosition(0, SCENE_HEIGHT);

		final IMenuItem restartMenuItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(BUT_RESTART, resourcesManager.mPlayBut, vbom),
				1.1f, 1);
		// new ScaleMenuItemDecorator (IMenuItem pMenuItem, float
		// pSelectedScale, float pUnselectedScale)
		// final IMenuItem introMenuItem = new ScaleMenuItemDecorator(
		// new SpriteMenuItem(BUT_INTRO, resourcesManager.mIntroBut, vbom),
		// 1.1f, 1);

		final IMenuItem scoreTitleMenuItem = new SpriteMenuItem(
				ITEM_SCORE_TITLE, resourcesManager.lose_score_title_region,
				vbom);
		mScoreTextMenuItem = new TextMenuItem(ITEM_BEST,
				ResourcesManager.getInstance().xhFont, "" + mActulScore,
				new TextOptions(HorizontalAlign.CENTER),
				ResourcesManager.getInstance().vbom);
		// ResourcesManager.getInstance().vbom);
		// final float height = score.getHeight();
		mBestTextMenuItem = new TextMenuItem(ITEM_BEST,
				ResourcesManager.getInstance().lFont,
				"BEST: " + getBestScore(), new TextOptions(
						HorizontalAlign.CENTER),
				ResourcesManager.getInstance().vbom);
		// final IMenuItem squidMenuItem = new SpriteMenuItem(ITEM_SQUID,
		// resourcesManager.mItemSquid, vbom);

		//
		mLoseMenuScene.addMenuItem(scoreTitleMenuItem);
		mLoseMenuScene.addMenuItem(mScoreTextMenuItem);
		mLoseMenuScene.addMenuItem(mBestTextMenuItem);

		mLoseMenuScene.addMenuItem(restartMenuItem);//

		mLoseMenuScene.buildAnimations();
		mLoseMenuScene.setBackgroundEnabled(false);

		mScoreTextMenuItem.setY(mScoreTextMenuItem.getY() - 20);
		mBestTextMenuItem.setY(mBestTextMenuItem.getY() - 10);
		restartMenuItem.setY(restartMenuItem.getY() + 30);
		// introMenuItem.setPosition(introMenuItem.getX(),
		// introMenuItem.getY() + 10);

		mLoseMenuScene.setOnMenuItemClickListener(this);

		// setChildScene(mLoseMenuScene);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
		case BUT_EXIT:
			// SceneManager.getInstance().loadGameScene(engine);
			return true;
		case BUT_INTRO:
			mWorld.mPlayer.onFlash();
			return true;
		case BUT_PAUSE:
			return true;
		case BUT_PLAY:

			mWorld.setState(World.STATE_START);
			return true;
		case BUT_RESUME:
			// SceneManager.getInstance().loadGameScene(engine);
			mWorld.setState(World.STATE_RESUME);
			return true;
		case BUT_RESTART:
			initScore();
			showScoreHUD();
			mWorld.setState(World.STATE_RESTART);
			return true;
		case ITEM_SCORE:
			return false;
		case ITEM_BEST:
			return false;
		case ITEM_SCORE_TITLE:
			return false;

		default:
			return false;
		}
	}

	// ---------------------------------------------------

	private static final String PREFERENCES_BEST_SCORE_ID = "preferences.best_score";
	// SharedPreferences mPreferences;

	private HUD mScoreHUD;

	private Text text_Score;
	private Text text_Best;
	private Text text_Temp;

	private Entity mHitsComboDisplay;// position
	private Text text_HitsCombo;
	// private Text text_HitsNum;
	// private Text text_Points;

	private int mScore = 0;
	public int mActulScore = 0;

	// public int mBestScore = 0;

	public void setTempText(final String string) {
		text_Temp.setText(string);
	}

	private void createScoreHUD() {
		mScoreHUD = new HUD();

		float x = 20;
		float y = 10;
		text_Score = new Text(x, y, ResourcesManager.getInstance().hFont,
				"0123456789", new TextOptions(HorizontalAlign.LEFT),
				ResourcesManager.getInstance().vbom);

		y += text_Score.getHeight() - 5;
		text_Best = new Text(x, y, ResourcesManager.getInstance().lFont,
				"BEST: 0123456789", new TextOptions(HorizontalAlign.LEFT),
				ResourcesManager.getInstance().vbom);

		y += text_Best.getHeight();
		text_Temp = new Text(x, y, ResourcesManager.getInstance().lFont,
				"Enemies: 0123456789", new TextOptions(HorizontalAlign.LEFT),
				ResourcesManager.getInstance().vbom);

		mScoreHUD.attachChild(text_Score);
		mScoreHUD.attachChild(text_Best);
		mScoreHUD.attachChild(text_Temp);

		mHitsComboDisplay = new Entity(-100, -100);
		text_HitsCombo = new Text(0, 0, ResourcesManager.getInstance().mFont,
				"0  HITS\nCOMBO\n+  0",
				new TextOptions(HorizontalAlign.CENTER),
				ResourcesManager.getInstance().vbom);
		text_HitsCombo.setSkewCenter(0, 0);

		final float half_w = text_HitsCombo.getWidth() * .5f;
		final float half_h = text_HitsCombo.getHeight() * .5f;
		text_HitsCombo.setPosition(-half_w, -half_h);

		//
		mHitsComboDisplay.attachChild(text_HitsCombo);

		mScoreHUD.attachChild(mHitsComboDisplay);

		initScore();

		camera.setHUD(mScoreHUD);
	}

	private TimerHandler plusOneHandler;

	// private int overplus = 0;

	private void initScore() {

		// mPreferences =
		// SimplePreferences.getInstance(ResourcesManager.getInstance().activity);

		mScore = 0;
		mActulScore = 0;
		// mBestScore =
		// getBestScore();//mPreferences.getInt(PREFERENCES_BEST_SCORE_ID,
		// 0);//128;

		text_Score.setText("0");
		text_Best.setText("BEST: " + getBestScore());
		// text_Temp.setText("Enemies: 0");

		if (plusOneHandler == null)
			this.registerUpdateHandler(plusOneHandler = new TimerHandler(
					0.125f, false, new ITimerCallback() {

						@Override
						public void onTimePassed(TimerHandler pTimerHandler) {
							// TODO Auto-generated method stub
							// plusOneScore();
							final int actulScore = mActulScore;
							if (mScore < actulScore) {
								mScore++;
								plusOneHandler.reset();
								// text_Score.setText("" + mScore);
							} else if (mScore > actulScore) {
								mScore--;
								plusOneHandler.reset();
							}
							// if (mScore != actulScore)
							// plusOneHandler.reset();
							text_Score.setText("" + mScore);
						}

					}));
	}

	@Override
	public void addScore() {
		mActulScore++;
		if (plusOneHandler.isTimerCallbackTriggered())
			plusOneHandler.reset();
	}

	private IEntityModifier mHitsComboFadeOut;

	@Override
	public void addScore(int score, float x, float y) {

		text_HitsCombo.setText("" + score + "  HITS\nCOMBO\n+  " + score);
		mHitsComboDisplay.setPosition(x, y);

		if (mHitsComboFadeOut == null) {// new FadeOutModifier(2.0f);
			mHitsComboFadeOut = new SequenceEntityModifier(new DelayModifier(
					.75f), new ParallelEntityModifier(new ScaleModifier(.25f,
					1.0f, 1.5f), new FadeOutModifier(.25f)));

			mHitsComboFadeOut.setAutoUnregisterWhenFinished(false);
			text_HitsCombo.registerEntityModifier(mHitsComboFadeOut);
		} else {
			text_HitsCombo.setAlpha(1.0f);
			text_HitsCombo.setScale(1.0f);
			mHitsComboFadeOut.reset();
		}

		mActulScore += score;
		if (plusOneHandler.isTimerCallbackTriggered())
			plusOneHandler.reset();
	}

	// private SharedPreferences mBestScorePreferences;
	private int getBestScore() {
		SharedPreferences prefs = activity.getSharedPreferences(
				PREFERENCES_BEST_SCORE_ID, Context.MODE_PRIVATE);// PreferenceManager.getDefaultSharedPreferences(activity);
		return prefs.getInt(PREFERENCES_BEST_SCORE_ID, 0);
	}

	private boolean setBestScore(final int newValue) {
		SharedPreferences prefs = activity.getSharedPreferences(
				PREFERENCES_BEST_SCORE_ID, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(PREFERENCES_BEST_SCORE_ID, newValue);
		return editor.commit();
	}
}
