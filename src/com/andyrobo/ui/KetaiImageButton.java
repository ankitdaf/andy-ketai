package com.andyrobo.ui;

import java.lang.reflect.Method;

import processing.core.PApplet;
import processing.core.PImage;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

/**
 * 
 * Creates an Android ImageButton
 * 
 * To process the buttonClick a sketch can define the following methods :<br /><br />
 * 
 * 		void namePressed()  - "name" is the name of the button supplied in the constructor<br />
 * 
 * To modify the button properties a sketch can call the following methods :<br /><br />
 * 
 * 		void setPosition(float x, float y) - x,y location for the button
 * 		void setVisibility(boolean isVisible) - make button visible or invisible
 * 
 * 
 * @author ankitdaf
 *
 */
public class KetaiImageButton extends ImageButton {
	
	/** The parent. */
	private PApplet parent;

	/** The name. */
	String name = "KetaiImageButton";
	
	/** The layout. */
	RelativeLayout layout;
	
	/** Button height. */
	int height=50;
	
	/** Button width. */
	int width=50;
	
	/** Image name. */
	String imgname="";
	
	/** Manual Resize. */
	boolean resize=false;
	
	/** The implemented onClick method for the button	 */
	Method buttonMethod;

	/**
	 * Instantiates a new ketai button.
	 *
	 * @param _parent the _parent
	 * @param label the button label
	 */
	public KetaiImageButton(PApplet _parent, final String image,final String _name, int _width, int _height ) {
		super(_parent.getApplicationContext());
		resize = true;
		width = _width;
		height = _height;
		setupButton(_parent,image,_name);		
	}

	/**
	 * Instantiates a new ketai button.
	 *
	 * @param _parent the _parent
	 * @param label the button label
	 */
	public KetaiImageButton(PApplet _parent,final String image,final String _name) {
		super(_parent.getApplicationContext());
		setupButton(_parent,image,_name);
	}
	
	/**
	 * 
	 * Instantiates a new ketai button with supplied parameters.
	 * 
	 * @param _parent the PApplet
	 * @param image the image filename
	 * @param label the button reference
	 */
	private void setupButton(PApplet _parent, final String image,final String _name) {
		parent = _parent;
		name = _name;
		imgname = image;
		init();
		findParentIntentions();
		this.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				if (buttonMethod != null) {
				try {
					buttonMethod.invoke(parent,null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}}
		});
	}
	
	/**
	 * Find parent intentions.
	 */
	private void findParentIntentions() {
		try {
			buttonMethod = parent.getClass().getMethod(name+"Pressed",null);
		} catch (Exception e) {
			PApplet.println("The "+name+"Pressed method has not been implemented.Nothing to do here");
		}
	}
	
	/**
	 * Inits the button
	 */
	private void init() {
		layout = new RelativeLayout(parent);
		RelativeLayout.LayoutParams btnparams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		btnparams.setMargins(2,2,2,2);
		PImage img = parent.loadImage(imgname);
		this.setImageBitmap((Bitmap) img.getNative());
		if(resize){
			btnparams.height = height;
			btnparams.width = width;
		}
		else {
			btnparams.height = img.height;
			btnparams.width = img.width;
		}
		layout.addView(this,btnparams);
		parent.runOnUiThread(new Runnable() {
			
			public void run() {
				parent.addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
			}
		});		
	}
	
	/** 
	 * 
	 * Set the (x,y) co-ordinates for the button
	 * 
	 * @param x x-position for button
	 * @param y y-position for button
	 */
	public void setPosition(float x,float y) {
		this.setX(x);
		this.setY(y);
	}
	
	/**
	 * 
	 * Make the button visible / invisible
	 * 
	 * @param isVisible true if visible, false otherwise
	 */
	public void setVisibility(boolean isVisible) {
		if (isVisible) this.setVisibility(VISIBLE);
		else this.setVisibility(INVISIBLE);
	}

}
