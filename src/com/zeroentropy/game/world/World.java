package com.zeroentropy.game.world;

import java.util.ArrayList;
import java.util.LinkedList;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.util.GLState;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

import android.opengl.GLES20;

import com.zeroentropy.game.ResourcesManager;
import com.zeroentropy.game.handler.IPlayerCollidable;
import com.zeroentropy.game.handler.PlayerCollisionHandler;
import com.zeroentropy.game.handler.enemy.RunnySquidHandler;
import com.zeroentropy.game.handler.world.IInkAttachable;
import com.zeroentropy.game.handler.world.IScoreboard;
import com.zeroentropy.game.scene.GameScene;
import com.zeroentropy.game.sprite.Bonus;
import com.zeroentropy.game.sprite.Enemy;
import com.zeroentropy.game.sprite.Ink;
import com.zeroentropy.game.sprite.Player;

public class World extends Entity implements IInkAttachable {// ,ISpawnable
																// {

	public World(final GameScene pGameScene) {
		mGameScene = pGameScene;
		RunnySquidHandler.getInstance().registerInkAttachable(this);
		attachChild(Enemy.mParticleSystemEntity);
		// attachChild(HealthVisual.CONTEXT);
	}

	private int mState = -1;
	private GameScene mGameScene;

	public final static int STATE_CREATE = 0;
	public final static int STATE_START = 1;
	public final static int STATE_RESUME = 2;
	public final static int STATE_PAUSE = 3;

	public final static int STATE_STOP = 4;
	public final static int STATE_RESTART = 6;

	public final static int STATE_DESTROY = 7;

	public int getState() {
		return mState;
	}

	public void setState(final int pState) {
		mState = pState;
		switch (pState) {
		case STATE_CREATE:

			onCreate();

			mPlayer.doCreate();
			mPlayer.doInit();

			mGameScene.showReadyMenu();
			break;
		case STATE_START:

			onStart();
			mPlayer.doStart();// .setState(Player.STATE_FALL);

			mGameScene.hideMenu();
			break;
		case STATE_RESUME:

			onResume();

			mGameScene.hideMenu();
			break;
		case STATE_PAUSE:
			onPause();
			mGameScene.showPauseMenu();
			break;

		case STATE_STOP:

			onStop();

			mPlayer.doStop();
			mGameScene.showLoseMenu();
			break;
		case STATE_RESTART:

			onStart();

			mPlayer.doInit();

			mGameScene.hideMenu();
			break;
		case STATE_DESTROY:
			break;
		default:
			break;
		}
	}

	private void onCreate() {

		createNewPlayer();

		createEnemyStorge();

		creatInkStorge();

		createSupplyStorge();

		// createScoreboard();
	}

	private void onStart() {

		mLevel = 0;
		mGameScene.setTempText("Level: "+mLevel);
		
		min_level = 0;
		// mScore = 0;
		// text_Score.setText("" + mScore);

		// initScore();

		addEnemies(initSize);

		registerLevelUpTimerHandler();

		registerCollisionHandler();

		registerBonusHandler();

		registerGameOverhandler();

		reduisDisposedInksSize();
		// onResume();
		this.mIgnoreUpdate = false;
	}

	private void onResume() {

		// this.mIgnoreUpdate = false;
		mPlayer.setIgnoreUpdate(false);
		mEnemyStorge.setIgnoreUpdate(false);
		mInkStorge.setIgnoreUpdate(false);

		mBonusHandler.setTimerCallbackTriggered(false);
		mLevelUpTimerHandler.setTimerCallbackTriggered(false);
	}

	// ...
	// ...Running...
	// ...

	private void onPause() {

		// this.mIgnoreUpdate = true;
		mPlayer.setIgnoreUpdate(true);
		mEnemyStorge.setIgnoreUpdate(true);
		mInkStorge.setIgnoreUpdate(true);

		mBonusHandler.setTimerCallbackTriggered(true);
		mLevelUpTimerHandler.setTimerCallbackTriggered(true);
	}

