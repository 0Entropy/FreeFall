package com.zeroentropy.game.handler;

public interface IHandler<T>{
	
	public void exit(final T pItem);
	
	public void execute(final float pSecondsElapsed, final T pItem);
	
//	public void executeCollides(final T pItem, final Player pPlayer);

	public void enter(final T pItem);
}
