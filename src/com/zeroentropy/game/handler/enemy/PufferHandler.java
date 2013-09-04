package com.zeroentropy.game.handler.enemy;

import java.util.Arrays;

import org.andengine.util.math.MathUtils;
import com.zeroentropy.game.util.ZeroConstants;
import com.zeroentropy.game.sprite.Enemy;
import com.zeroentropy.game.sprite.Player;

public class PufferHandler implements IEnemyHandler {
	private static final int mFirstTileIndex = 32;
	private static final int mLastTileIndex = 35;
	private static final int mFrameCount = mLastTileIndex - mFirstTileIndex + 1;

	private static long[] mFrameDurations = new long[mFrameCount];

	public static PufferHandler getInstance() {
		return INSTANCE;
	}

	private static PufferHandler INSTANCE = new PufferHandler();

	private PufferHandler() {
		Arrays.fill(mFrameDurations, FRAME_DURATION_EACH);
	}

	private static final int VELOCITY_Y = -50;
	private static final int VELOCITY_X = 50;

	public void enter(final Enemy pSprite) {
		if (pSprite.getRotation() != 0)
			pSprite.setRotation(0);
		pSprite.setFlippedHorizontal(MathUtils.randomBoolean());
		pSprite.resetPhysics();
		
		pSprite.animate(mFrameDurations, mFirstTileIndex, mLastTileIndex);

		pSprite.setVelocity(pSprite.getDir()
				* MathUtils.randomFactor(VELOCITY_X, 0.5f),
				MathUtils.randomFactor(VELOCITY_Y, 0.5f));
	}

	public void execute(final float pSecondsElapsed, final Enemy pSprite) {

		if (pSprite.getX() < 0 ) {
			pSprite.turnRight();
		}else if (pSprite.getX() > ZeroConstants.SCENE_WIDTH - ZeroConstants.BASE_SPRITE_WIDTH) {
			pSprite.turnLeft();
		}

	}

	public void exit(final Enemy pSprite) {
		// pSprite.setFlippedHorizontal(false);
//		pSprite.clearUpdateHandlers();
	}

	@Override
	public void executeCollides(final Enemy pSprite, final Player pPlayer) {
		// TODO Auto-generated method stub
		pSprite.onChangeHandler(Enemy.TYPE_ANGRY_PUFFER);
		// World.getInstance().mPlayer.setState(IPlayer.STATE_JUMP);
		// Ôö¼Ó»ðÑÛ£­Öµ
		// fiery_eyes++;
		pPlayer.getStung(5);
	}
}
