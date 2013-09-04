package com.zeroentropy.game.sprite;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.particle.BatchedPseudoSpriteParticleSystem;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.emitter.CircleParticleEmitter;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.IParticleInitializer;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseBackOut;

import android.opengl.GLES20;
import android.util.Log;

import com.zeroentropy.game.ResourcesManager;
import com.zeroentropy.game.handler.ILocatable;
import com.zeroentropy.game.handler.enemy.BindStarfishHandler;
import com.zeroentropy.game.handler.world.IScoreboard;
import com.zeroentropy.game.sprite.visual.FlashVisual;
import com.zeroentropy.game.sprite.visual.HealthVisual;
import com.zeroentropy.game.sprite.visual.LightVisual;
import com.zeroentropy.game.sprite.visual.StungVisual;
import com.zeroentropy.game.util.ZeroConstants;

public class Player extends Entity implements ILocatable {// AnimatedSprite {

	private PhysicsHandler mPhysicsHandler;
	private static final float BASE_GRAVITY = 1024;// 768;

	public static final int STATE_DOWN = 4;// 0x001; DOWN 向上转为向下的中间状态
	public static final int STATE_UP = 3;// 0x010; UP 向下转为向上的中间状态
	public static final int STATE_FALL = 1;// 0x011; FALL 落下/向上速度为零后，落下
	public static final int STATE_JUMP = 2;// 0x100; TURN 跳起/向下触碰后，跳起
	// public static final int STATE_STOP = 5;
	// public static final int STATE_RESTART = 6;

	private int mState = -1;

	// 身体部分
	private AnimatedSprite mBody;

	// 头部
	public AnimatedSprite mHand;

	// 攀爬海星贴图中心点与Player.Body贴图左上角Y方向（垂直）距离
	// { 88, 80, 58, 68, 78, 78, 78, 78 }
	// public static final int[] HEAD_CENTER_Y = { 8, 0, -22, -12, -2, -2, -2,
	// -2, };// cy
	// =
	// 80
	// 头部半径
	public static final int HEAD_RADIUS = 50;// 30;

	private final float mHandX = -50;// 24;
	private final float mHandY = -50;// 30;

	// 与Enemy碰撞后积分累计计算
	private IScoreboard mScoreborad;
	
	private float mExtraScoreX, mExtraScoreY;
	private int mExtraCounts = 0;

	// private int mScore = 0;

	public void setScoreboard(final IScoreboard pScoreborad) {
		mScoreborad = pScoreborad;
	}

	private void addScore() {
		
		if (mExtraCounts == 0) {
			mExtraScoreX = mX;
			mExtraScoreY = mY;
		} else {
			mExtraScoreX = (mExtraScoreX + mX) * 0.5f;
			mExtraScoreY = (mExtraScoreY + mY) * 0.5f;
		}
		mScoreborad.addScore();
		mExtraCounts++;
	}

	// 辅助视觉效果部分
	private LightVisual mLightVisual;
	private StungVisual mStungVisual;
	private HealthVisual mHealthVisual;
	private FlashVisual mFlashVisual;

	public Player() {
		// super(0, 0, ResourcesManager.getInstance().mBodyRegion,
		// ResourcesManager.getInstance().vbom);
		// attachParticleSystem();

		

		mStungVisual = new StungVisual();
		this.attachChild(mStungVisual);

		mLightVisual = new LightVisual();
		this.attachChild(mLightVisual);

		mBody = new AnimatedSprite(-ZeroConstants.WUKONG_WIDTH * 0.5f, -75f,// 72.5f
				ResourcesManager.getInstance().mBodyRegion,
				ResourcesManager.getInstance().vbom);

		this.attachChild(mBody);

		mFlashVisual = new FlashVisual();
		this.attachChild(mFlashVisual);
		
		mHand = new AnimatedSprite(mHandX, mHandY,
				ResourcesManager.getInstance().mHandRegion,
				ResourcesManager.getInstance().vbom);

		this.attachChild(mHand);

		mHealthVisual = new HealthVisual(0, -60);
		this.attachChild(mHealthVisual);
		
		BindStarfishHandler.getInstance().setLocation(this);

	}

