package com.zeroentropy.game.sprite.visual;

import java.util.LinkedList;
import java.util.ArrayList;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Ellipse;
import org.andengine.util.modifier.IModifier;

import com.zeroentropy.game.ResourcesManager;

public class StungVisual extends Entity {

	private LinkedList<StungVisualParticle> mDisposes = new LinkedList<StungVisualParticle>();
	private ArrayList<StungVisualParticle> mChildren = new ArrayList<StungVisualParticle>();

	public StungVisual() {

	}
	
	public void onShow(){
		StungVisualParticle particle = mDisposes.poll();
		
		if(particle == null){
			particle = new StungVisualParticle();
			StungVisual.this.attachChild(particle);
			mChildren.add(particle);
		}
		
		particle.onDisplay();
		
	}
	
	public void onHide(){
		final ArrayList<StungVisualParticle> pChildren = mChildren;
		if(pChildren.size() > 0 ){
			final int size = pChildren.size();
			for(int i=0; i<size; i++){
				final StungVisualParticle particle = pChildren.get(i);
				if(particle.getAlpha()>0){
					particle.onDisappear();
				}
			}
		}
			
	}
	
	class StungVisualParticle extends Ellipse {

		private Ellipse mEllipse;
		private static final float mRadius = 100;

		private boolean mFinished = true;
		private IEntityModifier mCreateModifier;
		private IEntityModifier mDestoryModifier;

		public StungVisualParticle() {
			super(0, 0, mRadius, mRadius, 10.0f,
					ResourcesManager.getInstance().vbom);
			this.setScale(0.3f);
			this.setColor(1.0f, 0.0f, 0.0f, 1.0f);
//			StungVisualParticle.this.attachChild(mEllipse);
		}

		public void onDisplay() {
			
			if(this.getAlpha() != 1.0f)
				this.setAlpha(1.0f);
			
			if(this.getScaleX() != 0.3f || 
					this.getScaleY() != 0.3f)
				this.setScale(0.3f);
			
			if (mCreateModifier == null) {

				mCreateModifier = new ScaleModifier(0.3f, 0.3f, 1.0f, new IEntityModifierListener(){

					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier,
							IEntity pItem) {
						
					}

					@Override
					public void onModifierFinished(
							IModifier<IEntity> pModifier, IEntity pItem) {
						StungVisualParticle.this.onDisappear();
					}
					
				});

			} else {
				mCreateModifier.reset();
			}
			this.registerEntityModifier(mCreateModifier);
		}

		public void onDisappear() {
			
			if (mDestoryModifier == null) {
				mDestoryModifier = new FadeOutModifier(0.5f, new IEntityModifierListener(){

					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier,
							IEntity pItem) {
						
					}

					@Override
					public void onModifierFinished(
							IModifier<IEntity> pModifier, IEntity pItem) {
						mDisposes.addFirst(StungVisualParticle.this);
						
					}
					
				});
				
			} else {
				mDestoryModifier.reset();
			}
			this.registerEntityModifier(mDestoryModifier);
		}
	}

	// private SingleValueSpanEntityModifier mStopModifier;

}
