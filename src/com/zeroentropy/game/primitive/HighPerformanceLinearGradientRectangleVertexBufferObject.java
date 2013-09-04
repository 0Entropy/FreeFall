package com.zeroentropy.game.primitive;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.primitive.Rectangle.IRectangleVertexBufferObject;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.util.color.Color;

public class HighPerformanceLinearGradientRectangleVertexBufferObject  extends HighPerformanceVertexBufferObject

implements IRectangleVertexBufferObject {

//	private Color mFromColor ;
//	private Color mToColor ;
//	private Co
	private Color[] mColors;
	private LinearGradientDirection mDirection;
	
	public HighPerformanceLinearGradientRectangleVertexBufferObject(final Color pFromColor, final Color pToColor, final LinearGradientDirection pDirection, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pVertexBufferObjectManager, Rectangle.RECTANGLE_SIZE, DrawType.STATIC, true, Rectangle.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT);
//		mFromColor = pFromColor;
//		mToColor = pToColor;
		Color centerColor = new Color(1,1,1);
		centerColor.mix(pFromColor, .5f, pToColor, .5f);
		mColors = new Color[]{pFromColor, centerColor, pToColor };
		mDirection = pDirection;
	}

	@Override
	public void onUpdateColor(final Rectangle pRectangle) {
		final float[] bufferData = this.mBufferData;

//		final float packedColor = pRectangle.getColor().getABGRPackedFloat();
//		final float fromColorf = mFromColor.getABGRPackedFloat();
//		final float toColorf = mToColor.getABGRPackedFloat();

		bufferData[0 * Rectangle.VERTEX_SIZE + Rectangle.COLOR_INDEX] = mColors[mDirection.getTopLeftColorIndex()].getABGRPackedFloat();//packedColor;
		bufferData[1 * Rectangle.VERTEX_SIZE + Rectangle.COLOR_INDEX] = mColors[mDirection.getBottomLeftColorIndex()].getABGRPackedFloat();;
		bufferData[2 * Rectangle.VERTEX_SIZE + Rectangle.COLOR_INDEX] = mColors[mDirection.getTopRightColorIndex()].getABGRPackedFloat();;
		bufferData[3 * Rectangle.VERTEX_SIZE + Rectangle.COLOR_INDEX] = mColors[mDirection.getBottomRightColorIndex()].getABGRPackedFloat();;

		this.setDirtyOnHardware();
	}

	@Override
	public void onUpdateVertices(final Rectangle pRectangle) {
		final float[] bufferData = this.mBufferData;

		final float x = 0;
		final float y = 0;
		final float x2 = pRectangle.getWidth(); // TODO Optimize with field access?
		final float y2 = pRectangle.getHeight(); // TODO Optimize with field access?

		bufferData[0 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_X] = x;
		bufferData[0 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_Y] = y;

		bufferData[1 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_X] = x;
		bufferData[1 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_Y] = y2;

		bufferData[2 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_X] = x2;
		bufferData[2 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_Y] = y;

		bufferData[3 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_X] = x2;
		bufferData[3 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_Y] = y2;

		this.setDirtyOnHardware();
	}

	public static enum LinearGradientDirection {
		// ===========================================================
		// Elements
		// ===========================================================

		LEFT_TO_RIGHT(0, 0, 2, 2),
		RIGHT_TO_LEFT(2, 2, 0, 0),
		BOTTOM_TO_TOP(2, 0, 2, 0),
		TOP_TO_BOTTOM(0, 2, 0, 2),
		TOPLEFT_TO_BOTTOMRIGHT(0, 1, 1, 2),
		BOTTOMRIGHT_TO_TOPLEFT(2, 1, 1, 0),
		TOPRIGHT_TO_BOTTOMLEFT(1, 2, 0, 1),
		BOTTOMLEFT_TO_TOPRIGHT(1, 0, 2, 1);

		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final int mTopLeftColorIndex;
		private final int mBottomLeftColorIndex;
		private final int mTopRightColorIndex;
		private final int mBottomRightColorIndex;

		// ===========================================================
		// Constructors
		// ===========================================================

		private LinearGradientDirection(final int pTopLeftColorIndex, final int pBottomLeftColorIndex, 
				final int pTopRightColorIndex, final int pBottomRightColorIndex) {
			this.mTopLeftColorIndex = pTopLeftColorIndex;
			this.mBottomLeftColorIndex = pBottomLeftColorIndex;
			this.mTopRightColorIndex = pTopRightColorIndex;
			this.mBottomRightColorIndex = pBottomRightColorIndex;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		final int getTopLeftColorIndex() {
			return this.mTopLeftColorIndex;
		}

		final int getBottomLeftColorIndex() {
			return this.mBottomLeftColorIndex ;
		}

		final int getTopRightColorIndex() {
			return this.mTopRightColorIndex;
		}

		final int getBottomRightColorIndex() {
			return this.mBottomRightColorIndex;
		}

		// ===========================================================
		// Methods from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
	
}
