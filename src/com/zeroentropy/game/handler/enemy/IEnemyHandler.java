package com.zeroentropy.game.handler.enemy;

import com.zeroentropy.game.handler.ICollisionHandler;
import com.zeroentropy.game.handler.IHandler;
import com.zeroentropy.game.sprite.Player;
import com.zeroentropy.game.sprite.Enemy;

public interface IEnemyHandler extends IHandler<Enemy>,
ICollisionHandler<Enemy, Player> {

public static final long FRAME_DURATION_EACH = 96;

//public void exit(final Enemy pItem);
//
//public void execute(final float pSecondsElapsed, final Enemy pItem);
//
//public void enter(final Enemy pItem);
//
//public void executeCollides(final Enemy pItem, final Player pPlayer);

}