package com.zeroentropy.game;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;

import com.zeroentropy.game.fonts.LinearGradientFont;
import com.zeroentropy.game.fonts.StrokeLinearGradientFont;
import com.zeroentropy.game.shader.MyShaderProgram;

public class ResourcesManager {
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private static final ResourcesManager INSTANCE = new ResourcesManager();

	public Engine engine;
	public GameActivity activity;
	public BoundCamera camera;
	public VertexBufferObjectManager vbom;

	// ---------------------------------------------
	// TEXTURES & TEXTURE REGIONS
	// ---------------------------------------------

	// Particle Textture
	private BitmapTextureAtlas particleTextureAtlas;
	public ITextureRegion particle_region;

	// Splash Texture
	private BitmapTextureAtlas splashTextureAtlas;

	public ITextureRegion splash_region;

	// Story Texture
	// private BitmapTextureAtlas storyTextureAtlas;


	// Menu Texture
	private BitmapTextureAtlas menuTextureAtlas;

	public ITextureRegion bg_title_region;
	public ITextureRegion but_play_mainmenu_region;
	public ITextureRegion lose_score_title_region;
	public ITextureRegion bg_story_region;
	// public ITextureRegion options_region;

	// Game Texture
	public BuildableBitmapTextureAtlas gameTextureAtlas;

	public ITextureRegion platform1_region;
	public ITextureRegion platform2_region;
	public ITextureRegion platform3_region;
	public ITextureRegion coin_region;

	// Game AutoParallaxBackground Texture
	private BitmapTextureAtlas mBackgroundTextureAtlas_0;

	public ITextureRegion mSkyTopRegion;
	public ITextureRegion mSkyBottomRegion;
	public ITextureRegion mCloudRegion;

	private BitmapTextureAtlas mBackgroundTextureAtlas_1;
	private BitmapTextureAtlas mBackgroundTextureAtlas_2;
	public ITextureRegion mSkyRegion_0;
	public ITextureRegion mSkyRegion_1;
	public ITextureRegion mSkyRegion_2;
	public ITextureRegion mSkyRegion_3;

	// Game Sprite Texture
	private BuildableBitmapTextureAtlas mSpritesTextureAtlas;

	public TiledTextureRegion mSpritesRegion;

	private BuildableBitmapTextureAtlas mInkAtlas;
	public TiledTextureRegion mInkRegion;

	private BuildableBitmapTextureAtlas mWukongTextureAtlas;

	public TiledTextureRegion mBodyRegion;
	public TiledTextureRegion mHandRegion;
	public TiledTextureRegion mFlashRegion;
	
	public TiledTextureRegion mSupplyBackgroundRegion;
	public TiledTextureRegion mSupplyRegion;

	// Game Button Texture
	private BuildableBitmapTextureAtlas mButtonAtlas;
	public ITextureRegion mExitBut;
	public ITextureRegion mIntroBut;
	public ITextureRegion mPauseBut;
	public ITextureRegion mPlayBut;
	public ITextureRegion mResumeBut;

//	public ITextureRegion mItemSquid;
	// ---------------------------------------------
	// FONTS
	// ---------------------------------------------

	public Font lFont;// for bestScore;
	public Font mFont;// for hits_combo;
	public Font hFont;
	public Font xhFont;

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	/**
	 * @param engine
	 * @param activity
	 * @param camera
	 * @param vbom
	 * <br>
	 * <br>
	 *            We use this method at beginning of game loading, to prepare
	 *            Resources Manager properly, setting all needed parameters, so
	 *            we can latter access them from different classes (eg. scenes)
	 */
	public static void prepareManager(Engine engine, GameActivity activity,
			BoundCamera camera, VertexBufferObjectManager vbom) {
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
	}

	public static ResourcesManager getInstance() {
		return INSTANCE;
	}

	public void loadMenuResources() {
		loadMenuGraphics();
		loadMenuAudio();
		loadMenuFonts();
	}

	public void loadGameResources() {
		loadGameBackground();
		loadGameGraphics();
		loadSpriteGraphics();
		loadParticleGraphics();// Á£×ÓÍ¼Æ¬×ÊÔ´
		loadGameButton();
		loadGameFonts();
		loadGameAudio();
		loadShaderProgram();
	}

	public void unloadGameTextures() {
		// TODO (Since we did not create any textures for game scene yet)
	}

	public void unloadMenuTextures() {
		menuTextureAtlas.unload();
	}

	public void loadMenuTextures() {
		menuTextureAtlas.load();
	}

