package com.zeroentropy.game.handler;

public interface ICollisionHandler<T, K> {

	public void executeCollides(final T pItem, final K pTarget);

}