	// When lossed or gameover...
	private void onStop() {
		// onPause();

		clearInkStorge();

		clearEnemyStorge();

		// if (mLevelUpTimerHandler != null
		// && !mLevelUpTimerHandler.isTimerCallbackTriggered()) {
		// mLevelUpTimerHandler.setTimerCallbackTriggered(true);
		// }
		// this.mIgnoreUpdate = true;
		// mGameOverHandler.setTimerCallbackTriggered(true);
		mBonusHandler.setTimerCallbackTriggered(true);
		// mCollisionHandler.setTimerCallbackTriggered(true);
		mLevelUpTimerHandler.setTimerCallbackTriggered(true);
	}

	// After onStop()... use onRestart()... to start again...
	private void onRestart() {
		onStart();
	}

	private void onDestroy() {

	}

	// ------------------------------------------------------------
	// Ìí¼ÓÐÂÍæ¼Ò
	// ------------------------------------------------------------

	public Player mPlayer;

	private void createNewPlayer() {
		mPlayer = new Player();
		mPlayer.setScoreboard(mGameScene);
		this.attachChild(mPlayer);
		// mPlayer.doCreate();//.setState(Player.STATE_READY);
	}

	// -------------------------------------------------------------

	// ------------------------------------------------------------
	// GameOver¿ØÖÆÆ÷
	// ------------------------------------------------------------
	private IUpdateHandler mGameOverHandler;

	private void registerGameOverhandler() {
		if (mGameOverHandler == null) {
			mGameOverHandler = new IUpdateHandler() {

				@Override
				public void onUpdate(float pSecondsElapsed) {

					if (mPlayer.isOut() && mState != STATE_STOP) {
						World.this.setState(STATE_STOP);
					}
				}

				@Override
				public void reset() {

				}

			};
			World.this.registerUpdateHandler(mGameOverHandler);
		}
	}

	// ------------------------------------------------------------
	// ½±Àø¿ØÖÆÆ÷
	// ------------------------------------------------------------
	private TimerHandler mBonusHandler;

	private void registerBonusHandler() {
		if (mBonusHandler == null) {
			mBonusHandler = new TimerHandler(2, true, new ITimerCallback() {
				@Override
				public void onTimePassed(final TimerHandler pTimerhandler) {
					if (mState == STATE_START || mState == STATE_RESTART) {
						final int pType = MathUtils.random(3);
						boolean isFulfilled = false;

						switch (pType) {
						case Bonus.TYPE_PEACH:
							isFulfilled = mPlayer.checkPeach();
							break;
						case Bonus.TYPE_EYE:
							if (isHidden())
								isFulfilled = mPlayer.checkVision();
							break;
						case Bonus.TYPE_MANTRA:
							isFulfilled = mPlayer.checkMantra();
							break;
						default:
							break;

						}
						if (isFulfilled)
							attachBonus(pType);
					}
				}
			});
			World.this.registerUpdateHandler(mBonusHandler);
		} else {
			mBonusHandler.reset();
		}
	}

	// ------------------------------------------------------------
	// Åö×²¿ØÖÆÆ÷
	// ------------------------------------------------------------

	private PlayerCollisionHandler mCollisionHandler;
	private ArrayList<IPlayerCollidable> mPlayerCollidables = new ArrayList<IPlayerCollidable>();

	private void registerCollisionHandler() {
		if (mCollisionHandler == null) {
			mCollisionHandler = new PlayerCollisionHandler(mPlayer,
					mPlayerCollidables);
			this.registerUpdateHandler(mCollisionHandler);
		} else {
			mCollisionHandler.reset();
		}

	}

	// ------------------------------------------------------------
	// Éý¼¶¿ØÖÆÆ÷
	// ------------------------------------------------------------

	private int mLevel = 0;
	private static final int MAX_LEVEL = 6;
	private int min_level = 0;

	private TimerHandler mLevelUpTimerHandler;
	private float[] mLevelUpTimes = { 1, 1, 2, 4, 8, 12, 24 };// { 10, 10, 20,
																// 40,
																// 80, 160 };

	// public int getLevel() {
	// return mLevel;
	// }

