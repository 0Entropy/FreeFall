package com.zeroentropy.game.handler.enemy;

import java.util.Arrays;

import org.andengine.util.math.MathUtils;
import com.zeroentropy.game.sprite.Enemy;
import com.zeroentropy.game.sprite.Player;


public class JellyfishHandler implements IEnemyHandler {
	// private PhysicsHandler mHandler;
	// private int mType = 0;
	private static final int mFirstTileIndex = 0;
	private static final int mLastTileIndex = 11;
	private static final int mFrameCount = mLastTileIndex - mFirstTileIndex + 1;
	
	private static long[] mFrameDurations = new long[mFrameCount] ;
	//private int[] mFrames = new int[mFrameCount];
	public static JellyfishHandler getInstance(){return INSTANCE;}
	private static JellyfishHandler INSTANCE = new JellyfishHandler();
	private JellyfishHandler(){
		Arrays.fill(mFrameDurations, FRAME_DURATION_EACH);
	}	
	
	private static final int VELOCITY_Y = -120;
	
	public void enter(final Enemy pSprite) {
		if (pSprite.getRotation() != 0)
			pSprite.setRotation(0);
		pSprite.setFlippedHorizontal(MathUtils.randomBoolean());
		pSprite.resetPhysics();

		pSprite.animate(mFrameDurations, mFirstTileIndex, mLastTileIndex);
		
		pSprite.setVelocityY(MathUtils.randomFactor(VELOCITY_Y, 0.5f));
	}

	public void execute(final float pSecondsElapsed, final Enemy pSprite) {

	}

	public void exit(final Enemy pSprite) {

	}

	@Override
	public void executeCollides(final Enemy pSprite, final Player pPlayer) {
		// TODO Auto-generated method stub
		pSprite.brokenSelf();
		// World.getInstance().mPlayer.setState(IPlayer.STATE_JUMP);
		// 增加桃子－生命值
		// peach++;
		pPlayer.getPeach();
	}
	

}
