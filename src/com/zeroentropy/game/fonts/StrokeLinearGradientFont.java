package com.zeroentropy.game.fonts;

import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.StrokeFont;
import org.andengine.opengl.texture.ITexture;
import org.andengine.util.color.Color;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.Log;

public class StrokeLinearGradientFont extends StrokeFont {

	public StrokeLinearGradientFont(final FontManager pFontManager,
			final ITexture pTexture, final Typeface pTypeface,
			final float pSize, final boolean pAntiAlias, final Color pColor,
			final float pStrokeWidth, final Color pStrokeColor) {
		this(pFontManager, pTexture, pTypeface, pSize, pAntiAlias, pColor
				.getARGBPackedInt(), pStrokeWidth, pStrokeColor
				.getARGBPackedInt());
	}

	public StrokeLinearGradientFont(final FontManager pFontManager,
			final ITexture pTexture, final Typeface pTypeface,
			final float pSize, final boolean pAntiAlias,
			final int pColorARGBPackedInt, final float pStrokeWidth,
			final int pStrokeColorARGBPackedInt) {
		this(pFontManager, pTexture, pTypeface, pSize, pAntiAlias,
				pColorARGBPackedInt, pStrokeWidth, pStrokeColorARGBPackedInt,
				false);
	}

	public StrokeLinearGradientFont(final FontManager pFontManager,
			final ITexture pTexture, final Typeface pTypeface,
			final float pSize, final boolean pAntiAlias, final Color pColor,
			final float pStrokeWidth, final Color pStrokeColor,
			final boolean pStrokeOnly) {
		this(pFontManager, pTexture, pTypeface, pSize, pAntiAlias, pColor
				.getARGBPackedInt(), pStrokeWidth, pStrokeColor
				.getARGBPackedInt(), pStrokeOnly);
	}

	public StrokeLinearGradientFont(final FontManager pFontManager,
			final ITexture pTexture, final Typeface pTypeface,
			final float pSize, final boolean pAntiAlias,
			final int pColorARGBPackedInt, final float pStrokeWidth,
			final int pStrokeColorARGBPackedInt, final boolean pStrokeOnly) {
		this(pFontManager, pTexture, pTypeface, pSize, pAntiAlias,
				pColorARGBPackedInt, 0XFFFFFFFF, 0XFF000000, pStrokeWidth,
				pStrokeColorARGBPackedInt, pStrokeOnly);

	}

	public StrokeLinearGradientFont(final FontManager pFontManager,
			final ITexture pTexture, final Typeface pTypeface,
			final float pSize, final boolean pAntiAlias,
			final int pColorARGBPackedInt, int colors[], float positions[],
			final float pStrokeWidth, final int pStrokeColorARGBPackedInt,
			final boolean pStrokeOnly) {
		super(pFontManager, pTexture, pTypeface, pSize, pAntiAlias,
				pColorARGBPackedInt, pStrokeWidth, pStrokeColorARGBPackedInt,
				pStrokeOnly);

		float height = -this.getAscent();
		Shader _originalShader = new LinearGradient(0, 0, 0, height, colors,
				positions, Shader.TileMode.CLAMP);
//		this.mPaint.setAlpha(0X0F);
		this.mPaint.setShader(_originalShader);
	}

	public StrokeLinearGradientFont(final FontManager pFontManager,
			final ITexture pTexture, final Typeface pTypeface,
			final float pSize, final boolean pAntiAlias,
			final int pColorARGBPackedInt, int color_0, int color_1,
			final float pStrokeWidth, final int pStrokeColorARGBPackedInt,
			final boolean pStrokeOnly) {
		super(pFontManager, pTexture, pTypeface, pSize, pAntiAlias,
				pColorARGBPackedInt, pStrokeWidth, pStrokeColorARGBPackedInt,
				pStrokeOnly);

		float height = -this.getAscent();
		Shader _originalShader = new LinearGradient(0, 0, 0, height,
				color_0, color_1, Shader.TileMode.CLAMP);
		this.mPaint.setShader(_originalShader);
	}

}