	private void registerLevelUpTimerHandler() {

		if (mLevelUpTimerHandler == null) {
			mLevelUpTimerHandler = new TimerHandler(3, false,
					new ITimerCallback() {

						@Override
						public void onTimePassed(TimerHandler pTimerHandler) {

							// if (mLevel <= MAX_LEVEL) {
							// mLevel++;
							//
							// mLevelUpTimerHandler
							// .setTimerSeconds(mLevelUpTimes[mLevel]);
							// } else {
							// mLevelUpTimerHandler.setTimerSeconds(12);
							// addEnemy(Enemy.TYPE_ANGLER);
							// }
							mLevel++;

							
							if (mLevel > MAX_LEVEL) {
								// addEnemy(Enemy.TYPE_ANGLER);
								// min_level++;
								if (min_level < MAX_LEVEL)
									mLevel = min_level++;
								else
									mLevel = MAX_LEVEL;

							}
							// mLevel %= (Enemy.TYPE_PUFFER+1);
							// mGameScene.updateBackground();
							mGameScene.setTempText("LEVEL: " + mLevel);
							mLevelUpTimerHandler.reset();
						}

					});
			this.registerUpdateHandler(mLevelUpTimerHandler);
		} else {
			mLevelUpTimerHandler.reset();
		}
	}

	// ------------------------------------------------------------

	// ------------------------------------------------------------
	// SupplyStore ²¹³äÎïÆ·²Ö¿â
	// ------------------------------------------------------------

	// public int getSupplysNum() {
	// return mSupplyStorge.getChildCount();
	// }

	// public final static int TAG_EYE = 0;
	// public final static int TAG_MANTRA = 1;
	// public final static int TAG_PEACH = 2;
	// public final static int TAG_FISH = 3;
	// public final static int TAG_DOUBLE = 4;

	private Entity mSupplyStorge;
	private ArrayList<Bonus> mSupplies = new ArrayList<Bonus>();

	private void attachBonus(final int pType) {
		Bonus pBonus = Bonus.mDisposedChilden.poll();

		final float x = MathUtils.random(ResourcesManager.getInstance().camera
				.getSurfaceWidth());

		if (pBonus == null) {

			pBonus = new Bonus(x, pType);
			mSupplyStorge.attachChild(pBonus);
			mSupplies.add(pBonus);
			mPlayerCollidables.add(pBonus);
		} else {

			pBonus.setPosition(x, 0);
			// pSupply.setTag(pTag);
			pBonus.onChangeHandler(pType);

		}
		pBonus.onManagedAttached();
	}

	private void createSupplyStorge() {
		mSupplyStorge = new Entity();
		this.attachChild(mSupplyStorge);
	}

	// ------------------------------------------------------------
	// InkStore Ä«¼£²Ö¿â
	// ------------------------------------------------------------

	private Entity mInkStorge;
	private Entity mInkBackgroundStorge;
	private ArrayList<Ink> mInks = new ArrayList<Ink>();
	private static final int MAX_DISPOSEDLIST_SIZE = 50;

	private boolean isHidden() {
		return mInks.size() - Ink.mDisposedInks.size() > 5;
	}

	private void reduisDisposedInksSize() {
		// int extraNum = 0;
		if (Ink.mDisposedInks.size() > MAX_DISPOSEDLIST_SIZE) {
			int extraNum = Ink.mDisposedInks.size() - MAX_DISPOSEDLIST_SIZE;
			while (extraNum-- > 0) {
				Ink temp = Ink.mDisposedInks.poll();
				mInks.remove(temp);
				if (temp.detachSelf())
					temp.getBackground().detachSelf();
				// temp.detachSelf();
			}
		}
		// return extraNum;
	}

	@Override
	public void attachInk(final float pX, final float pY) {
		Ink pInk = Ink.mDisposedInks.poll();
		if (pInk == null) {
			// final Ink
			pInk = new Ink(pX, pY);
			mInkStorge.attachChild(pInk);
			mInks.add(pInk);

			mInkBackgroundStorge.attachChild(pInk.getBackground());
		} else {
			pInk.setPosition(pX, pY);
			pInk.getBackground().setPosition(pX, pY);
		}

		pInk.onManagedCreate();
	}

	private void clearInkStorge() {
		if (mInks.size() > 0) {

			for (int i = 0; i < mInks.size(); i++) {
				try {
					final int mI = i;
					mInks.get(mI).onManagedDestroy();
				} catch (Exception e) {
					Debug.d("SPK - INK DOES NOT WANT TO DESTROY: " + e);
				}
			}
		}

		// mInks.clear();
	}