	// public void loadStoryScreen() {
	// BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
	// storyTextureAtlas = new BitmapTextureAtlas(
	// activity.getTextureManager(), 512, 1024,
	// TextureOptions.BILINEAR);
	// story_region = BitmapTextureAtlasTextureRegionFactory
	// .createFromAsset(menuTextureAtlas, activity,
	// "story.jpg",0,0);
	// storyTextureAtlas.load();
	// }
	//
	// public void unloadStoryScreen() {
	// storyTextureAtlas.unload();
	// story_region = null;
	// }

	public void loadSplashScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), 512, 1024,
				TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTextureAtlas, activity, "logo.png", 0, 0);
		splashTextureAtlas.load();
	}

	public void unloadSplashScreen() {
		splashTextureAtlas.unload();
		splash_region = null;
	}

	// ---------------------------------------------
	// PRIVATE METHODS
	// ---------------------------------------------
	private void loadShaderProgram() {
		engine.getShaderProgramManager().loadShaderProgram(
				MyShaderProgram.getInstance());
	}

	private void loadSpriteGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/sprites/");

		mInkAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 512, 1024,
				TextureOptions.BILINEAR);
		mInkRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mInkAtlas, activity, "ink.png", 1, 1);
		mSupplyBackgroundRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mInkAtlas, activity, "light.png", 1,
						1);
		mSupplyRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mInkAtlas, activity, "supply.png",
						4, 1);
		// mInkAtlas.load();

		mSpritesTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 1024, 512,
				TextureOptions.BILINEAR);

		mSpritesRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mSpritesTextureAtlas, activity,
						"sprite.png", 12, 4);

		mWukongTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 1024, 512,
				TextureOptions.BILINEAR);

		mBodyRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mWukongTextureAtlas, activity,
						"body.png", 6, 2);

		mHandRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mWukongTextureAtlas, activity,
						"hand.png", 1, 1);
		
		mFlashRegion = BitmapTextureAtlasTextureRegionFactory
		.createTiledFromAsset(this.mWukongTextureAtlas, activity,
				"flash.png", 1, 1);

		try {
			this.mInkAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 1));
			this.mInkAtlas.load();
			this.mSpritesTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 1));
			this.mSpritesTextureAtlas.load();
			this.mWukongTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 1));
			this.mWukongTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	private void loadGameGraphics() {
		// load Platform Texture
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		gameTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);

		platform1_region = BitmapTextureAtlasTextureRegionFactory 
				.createFromAsset(gameTextureAtlas, activity, "platform1.png");
		platform2_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, activity, "platform2.png");
		platform3_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, activity, "platform3.png");
		coin_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				gameTextureAtlas, activity, "coin.png");

		try {
			this.gameTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 1));
			this.gameTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	private void loadGameButton() {
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/game/button/");
		mButtonAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 1024, 256, TextureOptions.BILINEAR);

		mExitBut = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mButtonAtlas, activity, "but_exit.png");
		mIntroBut = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mButtonAtlas, activity, "but_intro.png");
		mPauseBut = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mButtonAtlas, activity, "but_pause.png");
		mPlayBut = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mButtonAtlas, activity, "but_play.png");
		mResumeBut = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mButtonAtlas, activity, "but_resume.png");
