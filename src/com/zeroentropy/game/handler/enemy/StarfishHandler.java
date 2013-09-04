package com.zeroentropy.game.handler.enemy;

import java.util.Arrays;

import org.andengine.entity.modifier.RotationModifier;

import com.zeroentropy.game.sprite.Enemy;
import com.zeroentropy.game.sprite.Player;
import org.andengine.util.math.MathUtils;
import com.zeroentropy.game.util.ZeroConstants;

public class StarfishHandler implements IEnemyHandler {

	public static final float ROTATION_CENTER_X = ZeroConstants.BASE_SPRITE_WIDTH * .5f;
	public static final float ROTATION_CENTER_Y = ZeroConstants.BASE_SPRITE_HEIGHT - 16;

	private static final int mFirstTileIndex = 23;
	private static final int mLastTileIndex = 31;
	private static final int mFrameCount = mLastTileIndex - mFirstTileIndex + 1;

	private static long[] mFrameDurations = new long[mFrameCount];

	public static StarfishHandler getInstance() {
		return INSTANCE;
	}

	private static StarfishHandler INSTANCE = new StarfishHandler();

	private StarfishHandler() {
		Arrays.fill(mFrameDurations, FRAME_DURATION_EACH);

	}

	private static final float BASIC_DURATION = 1.0f;
	private static final float BASIC_ROTATION = 90.0f;

	private static final int VELOCITY_Y = -160;//-128;

	public void enter(final Enemy pSprite) {

		if (pSprite.getRotation() != 0)
			pSprite.setRotation(0);
		pSprite.setFlippedHorizontal(MathUtils.randomBoolean());
		pSprite.resetPhysics();

		pSprite.setRotationCenter(ROTATION_CENTER_X, ROTATION_CENTER_Y);

		pSprite.animate(mFrameDurations, mFirstTileIndex, mLastTileIndex);

		pSprite.setVelocityY(MathUtils.randomFactor(VELOCITY_Y, 0.5f));
	}

	public void execute(final float pSecondsElapsed, final Enemy pSprite) {

	}

	public void exit(final Enemy pSprite) {
		// pSprite.setFlippedHorizontal(false);
		// pSprite.clearUpdateHandlers();
	}

	@Override
	public void executeCollides(final Enemy pSprite, final Player pPlayer) {
		// TODO Auto-generated method stub
		// pSprite.resetHandler();
		pSprite.onChangeHandler(Enemy.TYPE_BIND_STARFISH);
		pPlayer.onBindStarfish();
	}
}
