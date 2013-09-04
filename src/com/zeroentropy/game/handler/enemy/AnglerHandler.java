package com.zeroentropy.game.handler.enemy;

import java.util.Arrays;

import org.andengine.engine.handler.UpdateHandlerList;
import org.andengine.util.math.MathUtils;

import com.zeroentropy.game.sprite.Enemy;
import com.zeroentropy.game.sprite.Player;
import com.zeroentropy.game.util.ZeroConstants;

public class AnglerHandler implements IEnemyHandler {
	private static final int mFirstTileIndex = 12;
	private static final int mLastTileIndex = 20;
	private static final int mFrameCount = mLastTileIndex - mFirstTileIndex + 1;

	private static long[] mFrameDurations = new long[mFrameCount];

	public static AnglerHandler getInstance() {
		return INSTANCE;
	}

	private static AnglerHandler INSTANCE = new AnglerHandler();

	private AnglerHandler() {
		Arrays.fill(mFrameDurations, FRAME_DURATION_EACH);
	}

//	private static final int MIN_VELOCITY_Y = -120;//-64;
	private static final int VELOCITY_Y = -80;//-96;
//	private static final int MIN_VELOCITY_X = 80;//64;
	private static final int VELOCITY_X = 80;//96;

	@Override
	public void enter(final Enemy pSprite) {

		if (pSprite.getRotation() != 0)
			pSprite.setRotation(0);
		pSprite.setFlippedHorizontal(MathUtils.randomBoolean());
		pSprite.resetPhysics();

		pSprite.animate(mFrameDurations, mFirstTileIndex, mLastTileIndex);

		pSprite.setVelocity(
				pSprite.getDir()
						* MathUtils.randomFactor(VELOCITY_X, 0.5f),
				MathUtils.randomFactor(VELOCITY_Y, 0.5f));
	}

	@Override
	public void execute(final float pSecondsElapsed, final Enemy pSprite) {
		if (pSprite.getX() < 0) {
			pSprite.turnRight();
		} else if (pSprite.getX() > ZeroConstants.SCENE_WIDTH
				- ZeroConstants.BASE_SPRITE_WIDTH) {
			pSprite.turnLeft();
		}
	}

	@Override
	public void exit(final Enemy pSprite) {
		// pSprite.clearUpdateHandlers();

	}

	@Override
	public void executeCollides(final Enemy pSprite, final Player pPlayer) {
		// TODO Auto-generated method stub
		pSprite.brokenSelf();
		// World.getInstance().mPlayer.setState(IPlayer.STATE_JUMP);
		// ‘ˆº”÷‰”Ô£≠∞⁄Õ—÷µ
		// mantra++;
		pPlayer.getMantra();
	}

}
