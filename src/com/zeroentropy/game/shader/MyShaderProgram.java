package com.zeroentropy.game.shader;

import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.shader.exception.ShaderProgramLinkException;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.util.math.MathUtils;

import android.opengl.GLES20;

public class MyShaderProgram extends ShaderProgram {

	private static MyShaderProgram INSTANCE;

	public static final String VERTEXSHADER = 
			"uniform mat4 " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n"+ 
			"attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" + 
			//"attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_COLOR + ";\n" + 
			//"varying vec4 " + ShaderProgramConstants.VARYING_COLOR + ";\n" + 
			"void main() {\n" + 
			"	gl_Position = " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" + 
			//"	" + ShaderProgramConstants.VARYING_COLOR + " = " + ShaderProgramConstants.ATTRIBUTE_COLOR + ";\n" + 
			"}";

	public static final String FRAGMENTSHADER = 
		"precision lowp float; \n" + 
			//"varying vec4 " + ShaderProgramConstants.VARYING_COLOR + ";\n" + 
		"uniform vec4 " + MyShaderProgram.MY_UNIFORM_COLOR + ";\n" +
			"void main() {\n" + 
			//"	gl_FragColor = " + ShaderProgramConstants.VARYING_COLOR + ";\n" + 
			"	gl_FragColor = " + MyShaderProgram.MY_UNIFORM_COLOR + ";\n" +
			"}";

	public static int sUniformModelViewPositionMatrixLocation = ShaderProgramConstants.LOCATION_INVALID;
	private static final String MY_UNIFORM_COLOR = "my_u_color";
	public static int sMyUniformColorLocation = ShaderProgramConstants.LOCATION_INVALID;
	
	private MyShaderProgram() {
		super(VERTEXSHADER, FRAGMENTSHADER);
	}
	
	public static MyShaderProgram getInstance(){
		if(MyShaderProgram.INSTANCE == null){
			MyShaderProgram.INSTANCE = new MyShaderProgram();
		}
		return MyShaderProgram.INSTANCE;
	}
	
	@Override
	protected void link(final GLState pGLState) throws ShaderProgramLinkException {
		GLES20.glBindAttribLocation(this.mProgramID, ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION);
		//GLES20.glBindAttribLocation(this.mProgramID, ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR);

		super.link(pGLState);

		MyShaderProgram.sUniformModelViewPositionMatrixLocation = this.getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
		MyShaderProgram.sMyUniformColorLocation = this.getUniformLocation(MyShaderProgram.MY_UNIFORM_COLOR);
	}
	
	@Override
	public void bind(final GLState pGLState, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		GLES20.glDisableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION);

		super.bind(pGLState, pVertexBufferObjectAttributes);

		GLES20.glUniformMatrix4fv(MyShaderProgram.sUniformModelViewPositionMatrixLocation, 1, false, pGLState.getModelViewProjectionGLMatrix(), 0);
		GLES20.glUniform4f(MyShaderProgram.sMyUniformColorLocation, //MathUtils.RANDOM.nextFloat(),
				MathUtils.RANDOM.nextFloat(), 0.0f, 0.0f, 0.8f);
	}

	@Override
	public void unbind(final GLState pGLState) {
		GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION);
		
		super.unbind(pGLState);
	}
	
}