	// 以下为查验Bonus释放条件是否满足。
	public boolean checkPeach() {
		final boolean pFulfilled = (mHealthVisual.getHealth() < 100 && mPeach > 0);
		if (pFulfilled) {
			mPeach--;
		}
		return pFulfilled;
	}

	public boolean checkVision() {
		final boolean pFulfilled = (getVisionWidth() == 0 && mFiery_eyes > 0);
		if (pFulfilled) {
			mFiery_eyes--;
		}
		return pFulfilled;
	}

	public boolean checkMantra() {
		final boolean pFulfilled = (mJumpFactor < 1.0f && mMantra > 0);
		if (pFulfilled) {
			mMantra--;
		}
		return pFulfilled;
//		return true;
	}

	//
	private MoveYModifier mInitMoveY;
	private IEntityModifierListener mInitMoveListener;
	private TimerHandler mRepeatHandler;

	public void doCreate() {

		// registerSupplyHandler();

		// setVisionWidth(0.0f);

		if (mPhysicsHandler == null) {
			mPhysicsHandler = new PhysicsHandler(this);
			this.registerUpdateHandler(mPhysicsHandler);
		}

		// 重复摆手动作。。。
		if (mRepeatHandler == null) {
			mRepeatHandler = new TimerHandler(1f, true, new ITimerCallback() {
				public void onTimePassed(final TimerHandler pTimerHandler) {
					if (mState == STATE_DOWN || mState == -1)
						Player.this.animateDown();
				}
			});
			this.registerUpdateHandler(mRepeatHandler);
		} else {
			mRepeatHandler.reset();
		}

	}

	public void doInit() {

//		initScore();

//		if (!mLightVisual.isFinished()) {
			mLightVisual.onFinish();
//		}
		headDown();

		final float pInitX = ZeroConstants.SCENE_WIDTH * .5f;// (ZeroConstants.SCENE_WIDTH
																// -
																// ZeroConstants.WUKONG_WIDTH)
																// * .5f;
		final float pInitY = -2.0f * ZeroConstants.WUKONG_HEIGHT;
		setPosition(pInitX, pInitY);

		if (mInitMoveY == null) {
			mInitMoveY = new MoveYModifier(1.5f, pInitY, 150,
					EaseBackOut.getInstance());

			this.registerEntityModifier(new SequenceEntityModifier(
					new DelayModifier(1.5f), mInitMoveY));
		} else {

			if (mInitMoveListener == null) {
				mInitMoveListener = new IEntityModifierListener() {

					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier,
							IEntity pItem) {

						// setX(pInitX);

					}

					@Override
					public void onModifierFinished(
							IModifier<IEntity> pModifier, IEntity pItem) {

						doStart();

					}

				};
				mInitMoveY.addModifierListener(mInitMoveListener);
			}
			mInitMoveY.reset();
			this.registerEntityModifier(mInitMoveY);
		}