	private void creatInkStorge() {
		mInkBackgroundStorge = new Entity();
		this.attachChild(mInkBackgroundStorge);

		mInkStorge = new Entity() {
			// private boolean mClippingEnabled = true;

			@Override
			protected void onManagedDraw(final GLState pGLState,
					final Camera pCamera) {

				if (mPlayer.getVisionWidth() > 0) { // mClippingEnabled

					/* Enable scissor test, while remembering previous state. */
					final boolean wasScissorTestEnabled = pGLState
							.enableScissorTest();

					final int surfaceHeight = pCamera.getSurfaceHeight();
					final int surfaceWidth = pCamera.getSurfaceWidth();
					//
					// /* In order to apply clipping, we need to determine the
					// the axis
					// aligned bounds in OpenGL coordinates. */
					//
					// /* Determine clipping coordinates of each corner in
					// surface
					// coordinates. */
					// final float[] lowerLeftSurfaceCoordinates =
					// pCamera.getCameraSceneCoordinatesFromSceneCoordinates(this.convertLocalToSceneCoordinates(0,
					// 0));
					// final int lowerLeftX = (int)
					// Math.round(lowerLeftSurfaceCoordinates[Constants.VERTEX_INDEX_X]);
					// final int lowerLeftY = surfaceHeight - (int)
					// Math.round(lowerLeftSurfaceCoordinates[Constants.VERTEX_INDEX_Y]);
					//
					// final float[] upperLeftSurfaceCoordinates =
					// pCamera.getCameraSceneCoordinatesFromSceneCoordinates(this.convertLocalToSceneCoordinates(0,
					// this.mHeight));
					// final int upperLeftX = (int)
					// Math.round(upperLeftSurfaceCoordinates[Constants.VERTEX_INDEX_X]);
					// final int upperLeftY = surfaceHeight - (int)
					// Math.round(upperLeftSurfaceCoordinates[Constants.VERTEX_INDEX_Y]);
					//
					// final float[] upperRightSurfaceCoordinates =
					// pCamera.getCameraSceneCoordinatesFromSceneCoordinates(this.convertLocalToSceneCoordinates(this.mWidth,
					// this.mHeight));
					// final int upperRightX = (int)
					// Math.round(upperRightSurfaceCoordinates[Constants.VERTEX_INDEX_X]);
					// final int upperRightY = surfaceHeight - (int)
					// Math.round(upperRightSurfaceCoordinates[Constants.VERTEX_INDEX_Y]);
					//
					// final float[] lowerRightSurfaceCoordinates =
					// pCamera.getCameraSceneCoordinatesFromSceneCoordinates(this.convertLocalToSceneCoordinates(this.mWidth,
					// 0));
					// final int lowerRightX = (int)
					// Math.round(lowerRightSurfaceCoordinates[Constants.VERTEX_INDEX_X]);
					// final int lowerRightY = surfaceHeight - (int)
					// Math.round(lowerRightSurfaceCoordinates[Constants.VERTEX_INDEX_Y]);
					//
					// /* Determine minimum and maximum x clipping coordinates.
					// */
					// final int minClippingX = MathUtils.min(lowerLeftX,
					// upperLeftX,
					// upperRightX, lowerRightX);
					// final int maxClippingX = MathUtils.max(lowerLeftX,
					// upperLeftX,
					// upperRightX, lowerRightX);
					//
					// /* Determine minimum and maximum y clipping coordinates.
					// */
					// final int minClippingY = MathUtils.min(lowerLeftY,
					// upperLeftY,
					// upperRightY, lowerRightY);
					// final int maxClippingY = MathUtils.max(lowerLeftY,
					// upperLeftY,
					// upperRightY, lowerRightY);
					//
					// /* Determine clipping width and height. */
					// final int clippingWidth = maxClippingX - minClippingX;
					// final int clippingHeight = maxClippingY - minClippingY;

					/* Finally apply the clipping. */
					// GLES20.glScissor(minClippingX, minClippingY,
					// clippingWidth,
					// clippingHeight);

					final float blackCenterX = mPlayer.getX();// .getX();
					// final float blackCenterY =
					// World.getInstance().mPlayer.getRotationCenterY();
					final float blackWidth2 = mPlayer.getVisionWidth() / 2.0f;

					final int leftWidth = Math
							.round(blackCenterX - blackWidth2);
					final int rightWidth = Math.round(surfaceWidth
							- blackCenterX - blackWidth2);

					if (leftWidth > 0) {
						GLES20.glScissor(0, 0, leftWidth, surfaceHeight);
						super.onManagedDraw(pGLState, pCamera);
					}
					if (rightWidth > 0) {
						final int rightX = Math.round(blackCenterX
								+ blackWidth2);
						GLES20.glScissor(rightX, 0, rightWidth, surfaceHeight);
						// Math.round(((clipX + point.x)) * screenRatioX),
						// Math.round((cameraH - ((clipY + point.y) + clipH)) *
						// screenRatioY),
						// Math.round(clipW * screenRatioX),
						// Math.round(clipH * screenRatioY));

						/* Draw children, etc... */
						super.onManagedDraw(pGLState, pCamera);
					}

					/* Revert scissor test to previous state. */
					pGLState.setScissorTestEnabled(wasScissorTestEnabled);
				} else {
					super.onManagedDraw(pGLState, pCamera);
				}
			}
		};
		this.attachChild(mInkStorge);
	}

