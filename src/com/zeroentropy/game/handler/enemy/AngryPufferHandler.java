package com.zeroentropy.game.handler.enemy;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.modifier.EntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.util.math.MathUtils;

import com.zeroentropy.game.sprite.Enemy;
import com.zeroentropy.game.sprite.Player;
import com.zeroentropy.game.util.ZeroConstants;

public class AngryPufferHandler implements IEnemyHandler {

	public static AngryPufferHandler getInstance() {
		return INSTANCE;
	}

	private static AngryPufferHandler INSTANCE = new AngryPufferHandler();

	private AngryPufferHandler() {
		// Arrays.fill(mFrameDurations, FRAME_DURATION_EACH);
	}

	private static final int mCurrentTileIndex = 47;
	private static final int VELOCITY_Y = -20;// -24;
//	private static final int MAX_VELOCITY_Y = -30;// -24;

	public void enter(final Enemy pSprite) {

		if (pSprite.getRotation() != 0)
			pSprite.setRotation(0);
		// pSprite.setFlippedHorizontal(MathUtils.randomBoolean());
		pSprite.resetPhysics();

		pSprite.stopAnimation(mCurrentTileIndex);

		LoopEntityModifier modifier = new LoopEntityModifier(new SequenceEntityModifier(
				new RotationModifier(.75f, -8, 8), 
				new RotationModifier(.75f, 8, -8)));
		pSprite.registerEntityModifier(modifier);

		pSprite.setVelocityY(MathUtils.randomFactor(VELOCITY_Y, 0.5f));
	}

	public void execute(final float pSecondsElapsed, final Enemy pSprite) {

		if (pSprite.getX() < 0) {
			pSprite.turnRight();
		} else if (pSprite.getX() > ZeroConstants.SCENE_WIDTH
				- ZeroConstants.BASE_SPRITE_WIDTH) {
			pSprite.turnLeft();
		}

	}

	public void exit(final Enemy pSprite) {
		pSprite.clearEntityModifiers();
	}

	@Override
	public void executeCollides(final Enemy pSprite, final Player pPlayer) {
		// TODO Auto-generated method stub
		pSprite.brokenSelf();
		// World.getInstance().mPlayer.setState(IPlayer.STATE_JUMP);
		// Ôö¼Ó»ðÑÛ£­Öµ
		// fiery_eyes++;
		pPlayer.getStung(20);
	}
}