		this.setIgnoreUpdate(false);
		this.setVisible(true);

	}

	public void doStart() {

		// detachAllBindStarfish();
		mHealthVisual.onInit();
		mLightVisual.onInit();
		mFlashVisual.onInit();
		
//		mHealth = 100;
		mPeach = 0;// 水母：桃子－生命值－蜇刺－刺豚

		mJumpFactor = 1.0f;
		mMantra = 0;//

		mFiery_eyes = 0;// 奔跑的章鱼：火眼－墨汁－遮挡－章鱼
		// mVisionWidth = 0;

		if (mPhysicsHandler != null)
			mPhysicsHandler.setEnabled(true);

		if (mPhysicsHandler.getAccelerationY() != BASE_GRAVITY)
			mPhysicsHandler.setAccelerationY(BASE_GRAVITY);

		if (mPhysicsHandler.getVelocityY() != 0)
			mPhysicsHandler.setVelocityY(0);
	}

	public void doStop() {
		// mState = STATE_STOP;

//		if (!mLightVisual.isFinished()) {
			mLightVisual.onFinish();
//		}

		mStungVisual.onHide();

		if (mPhysicsHandler != null)
			mPhysicsHandler.setEnabled(false);

		this.setVisible(false);

	}

	//
	// public void doRestart() {
	// // doCreate();
	//
	// }

	public void setGravity(final float pGravity) {
		// Log.e("AccelerationChanged",
		// "----------||"+pGravity+"||-----------");
		if (mPhysicsHandler != null) {
			// mPhysicsHandler.setAccelerationX(pGravity*10);
			mPhysicsHandler.setVelocityX(pGravity * 128);// 64);
		}
	}

	// HEAD KEEP DOWN
	private void headDown() {

		mState = STATE_DOWN;

	}

	// TURN HEAD DOWN TO UP
	public void jump() {
		mState = STATE_JUMP;
		final float pDistance;
		if (mY > ZeroConstants.WUKONG_HEIGHT)
			pDistance = (mY - ZeroConstants.WUKONG_HEIGHT * .5f) * mJumpFactor;

		else
			pDistance = ZeroConstants.WUKONG_HEIGHT * mJumpFactor;
		// final float pVY = (float) -Math.sqrt(2.0 * BASE_GRAVITY * pDistance);
		mPhysicsHandler.setVelocityY((float) -Math.sqrt(2.0 * BASE_GRAVITY
				* pDistance));
		animateJump();
//		doPartialSum();
		addScore();
	}

	// HEAD KEEP UP
	private void headUp() {
		mState = STATE_UP;
	}

	// TURN HEAD UP TO DOWN
	private void fall() {
		mState = STATE_FALL;
		animateFall();
//		doEntireSum();
		if(mExtraCounts>2)
			mScoreborad.addScore(mExtraCounts, mExtraScoreX, mExtraScoreY);
		mExtraCounts = 0;
	}

	// 向上转为向下的过程（头上脚下渐变为头下脚上）
	// 此过程内，状态为FALL。。。
	// 单次播放不循环。。。
	// 播放完成后，停留在"头下脚上"姿势。。。状态改为DOWN。。。
	private void animateFall() {
		// this
		mBody.animate(new long[] { 24, 24, 24, 24, 24 }, 0, 4, 0,
				new IAnimationListener() {

					@Override
					public void onAnimationStarted(
							AnimatedSprite pAnimatedSprite,
							int pInitialLoopCount) {

					}

					@Override
					public void onAnimationFrameChanged(
							AnimatedSprite pAnimatedSprite, int pOldFrameIndex,
							int pNewFrameIndex) {

					}

					@Override
					public void onAnimationLoopFinished(
							AnimatedSprite pAnimatedSprite,
							int pRemainingLoopCount, int pInitialLoopCount) {

					}

					@Override
					public void onAnimationFinished(
							AnimatedSprite pAnimatedSprite) {
						Player.this.headDown();// setState(STATE_FALL);

					}
				});
	}

	// 跳起，头向下转为向上的过程。。。
	// 此过程内，状态为FALL。。。
	// 单次播放不循环。。。
	// 播放完成后，停留在"头上脚下"姿势。。。状态改为UP。。。
	private void animateJump() {
		// this.
		mBody.animate(new long[] { 24, 24, 24, 24, 24 }, new int[] { 4, 3, 2,
				1, 0 }, 0, new IAnimationListener() {

			@Override
			public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
					int pInitialLoopCount) {

			}

			@Override
			public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite,
					int pOldFrameIndex, int pNewFrameIndex) {

			}

			@Override
			public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,
					int pRemainingLoopCount, int pInitialLoopCount) {

			}

			@Override
			public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
				Player.this.headUp();

			}
		});
	}

	private void animateDown() {
		// this.
		mBody.animate(
				new long[] { 48, 48, 48, 24, 48, 48, 48, 48, 24, 48, 48 },
				new int[] { 4, 5, 6, 7, 6, 5, 4, 5, 6, 5, 4 }, 0);
	}

	private void onCheckBounds() {
		if (mX > ZeroConstants.SCENE_WIDTH) {// - ZeroConstants.WUKONG_WIDTH) {
			mX = ZeroConstants.SCENE_WIDTH;// - ZeroConstants.WUKONG_WIDTH;
			// mDX = 0;
		}
		if (mX < 0) {
			mX = 0;
			// mDX = 0;
		}
		if (mY > ZeroConstants.SCENE_HEIGHT + ZeroConstants.WUKONG_HEIGHT) {
			mY = ZeroConstants.SCENE_HEIGHT + ZeroConstants.WUKONG_HEIGHT;
		}
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {

		super.onManagedUpdate(pSecondsElapsed);

		onCheckBounds();

		if (mState == STATE_UP && mPhysicsHandler.getVelocityY() >= 0)
			this.fall();// setState(STATE_DOWN);

	}

	// ---------------------------------------------
	// PEACH & HEALTH
	// ---------------------------------------------

//	public int mHealth = 100;
	public int mPeach = 0;// 水母：桃子－生命值－蜇刺－刺豚

	public void getStung(final int pValue) {
		jump();// setState(STATE_JUMP);
//		mHealthVisual.onDisplay();
		// spawnParticles();
		mHealthVisual.addHealth(-pValue);
//		mHealth -= pValue;
	}

	public void getPeach() {
		jump();// setState(STATE_JUMP);
		mPeach += 5;
	}

	public void getCure(final int pCure) {
//		mHealth += pCure;
		mHealthVisual.addHealth(pCure);
	}

	// ---------------------------------------------
	// MANTRA & FLASH
	// ---------------------------------------------

	protected float mJumpFactor = 1.0f;
	public int mMantra = 0;// 鮟鱇鱼：咒语－摆脱－捆绑－海星

	public void getMantra() {
		jump();// setState(STATE_JUMP);
		mMantra++;
	}

	public void onBindStarfish() {
//		jump();
		mJumpFactor *= .8f;// <<= 1;
		if (mJumpFactor < 0.1f)
			mJumpFactor = 0.1f;
		Log.i("===>mJumpFactor<===", ": " + mJumpFactor);
	}

	private boolean isLocatable = true;

	public void onFlash() {

		// 闪光动画。。。

		// starfish逐个逃逸。。。
		// final int size = this.mHand.getChildCount();
		// for (int i = 0; i < size; i++) {
		// this.detachBindStarfish((Enemy) this.mHand.getChildByIndex(i));
		// }
		mFlashVisual.onShow();
		
		isLocatable = false;
		mJumpFactor = 1.0f;
		this.registerUpdateHandler(new TimerHandler(2.0f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				isLocatable = true;

			}

		}));
	}

	// ---------------------------------------------
	// FIERYEYES & VISION
	// ---------------------------------------------

	public int mFiery_eyes = 0;// 奔跑的章鱼：火眼－墨汁－遮挡－章鱼

	// protected float mVisionWidth = 0;

	public void getFieryEyes() {
		jump();// setState(STATE_JUMP);
		mFiery_eyes++;

	}

	public void getVision() {
		mLightVisual.onStart();
	}

	public float getVisionWidth() {
		return mLightVisual.getVisionWidth();
	}

	// ---------------------------------------------
	// SOMERSAULT & ???
	// ---------------------------------------------
	public int mSomersault = 0;// 鹦鹉螺：筋斗云－减重－坠落－海星

	public void getSomersault() {
		jump();// setState(STATE_JUMP);
		mSomersault++;

	}

	public boolean isOut() {
		return mY >= ZeroConstants.SCENE_HEIGHT + ZeroConstants.WUKONG_HEIGHT;
	}

	// -----------------------------------------------------------------------
	// 粒子系统测试
	// -----------------------------------------------------------------------

	public static IEntity mParticleSystemEntity = new Entity();
	private CircleParticleEmitter particleEmitter;
	private BatchedPseudoSpriteParticleSystem particleSystem;

	public void spawnParticles() {
		particleEmitter.setCenter(mX, mY);
		particleSystem.setParticlesSpawnEnabled(true);
		mParticleSystemEntity.registerUpdateHandler(new TimerHandler(.1f,
				false, new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						particleSystem.setParticlesSpawnEnabled(false);
						// particleSystem.reset();

					}

				}));
	}

	private void attachParticleSystem() {

		particleEmitter = new CircleParticleEmitter(0, 0,
				ZeroConstants.BASE_SPRITE_WIDTH * .5f,
				ZeroConstants.BASE_SPRITE_HEIGHT * .5f);
		// final SpriteParticleSystem particleSystem = new SpriteParticleSystem(
		// particleEmitter, 10, 10, 10, resourcesManager.particle_region,
		// vbom);
		// BatchedPseudoSpriteParticleSystem
		particleSystem = new BatchedPseudoSpriteParticleSystem(particleEmitter,
				40, 60, 60, ResourcesManager.getInstance().particle_region,
				ResourcesManager.getInstance().vbom);

		// particleSystem
		// .addParticleInitializer(new AccelerationParticleInitializer<Entity>(
		// 0, 0, 250, 500));//768
		particleSystem
				.addParticleInitializer(new ColorParticleInitializer<Entity>(
						.75f, .9f, .2f, .45f, .4f, .6f));
		// particleSystem
		// .addParticleInitializer(new AlphaParticleInitializer<Entity>(1));
		// particleSystem
		// .addParticleInitializer(new BlendFunctionParticleInitializer<Entity>(
		// GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
		// particleSystem
		// .addParticleInitializer(new VelocityParticleInitializer<Entity>(
		// -120, 120, -120, -120));
		// particleSystem
		// .addParticleInitializer(new RotationParticleInitializer<Entity>(
		// 0.0f, 360.0f));
		// particleSystem/*
		// .addParticleInitializer(new ScaleParticleInitializer<Entity>(//
		// .5f, .2f));*/
		particleSystem
				.addParticleInitializer(new ExpireParticleInitializer<Entity>(2));

		particleSystem
				.addParticleInitializer(new IParticleInitializer<Entity>() {

					@Override
					public void onInitializeParticle(
							final Particle<Entity> pParticle) {

						final float w2 = ResourcesManager.getInstance().particle_region
								.getWidth() * .5f;
						final float h2 = ResourcesManager.getInstance().particle_region
								.getHeight() * .5f;
						pParticle.getEntity().setRotationCenter(w2, h2);
						pParticle.getEntity().setScaleCenter(w2, h2);
						pParticle.getEntity().setScale(
								MathUtils.random(.3f, .6f));
						pParticle.getPhysicsHandler().setAngularVelocity(
								MathUtils.random(-720, 720));
						pParticle.getPhysicsHandler().setAccelerationY(
								MathUtils.random(-800, -600));
						pParticle.getPhysicsHandler().setVelocity(
								MathUtils.random(-180, 180),
								MathUtils.random(120, 240));
					}

				});

		// particleSystem
		// .addParticleModifier(new RotationParticleModifier<Entity>(0, 6,
		// 0, 360));
		// IParticleInitializer
		// particleSystem.addParticleModifier(new
		// ScaleParticleModifier<Entity>(0,
		// 5, 1.0f, .5f, 1.0f, 2.0f));
		// particleSystem.addParticleModifier(new
		// ColorParticleModifier<Entity>(0,
		// 3, 1, 1, 1, 0.5f, 1, 0));
		// particleSystem.addParticleModifier(new
		// ColorParticleModifier<Entity>(4,
		// 6, 1, 1, 0.5f, 1, 0, 1));
		// particleSystem.addParticleModifier(new
		// AlphaParticleModifier<Entity>(0,
		// 2, 1, 0));
		// particleSystem.addParticleModifier(new
		// AlphaParticleModifier<Entity>(4,
		// 6, 1, 0));
		particleSystem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);

		particleSystem.setParticlesSpawnEnabled(false);

		mParticleSystemEntity.attachChild(particleSystem);

	}

	@Override
	public float getLocationX() {
		return getX();
	}

	@Override
	public float getLocationY() {
		return getY();
	}

	@Override
	public boolean getLocatable() {
		return isLocatable;
	}

	// private ISpawnable mParticleSpawn;
	// public void registerParticleSpawn(final ISpawnable pParticleSpawn){
	// mParticleSpawn = pParticleSpawn;
	// }
}
