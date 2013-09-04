package com.zeroentropy.game.handler;

import com.zeroentropy.game.sprite.Player;

public interface IPlayerCollidable {
	
	public boolean onCollision(final Player pPlayer);
	
}
