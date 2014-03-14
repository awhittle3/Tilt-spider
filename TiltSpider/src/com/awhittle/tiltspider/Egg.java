/*The following code was borrowed from
 * http://developer.android.com/training/graphics/opengl/index.html
 * and modified to suit the needs of the application.
 * 
 * 
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.awhittle.tiltspider;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

/**
 * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
 */
public class Egg {

	    private final String vertexShaderCode =
	            // This matrix member variable provides a hook to manipulate
	            // the coordinates of the objects that use this vertex shader
	            "uniform mat4 uMVPMatrix;" +
	            "attribute vec4 vPosition;" +
	            "void main() {" +
	            // The matrix must be included as a modifier of gl_Position.
	            // Note that the uMVPMatrix factor *must be first* in order
	            // for the matrix multiplication product to be correct.
	            "  gl_Position = uMVPMatrix * vPosition;" +
	            "}";

	    private final String fragmentShaderCode =
	            "precision mediump float;" +
	            "uniform vec4 vColor;" +
	            "void main() {" +
	            "  gl_FragColor = vColor;" +
	            "}";

	    private final FloatBuffer vertexBuffer;
	    private final ShortBuffer drawListBuffer;
	    private final int mProgram;
	    private int mPositionHandle;
	    private int mColorHandle;
	    private int mMVPMatrixHandle;
	    public static float[] eggCoords;
	    public static int[] eggLocation = new int[2];

	    // number of coordinates per vertex in this array
	    static final int COORDS_PER_VERTEX = 3;
	    private static final float eggTemplate[] = {
	        0.0f, 0.0f, 0.0f,
	        0.1f, 0.1f, 0.0f,
	        0.1f, -0.1f, 0.0f,
	        -0.1f, -0.1f, 0.0f,
	        -0.1f, 0.1f, 0.0f};
	    
	    public static final float initEggCoords[] = ShapeTools.scaleMatrix(eggTemplate, 0.6f);
	    
	    public static final short drawOrder[] = { 0,1,2, 0,3,4 }; // order to draw vertices

	    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

	    float color[] = { 0.7f, 0.7f, 0.7f, 1.0f };

	    /**
	     * Sets up the drawing object data for use in an OpenGL ES context.
	     */
	    public Egg() {

	    	resetEggLocation(-10, 10);
	    	
	        // initialize vertex byte buffer for shape coordinates
	        ByteBuffer bb = ByteBuffer.allocateDirect(
	        // (# of coordinate values * 4 bytes per float)
	                eggCoords.length * 4);
	        bb.order(ByteOrder.nativeOrder());
	        vertexBuffer = bb.asFloatBuffer();
	        vertexBuffer.put(eggCoords);
	        vertexBuffer.position(0);

	        // initialize byte buffer for the draw list
	        ByteBuffer dlb = ByteBuffer.allocateDirect(
	                // (# of coordinate values * 2 bytes per short)
	                drawOrder.length * 2);
	        dlb.order(ByteOrder.nativeOrder());
	        drawListBuffer = dlb.asShortBuffer();
	        drawListBuffer.put(drawOrder);
	        drawListBuffer.position(0);

	        // prepare shaders and OpenGL program
	        int vertexShader = MyGLRenderer.loadShader(
	                GLES20.GL_VERTEX_SHADER,
	                vertexShaderCode);
	        int fragmentShader = MyGLRenderer.loadShader(
	                GLES20.GL_FRAGMENT_SHADER,
	                fragmentShaderCode);

	        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
	        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
	        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
	        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
	    }



		/**
	     * Encapsulates the OpenGL ES instructions for drawing this shape.
	     *
	     * @param mvpMatrix - The Model View Project matrix in which to draw
	     * this shape.
	     */
	    public void draw(float[] mvpMatrix, boolean newEgg) {
	    	
	    	if (newEgg){
	    		MyGLRenderer.mEgg = new Egg();
	    	}
	    	// Add program to OpenGL environment
	        GLES20.glUseProgram(mProgram);

	        // get handle to vertex shader's vPosition member
	        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

	        // Enable a handle to the triangle vertices
	        GLES20.glEnableVertexAttribArray(mPositionHandle);

	        // Prepare the triangle coordinate data
	        GLES20.glVertexAttribPointer(
	                mPositionHandle, COORDS_PER_VERTEX,
	                GLES20.GL_FLOAT, false,
	                vertexStride, vertexBuffer);

	        // get handle to fragment shader's vColor member
	        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

	        // Set color for drawing the triangle
	        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

	        // get handle to shape's transformation matrix
	        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
	        MyGLRenderer.checkGlError("glGetUniformLocation");

	        // Apply the projection and view transformation
	        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
	        MyGLRenderer.checkGlError("glUniformMatrix4fv");

	        // Draw the square
	        GLES20.glDrawElements(
	                GLES20.GL_TRIANGLES, drawOrder.length,
	                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

	        // Disable vertex array
	        GLES20.glDisableVertexAttribArray(mPositionHandle);
	    }
	    
	    private static int prevX = 0;
	    private static int prevY = 0;
	    
	    public static void resetEggLocation(int min, int max) {
			int eggLocationX =  (min + (int)(Math.random() * ((max - min) + 1)));
			int eggLocationY =  (min + (int)(Math.random() * ((max - min) + 1)));
			
			if (eggLocationX >= 10){
				eggLocationX -= 1;
			}
			if (eggLocationY >= 10){
				eggLocationY -= 1;
			}
			if (eggLocationX <= -10){
				eggLocationX += 1;
			}
			if (eggLocationY <= -10){
				eggLocationY += 1;
			}
				
			eggLocation[0] += eggLocationX - prevX;
			eggLocation[1] += eggLocationY - prevY;
			if (eggLocation == Head.headLocation){
				eggLocation[0] = eggLocation[0]+1;
			}
			
			eggCoords = ShapeTools.translateMatrix(initEggCoords, (eggLocationX - prevX)/10.0f, (eggLocationY - prevY)/10.0f);
			
			prevX = eggLocationX;
			prevY = eggLocationY;
	    }
}