//		mItemSquid = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
//				mButtonAtlas, activity, "squid_menu_item.png");

		lose_score_title_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				mButtonAtlas, activity, "lose_score_title.png");

		try {
			this.mButtonAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 1));
			this.mButtonAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	private void loadGameBackground() {
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/game/background/");

		this.mBackgroundTextureAtlas_0 = new BitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024);

		this.mSkyTopRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBackgroundTextureAtlas_0, activity,
						"sky_top.png", 0, 0);
		this.mSkyBottomRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBackgroundTextureAtlas_0, activity,
						"sky_bottom.png", 301, 0);
		this.mCloudRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBackgroundTextureAtlas_0, activity,
						"cloud000.png", 602, 0);

		this.mBackgroundTextureAtlas_0.load();

		this.mBackgroundTextureAtlas_1 = new BitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024);
		this.mBackgroundTextureAtlas_2 = new BitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024);

		this.mSkyRegion_0 = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBackgroundTextureAtlas_1, activity,
						"sky_0.png", 0, 0);
		this.mSkyRegion_1 = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBackgroundTextureAtlas_1, activity,
						"sky_1.png", 481, 0);

		this.mSkyRegion_2 = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBackgroundTextureAtlas_2, activity,
						"sky_2.png", 0, 0);
		this.mSkyRegion_3 = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBackgroundTextureAtlas_2, activity,
						"sky_3.png", 481, 0);

		this.mBackgroundTextureAtlas_1.load();
		this.mBackgroundTextureAtlas_2.load();
		/*
		 * this.mParallaxLayerMid = BitmapTextureAtlasTextureRegionFactory
		 * .createFromAsset(this.mAutoParallaxBackgroundTexture, activity,
		 * "parallax_background_layer_mid.png", 0, 645);
		 */

	}

	private void loadMenuFonts() {
//		FontFactory.setAssetBasePath("font/");
		final ITexture xhFontTexture = new BitmapTextureAtlas(
				activity.getTextureManager(), 512, 256,
				TextureOptions.BILINEAR);
		
		final ITexture hFontTexture = new BitmapTextureAtlas(
				activity.getTextureManager(), 512, 128,
				TextureOptions.BILINEAR);

		final ITexture mFontTexture = new BitmapTextureAtlas(
				activity.getTextureManager(), 512, 32,
				TextureOptions.BILINEAR);//_PREMULTIPLYALPHA);
		final ITexture lFontTexture = new BitmapTextureAtlas(
				activity.getTextureManager(), 512, 16,
				TextureOptions.BILINEAR);//_PREMULTIPLYALPHA

		// "foughtknight.ttf"//"simplex.ttf"//"tf2build.ttf"//New Super Mario Font U.ttf
		final Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "font/oceanicdrift.ttf");
		lFont = new LinearGradientFont(activity.getFontManager(),
				lFontTexture, typeface, 26, true,
				Color.WHITE_ABGR_PACKED_INT, 0XFFFFBF00, 0XFFFF5F2F);//0XFFFFFF00 -> 0XFFFF5F00
		
		lFont.load();
		
		final int m_colors[] = new int[] { 0XFFFFFF00, 0XFFFFFFCF, 0XFFFFBF00,
				0XFFFFFF00 }; // 0X_AA_RR_GG_BB
		final float positions[] = new float[] { 0.0F, 0.44F, 0.45F, .8F };

		mFont = new StrokeLinearGradientFont(activity.getFontManager(),
				mFontTexture, typeface, 48, true,
				0XFFFFFFFF, m_colors, positions, 1, 0XFFFF9F2F, false);

		mFont.load();

//		hFont = new StrokeLinearGradientFont(activity.getFontManager(),
//				hFontTexture, typeface, 64, true,
//				Color.WHITE_ABGR_PACKED_INT, m_colors, positions, 1, 0XFFFF9F2F, false);
//
//		hFont.load();
		
//		final float h_positions[] = new float[] { 0.0F, 0.52F, 0.53F, .8F };
		final int h_colors[] = new int[] { 0XFFFFFFCF, 0XFFFFFF00, 0XFFFFBF00,
				0XFFFFFFCF };
		hFont = new LinearGradientFont(activity.getFontManager(),
				hFontTexture, typeface, 72, true,
				Color.WHITE_ABGR_PACKED_INT, h_colors, positions);//, 2, 0XFFFFFFFF, false);

		hFont.load();
		
		xhFont = new LinearGradientFont(activity.getFontManager(),
				xhFontTexture, typeface, 128, true,
				Color.WHITE_ABGR_PACKED_INT, h_colors, positions);//, 2, 0XFFFFFFFF, false);

		xhFont.load();

	}

	private void loadMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		menuTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),
				1024, 1024, TextureOptions.BILINEAR);

		bg_title_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				menuTextureAtlas, activity, "title.png", 0, 0);
		
		
		but_play_mainmenu_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				menuTextureAtlas, activity, "but_play.png", 0, 801);
		
		
		bg_story_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				menuTextureAtlas, activity, "story.jpg", 481, 0);
		// options_region = BitmapTextureAtlasTextureRegionFactory
		// .createFromAsset(menuTextureAtlas, activity, "options.png", 0, 801);

		this.menuTextureAtlas.load();

		// try {
		// // this.menuTextureAtlas
		// // .build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource,
		// BitmapTextureAtlas>(
		// // 0, 0, 1));
		//
		// } catch (final TextureAtlasBuilderException e) {
		// Debug.e(e);
		// }

	}

	private void loadParticleGraphics() {
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/particle/");
		particleTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), 32, 32,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		particle_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(particleTextureAtlas, activity,
						"particle_rectangle.png", 0, 0);

		particleTextureAtlas.load();
	}

	private void loadMenuAudio() {

	}

	private void loadGameFonts() {

	}

	private void loadGameAudio() {

	}

	// ---------------------------------------------
	// GETTERS AND SETTERS
	// ---------------------------------------------
}
