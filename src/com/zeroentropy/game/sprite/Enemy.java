package com.zeroentropy.game.sprite;

import java.util.LinkedList;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.IUpdateHandler.IUpdateHandlerMatcher;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.particle.BatchedPseudoSpriteParticleSystem;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.emitter.CircleParticleEmitter;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.IParticleInitializer;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.util.math.MathUtils;

import android.opengl.GLES20;

import com.zeroentropy.game.ResourcesManager;
import com.zeroentropy.game.handler.IPlayerCollidable;
import com.zeroentropy.game.handler.enemy.AnglerHandler;
import com.zeroentropy.game.handler.enemy.AngryPufferHandler;
import com.zeroentropy.game.handler.enemy.BindStarfishHandler;
import com.zeroentropy.game.handler.enemy.EscapeStarfishHandler;
import com.zeroentropy.game.handler.enemy.IEnemyHandler;
import com.zeroentropy.game.handler.enemy.JellyfishHandler;
import com.zeroentropy.game.handler.enemy.NautilusHandler;
import com.zeroentropy.game.handler.enemy.PufferHandler;
import com.zeroentropy.game.handler.enemy.RunnySquidHandler;
import com.zeroentropy.game.handler.enemy.SquidHandler;
import com.zeroentropy.game.handler.enemy.StarfishHandler;
import com.zeroentropy.game.util.ZeroConstants;

public abstract class Enemy extends AnimatedSprite implements IPlayerCollidable {

	public static final int TYPE_JELLYFISH = 0; // 水母
	public static final int TYPE_ANGLER = 1; // 鮟鱇鱼
	public static final int TYPE_SQUID = 2; // 乌贼
	public static final int TYPE_NAUTILUS = 3; // 鹦鹉螺
	public static final int TYPE_STARFISH = 4; // 海星
	public static final int TYPE_PUFFER = 5; // 刺鲀
	// 派生类-Model
	public static final int TYPE_RUNNY_SQUID = 6; // 奔跑的乌贼
	public static final int TYPE_ANGRY_PUFFER = 7;// 生气地刺豚
	// 静止类-StaticModel -
	// public static final int TYPE_INK = 8; // 墨水
	public static final int TYPE_BROKEN = 8; // 普通动画效果
	public static final int TYPE_ESCAPE_STARFISH = 9; // 逃逸的海星
	// 恒动类-MotionModel-NativeModel
	// public static final int TYPE_BUBBLE = 10; // 气泡
	// public static final int TYPE_CLOUD = 11; // 云层
	// 绑定类-BindModel
	public static final int TYPE_BIND_STARFISH = 10;// 攀附的海星
	// 救生气囊
	// 头灯
	// 效果---刺中、雷电......

	// public static final int TYPE_CLOUD_0 = 20; // 云层_0
	// public static final int TYPE_CLOUD_1 = 21; // 云层_1
	// public static final int TYPE_CLOUD_2 = 22; // 云层_2
	public static LinkedList<Enemy> mDisposedEnemies = new LinkedList<Enemy>();

	protected IEnemyHandler mCurrentHandler;
	// public World mWorld;

	private int mType = -1;

	public float mElapsedTime;

	public int getType() {
		return mType;
	}

	public Enemy(final int pType) {
		super(0, 0, ResourcesManager.getInstance().mSpritesRegion,
				ResourcesManager.getInstance().vbom);

		mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(mPhysicsHandler);

		mMatcher = new IUpdateHandlerMatcher() {

			@Override
			public boolean matches(IUpdateHandler pObject) {
				// TODO Auto-generated method stub
				return mPhysicsHandler != pObject;
			}

		};
		this.initPosition();
		this.onChangeHandler(pType);
		attachParticleSystem();
		// this.mWorld = pWorld;
	}

	// ----------------------------------------------------------
	private PhysicsHandler mPhysicsHandler;
	private IUpdateHandlerMatcher mMatcher;

	public void unregisterUpdateHandlers() {
		this.unregisterUpdateHandlers(mMatcher);
	}