	// Ä«¼£½áÊø--------------------------

	// ------------------------------------------------------------
	// ENEMY µÐÈË²¿·Ö
	// ------------------------------------------------------------
	private Entity mEnemyStorge;
	private LinkedList<Enemy> mEnemies = new LinkedList<Enemy>();
	// public SupplyStorage mSupplys;

	private static final int initSize = 5;

	// public int getEnemiesNum(){
	// return mEnemies.size();
	// }
	//
	private void createEnemyStorge() {
		mEnemyStorge = new Entity();
		this.attachChild(mEnemyStorge);
		// this.attachChild(Enemy.mParticleSystemEntity);
	}

	private void clearEnemyStorge() {
		if (mEnemies.size() > 0) {

			for (int i = 0; i < mEnemies.size(); i++) {
				try {
					final int mI = i;
					mEnemies.get(mI).disposeSelf();
				} catch (Exception e) {
					Debug.d("SPK - ENEMY DOES NOT WANT TO DESTROY: " + e);
				}
			}
			// mEnemies.clear();
		}
	}

	private void addEnemy(final int pType) {
		Enemy pEnemy = Enemy.mDisposedEnemies.poll();
		if (pEnemy == null) {
			pEnemy = new Enemy(pType) {

				@Override
				public void resetHandler() {
					this.initPosition();
					// this.onChangeHandler(Enemy.TYPE_NAUTILUS);//
					// .TYPE_SQUID);

//					this.onChangeHandler(TYPE_STARFISH);
					this.onChangeHandler(MathUtils.random(mLevel));
					// > TYPE_PUFFER ? (TYPE_PUFFER + 1) : mLevel));
					// this.onChangeHandler(MathUtils.random(mLevel));

				}

				// @Override
				// public void executeDestroyAnimation() {
				// World.this.addParticles(pEnemy.getX(), pEnemy.getY());
				//
				// }

			};
			// pEnemy.registerParticleSpawn(this);
			mEnemyStorge.attachChild(pEnemy);
			mPlayerCollidables.add(pEnemy);
			mEnemies.add(pEnemy);
			// mPlayer.addEnemy(pEnemy);
		} else {
			pEnemy.setVisible(true);
			pEnemy.setIgnoreUpdate(false);
			pEnemy.initPosition();
			pEnemy.onChangeHandler(pType);
		}

	}

	private void addEnemies(final int pSize, final int pType) {
		for (int i = 0; i < pSize; i++) {
			addEnemy(pType);
		}
	}

	private void addEnemies(final int pSize) {
		for (int i = 0; i < pSize; i++) {
			// if (org.andengine.util.math.MathUtils.RANDOM.nextBoolean()) {
			// addEnemy(Enemy.TYPE_SQUID);
			// } else {
			// addEnemy(Enemy.TYPE_STARFISH);
			// }
			addEnemy(Enemy.TYPE_JELLYFISH);
		}
	}

	// Score------------------------------------------------------------------
	// private Entity mScoreDisplay;

}
