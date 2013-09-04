package com.zeroentropy.game.handler.bonus;

import com.zeroentropy.game.handler.ICollisionHandler;
import com.zeroentropy.game.handler.IHandler;
import com.zeroentropy.game.sprite.Player;
import com.zeroentropy.game.sprite.Bonus;

public interface ISupplyHandler extends IHandler<Bonus>, ICollisionHandler<Bonus, Player> {

}