	public PhysicsHandler getPhysics() {
		return mPhysicsHandler;
	}

	public boolean isEnabled() {
		return mPhysicsHandler.isEnabled();
	}

	public void setEnabled(final boolean pEnabled) {
		mPhysicsHandler.setEnabled(pEnabled);
	}

	public float getVelocityX() {
		return mPhysicsHandler.getVelocityX();
	}

	public float getVelocityY() {
		return mPhysicsHandler.getVelocityY();
	}

	public void setVelocityX(final float pVelocityX) {
		mPhysicsHandler.setVelocityX(pVelocityX);
	}

	public void setVelocityY(final float pVelocityY) {
		mPhysicsHandler.setVelocityY(pVelocityY);
	}

	public void setVelocity(final float pVelocity) {
		mPhysicsHandler.setVelocity(pVelocity);// pVelocity;
		// this.mVelocityY = pVelocity;
	}

	public void setVelocity(final float pVelocityX, final float pVelocityY) {
		mPhysicsHandler.setVelocity(pVelocityX, pVelocityY);
	}

	public float getAccelerationX() {
		return mPhysicsHandler.getAccelerationX();// .mAccelerationX;
	}

	public float getAccelerationY() {
		return mPhysicsHandler.getAccelerationY();// .mAccelerationY;
	}

	public void setAccelerationX(final float pAccelerationX) {
		mPhysicsHandler.setAccelerationX(pAccelerationX);// pAccelerationX;
	}

	public void setAccelerationY(final float pAccelerationY) {
		mPhysicsHandler.setAccelerationY(pAccelerationY);
	}

	public void setAcceleration(final float pAccelerationX,
			final float pAccelerationY) {
		mPhysicsHandler.setAcceleration(pAccelerationX, pAccelerationY);
	}

	public void setAcceleration(final float pAcceleration) {
		mPhysicsHandler.setAcceleration(pAcceleration);
	}

	public void accelerate(final float pAccelerationX,
			final float pAccelerationY) {
		mPhysicsHandler.accelerate(pAccelerationX, pAccelerationY);
	}

	public float getAngularVelocity() {
		return mPhysicsHandler.getAngularVelocity();
	}

	public void setAngularVelocity(final float pAngularVelocity) {
		mPhysicsHandler.setAngularVelocity(pAngularVelocity);
	}

	public void resetPhysics() {
		mPhysicsHandler.reset();
	}

	// -----------------------------------------------------------------
	public void turnRound() {
		final float v_x = mPhysicsHandler.getVelocityX();
		mPhysicsHandler.setVelocityX(-v_x);

		if (mPhysicsHandler.getVelocityX() > 0)
			setFlippedHorizontal(false);
		else
			setFlippedHorizontal(true);
	}

	public void turnRight() {

		if (mPhysicsHandler.getVelocityX() < 0)
			turnRound();
	}

	public void turnLeft() {

		if (mPhysicsHandler.getVelocityX() > 0)
			turnRound();

	}

	private int mDir = 0;

	public int getDir() {
		return mDir;
	}

	@Override
	public void setFlippedHorizontal(final boolean bool) {
		if (bool)
			mDir = -1;
		else
			mDir = 1;
		super.setFlippedHorizontal(bool);
	}

	// -------------------------------------------------------------------------------
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		if (mCurrentHandler != null) {
			mCurrentHandler.execute(pSecondsElapsed, this);
			mElapsedTime += pSecondsElapsed;

		}

