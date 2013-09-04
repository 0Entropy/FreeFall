package com.zeroentropy.game.fonts;

import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.ITexture;
import org.andengine.util.color.Color;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;

public class LinearGradientFont extends Font {

	
	public LinearGradientFont(final FontManager pFontManager, final ITexture pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final Color pColor) {
		this(pFontManager, pTexture, pTypeface, pSize, pAntiAlias, pColor.getARGBPackedInt());
	}

	public LinearGradientFont(final FontManager pFontManager, final ITexture pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColorARGBPackedInt) {
//		super(pFontManager, pTexture, pTypeface, pSize, pAntiAlias, pColorARGBPackedInt);
		this(pFontManager, pTexture, pTypeface, pSize, pAntiAlias, pColorARGBPackedInt, 0XFFFFFFFF, 0XFF000000);
		
	}
	
	public LinearGradientFont(final FontManager pFontManager, final ITexture pTexture, final Typeface pTypeface, final float pSize, 
			final boolean pAntiAlias, final int pColorARGBPackedInt, final int color_0, final int color_1) {
		super(pFontManager, pTexture, pTypeface, pSize, pAntiAlias, pColorARGBPackedInt);
		
		float height = -this.getAscent();
		LinearGradient pLinearGradient = new LinearGradient(0, 0, 0, height,
				color_0, color_1, Shader.TileMode.CLAMP);
		this.mPaint.setShader(pLinearGradient);
	}
	
	public LinearGradientFont(final FontManager pFontManager, final ITexture pTexture, final Typeface pTypeface, final float pSize, 
			final boolean pAntiAlias, final int pColorARGBPackedInt, final int colors[], final float positions[]) {
		super(pFontManager, pTexture, pTypeface, pSize, pAntiAlias, pColorARGBPackedInt);
		
		float height = -this.getAscent();
		LinearGradient pLinearGradient = new LinearGradient(0, 0, 0, height,
				colors, positions, Shader.TileMode.CLAMP);
		this.mPaint.setShader(pLinearGradient);
	}
	
	//-------------------------------------------------------
	
	
}
