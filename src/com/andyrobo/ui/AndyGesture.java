package com.andyrobo.ui;

import java.lang.reflect.Method;
import android.view.MotionEvent;

import processing.core.PApplet;
import processing.core.PVector;
import ketai.ui.KetaiGesture;

/**
 * This class is extended from KetaiGesture to ensure compatibility
 * Provides gesture recognition services to a processing sketch.
 * 		To receive gesture events a sketch can define the following methods:<br /><br />
 * 
 * 		void onTap(float x, float y)  - x, y location of the tap<br />
 * 		void onDoubleTap(float x, float y) - x,y location of double tap<br />
 *		void onFlick(float x, float y, float px, float py, float v) - x,y where flick ended, px,py - where flick began, v - velocity of flick in pixels/sec <br /> 
 *		void onScroll(int x int y) - not currently used<br />
 *		void onLongPress(float x, float y)  - x, y position of long press<br />
 *		void onPinch(float x, float y, float r) - x,y of center, r is the distance change<br />
 *		void onRotate(float x, float y, float a) - x, y of center, a is the angle change in radians<br />
 *
 *		In addition, you can use : 
 *		void onFling(float x, float y, float px, float py, float vx, float vy) - x,y where flick ended, px,py - where flick began, vx - velocity of flick along X axis in pixels/sec, vy - velocity of flick along Y axis in pixels/sec <br />
 *
 *		If you have defined onFling in your sketch, onFlick will not be called even if it has been defined. 
 */


public class AndyGesture extends KetaiGesture {
	
	/** The parent. */
	PApplet parent;

	/** The on Fling method. */
	Method onFlingMethod, onFlickMethod;
	
	/**
	 * Instantiates a new Andy gesture, an extended class of ketai gesture
	 *
	 * @param _parent the PApplet/sketch
	 */

	public AndyGesture(PApplet _parent) {
		super(_parent);
		parent = _parent;
		findParentIntentions();
	}
	
	/**
	 * Find parent intentions.
	 */
	private void findParentIntentions() {
		try {
			onFlingMethod = parent.getClass().getMethod("onFling",
					new Class[] { float.class, float.class, float.class,
					float.class,float.class,float.class});
		} catch (Exception e) {
		}
	}
	
	/**
	 * Surface touch event.
	 *
	 * @param event the event
	 * @return true, if successful
	 */
	public boolean surfaceTouchEvent(MotionEvent event) {
		return super.surfaceTouchEvent(event);
	}
	
	/* (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		if (onFlingMethod != null) {
			try {
				onFlingMethod.invoke(parent,
						new Object[] { arg1.getX(), arg1.getY(), arg0.getX(),
								arg0.getY(), arg2,arg3 });
				return true;
			} catch (Exception e) {
			}
		}
		else if (onFlickMethod != null) {
				try {
					PVector v = new PVector(arg2, arg3);
					
					onFlickMethod.invoke(parent,
							new Object[] { arg1.getX(), arg1.getY(), arg0.getX(),
									arg0.getY(), v.mag() });
					return true;
				} catch (Exception e) {
				}				
			}

		return false;
	}
	
	

}
