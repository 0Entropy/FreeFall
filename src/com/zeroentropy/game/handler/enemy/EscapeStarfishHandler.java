package com.zeroentropy.game.handler.enemy;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.IModifier;

import com.zeroentropy.game.sprite.Enemy;
import com.zeroentropy.game.sprite.Player;
import com.zeroentropy.game.util.ZeroConstants;

public class EscapeStarfishHandler implements IEnemyHandler {

	public static EscapeStarfishHandler getInstance() {
		return INSTANCE;
	}

	private static EscapeStarfishHandler INSTANCE = new EscapeStarfishHandler();

	private EscapeStarfishHandler() {
	}

	private static float VELOCITY_X = 50;
	private static float VELOCITY_Y = -50;
	private static float ANGULAR_V = 60;
	private static float ACCELERATION_Y = 120;
	private static float DURATION = 1;

//	private static final float BASIC_DURATION = 1.0f;
//	private static final float BASIC_ROTATION = 90.0f;

	// Acceleration
	@Override
	public void enter(final Enemy pSprite) {

		// if (pSprite.getRotation() != 0)
		// pSprite.setRotation(0);
		// pSprite.setFlippedHorizontal(MathUtils.randomBoolean());
		pSprite.resetPhysics();

		final float v_x, v_y, a_y, a_v, duration;
		v_x = MathUtils.randomFactor(VELOCITY_X, 1.0f);
		v_y = MathUtils.randomFactor(VELOCITY_Y, 1.0f);
		a_y = MathUtils.randomFactor(ACCELERATION_Y, 1.0f);
		a_v = MathUtils.randomFactor(ANGULAR_V, 1.0f);
		duration = MathUtils.randomFactor(DURATION, 1.0f);
		
		pSprite.setVelocity(pSprite.getDir() * v_x, v_y);
		pSprite.setAngularVelocity(pSprite.getDir() * a_v);
		pSprite.setAccelerationY(a_y);

		pSprite.registerUpdateHandler(new TimerHandler(duration,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						 pSprite.onChangeHandler(Enemy.TYPE_STARFISH);

					}

				}));
	}

	@Override
	public void execute(final float pSecondsElapsed, final Enemy pSprite) {
		if (pSprite.getX() > ZeroConstants.SCENE_WIDTH
				- ZeroConstants.BASE_SPRITE_WIDTH) {
			pSprite.setX(ZeroConstants.SCENE_WIDTH
					- ZeroConstants.BASE_SPRITE_WIDTH);
			// mDX = 0;
		}
		if (pSprite.getX() < 0) {
			pSprite.setX(0);
			// mDX = 0;
		}
		if (pSprite.getY() > ZeroConstants.SCENE_HEIGHT
				+ ZeroConstants.WUKONG_HEIGHT) {
			// mY = ZeroConstants.SCENE_HEIGHT + ZeroConstants.WUKONG_HEIGHT;
			pSprite.resetHandler();
		}

	}

	@Override
	public void exit(final Enemy pSprite) {
		pSprite.unregisterUpdateHandlers();
		pSprite.clearEntityModifiers();

	}

	@Override
	public void executeCollides(final Enemy pItem, final Player pTarget) {
		// TODO Auto-generated method stub

	}

}
