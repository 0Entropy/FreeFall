package com.zeroentropy.game.handler;

import java.util.ArrayList;
import org.andengine.engine.handler.IUpdateHandler;

import com.zeroentropy.game.sprite.Player;

public class PlayerCollisionHandler implements IUpdateHandler {

	private ArrayList<IPlayerCollidable> mPlayerCollidables;
	private Player mPlayer;

	public PlayerCollisionHandler(final Player pPlayer,
			final ArrayList<IPlayerCollidable> pPlayerCollidable) {
		mPlayer = pPlayer;
		mPlayerCollidables = pPlayerCollidable;
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {

		final ArrayList<IPlayerCollidable> playerCollidables = mPlayerCollidables;
		final Player player = mPlayer;
		final int count = playerCollidables.size();
		
		for (int i = 0; i < count; i++) {
			playerCollidables.get(i).onCollision(player);
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	// private Player mPlayer;

}