		if (getY() < -2.0f * ZeroConstants.WUKONG_HEIGHT) {

			this.resetHandler();
		}

	}

	public void animate(final long[] pFrameDurations, final int pFirstIndex,
			final int pLastIndex) {
		// final int index = pFrames[0];
		final int length = pFrameDurations.length;
		final int[] frames = new int[length];
		final int index = MathUtils.random(pFirstIndex, pLastIndex);
		for (int i = 0; i < length; i++) {
			frames[i] = pFirstIndex + (index + i) % length;
		}

		super.animate(pFrameDurations, frames, true);
	};

	public void initPosition() {
		// final float w_2 = ZeroConstants.BASE_SPRITE_WIDTH * 0.5f;// / z;
		// final float h = mCloudHeight / z;
		final float x = MathUtils.random(ZeroConstants.SCENE_WIDTH
				- ZeroConstants.BASE_SPRITE_WIDTH);
		final float y = ZeroConstants.SCENE_HEIGHT
				+ MathUtils.random(ZeroConstants.SCENE_HEIGHT * 0.5f);
		// pSprite.setFlippedHorizontal(MathUtils.randomBoolean());
		setPosition(x, y);
		mPhysicsHandler.reset();
	}

	protected void resetSelf() {
		if (mType != -1) {
			this.initPosition();
			this.onChangeHandler(mType);
		}
	}

	public abstract void resetHandler();

	public void disposeSelf() {
		mCurrentHandler.exit(this);

		Enemy.this.spawnParticles();

		Enemy.this.setVisible(false);
		Enemy.this.setIgnoreUpdate(true);
		initPosition();
		mDisposedEnemies.add(Enemy.this);
		mType = -1;
	}

	public void brokenSelf() {
		mCurrentHandler.exit(this);

		Enemy.this.spawnParticles();

		Enemy.this.resetHandler();
	}

	@Override
	public boolean onCollision(final Player pPlayer) {
		if (pPlayer == null)
			return false;
		final boolean isCollided = mElapsedTime > 1.0f
				&& this.collidesWith(pPlayer.mHand);
		if (isCollided) {
			mCurrentHandler.executeCollides(this, pPlayer);
//			pPlayer.doPartialSum();
		}
		return isCollided;
	}

	public synchronized void onChangeHandler(final int pType) {
		final IEnemyHandler pHandler = getHandler(pType);
		if (pHandler == null) {
			return;
		} else {

			if (this.mCurrentHandler != null)
				mCurrentHandler.exit(this);
			this.mCurrentHandler = pHandler;
			mCurrentHandler.enter(this);
		}
		mType = pType;
		mElapsedTime = 0;
	}

	private IEnemyHandler getHandler(int type) {
		switch (type) {

		case TYPE_JELLYFISH:// 0
			return JellyfishHandler.getInstance();
		case TYPE_ANGLER:// 1
			return AnglerHandler.getInstance();
		case TYPE_SQUID:// 2
			return SquidHandler.getInstance();
		case TYPE_NAUTILUS:// 3
			return NautilusHandler.getInstance();
		case TYPE_STARFISH:// 4
			return StarfishHandler.getInstance();
		case TYPE_PUFFER:// 5
			return PufferHandler.getInstance();
		case TYPE_RUNNY_SQUID:// 6
			return RunnySquidHandler.getInstance();
		case TYPE_ANGRY_PUFFER:// 7
			return AngryPufferHandler.getInstance();
		case TYPE_BIND_STARFISH:// 8
			return BindStarfishHandler.getInstance();
		case TYPE_ESCAPE_STARFISH:// 9
			return EscapeStarfishHandler.getInstance();
		default:
			break;
		}
		return null;
	}

	// -----------------------------------------------------------------------
	// 粒子系统测试
	// -----------------------------------------------------------------------

	public static IEntity mParticleSystemEntity = new Entity();
	private CircleParticleEmitter particleEmitter;
	private BatchedPseudoSpriteParticleSystem particleSystem;

	public void spawnParticles() {
		particleEmitter.setCenter(mX + mWidth * .5f, mY + mHeight * .5f);
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
								MathUtils.random(600, 800));
						pParticle.getPhysicsHandler().setVelocity(
								MathUtils.random(-180, 180),
								MathUtils.random(-240, -120));
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

	// private ISpawnable mParticleSpawn;
	// public void registerParticleSpawn(final ISpawnable pParticleSpawn){
	// mParticleSpawn = pParticleSpawn;
	// }
}
