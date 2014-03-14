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

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";
    private Head mHead;
    private Square mSquare;
    public static Egg mEgg;

    private boolean eggEaten = false;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final static float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.9f, 1.0f);

        mSquare = new Square();
        mEgg = new Egg();
    }

    @Override
    public void onDrawFrame(GL10 unused) {

        //Pause for a short amount of time
		try {
		    Thread.sleep(MainActivity.stepTime);
		}
		catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		


		
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // Draw square background
        mSquare.draw(mMVPMatrix);
        
        //Is snake in bounds?
        if (Head.headLocation[0] > 9 || Head.headLocation[0] < -9 ||
        		Head.headLocation[1] > 9 || Head.headLocation[1] < -9){

        	GameActivity.snakeAlive = false;
        }
        

       
        //Was egg eaten?
        if ((Head.headLocation[0] == Egg.eggLocation[0]) && (Head.headLocation[1] == Egg.eggLocation[1])){
        	eggEaten = true;
        	GameActivity.counter++; //Increase score
        }
        
        //Draw egg. Draw in new location if eaten
        mEgg.draw(mMVPMatrix, eggEaten);
        

        //Draw head in new location
        mHead = new Head();
    	Movement.moveSnake(GameActivity.dir);
        mHead.draw(mMVPMatrix);

        //Reset eggEaten flag
		if (eggEaten){
			eggEaten = false;
		}
		

        

    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

    }

    /**
     * Utility method for compiling a OpenGL shader.
     *
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
    * Utility method for debugging OpenGL calls. Provide the name of the call
    * just after making it:
    *
    * <pre>
    * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
    * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
    *
    * If the operation is not successful, the check throws an error.
    *
    * @param glOperation - Name of the OpenGL call to check.
    */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
    
    /*
	private void elaborateTail(int counter) {
		//Draw tail in old location
		int tailLength = counter;
		if(tailLength == 0){
			return;
		}
			if (tailLength == 3){
				
				oldTailCoords3 = putTail( oldTailCoords2, oldTailLocation2);
				oldTailLocation3 = oldTailLocation2;
			} else
			if (tailLength == 2){
				
				oldTailCoords2 = putTail( oldTailCoords1, oldTailLocation1);
				oldTailLocation2 = oldTailLocation1;
			} else
			if (tailLength == 1){
				
			//	oldTailCoords1 = putTail(oldHeadCoords, oldHeadLocation);
			//	oldTailLocation1 = oldHeadLocation;
			}
		
        	tailLength--;
        	elaborateTail(tailLength);
        
    }

	private static float[] putTail(float[] coords, int[] location) {
		Tail.tailLocation = location;
		Tail.tailCoords = coords;
		Tail mTail = new Tail();
		mTail.draw(mMVPMatrix);
		return coords;
	}
     */
}