package com.andyrobo.ui;

import java.lang.reflect.Method;

import processing.core.PApplet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

/**
 * 
 * Creates an Android Button. All methods below are in addition to the native Android methods
 * 
 * To process the buttonClick a sketch can define the following methods :<br /><br />
 * 
 * 		void namePressed()  - "name" is the name of the button supplied in the constructor<br />
 * 
 * To modify the button properties a sketch can call the following methods :<br /><br />
 * 
 * 		void setPosition(float x, float y) - x,y location for the button<br />
 * 		void setVisibility(boolean isVisible) - button visible or invisible<br />
 * 		void setLabel(String _label) - label of the button <br />
 * 
 * @author ankitdaf
 *
 */
public class KetaiButton extends Button {

	/** The parent. */
	private PApplet parent;

	/** The name. */
	String name = "KetaiButton";
	
	/** The button text */
	String label = "";
	
	/** The layout. */
	RelativeLayout layout;
	
	/** Button height. */
	int height=50;
	
	/** Button width. */
	int width=50;
	
	/** Manual Resize. */
	boolean resize=false;
	
	/** The implemented onClick method for the button	 */
	Method buttonMethod;

	/**
	 * 
	 * @param _parent the PApplet
	 * @param _name the button reference
	 * @param _label the button text
	 * @param _width button width
	 * @param _height button height
	 */
	public KetaiButton(PApplet _parent, final String _name,final String _label, int _width, int _height) {
		super(_parent.getApplicationContext());
		resize = true;
		width = _width;
		height = _height;
		setupButton(_parent, _name, _label);
	}

	/**
	 * 
	 * @param _parent the PApplet
	 * @param _name the button reference
	 * @param _label the button text
	 */
	public KetaiButton(PApplet _parent, final String _name,final String _label) {
		super(_parent.getApplicationContext());
		setupButton(_parent, _name, _label);
	}

	/**
	 * 
	 * Instantiates a new ketai button with supplied parameters.
	 * 
	 * @param _parent the PApplet
	 * @param _name the name reference
	 * @param _label the button text
	 */
	private void setupButton(PApplet _parent, final String _name,final String _label) {
		parent = _parent;
		name = _name;
		label = _label;
		findParentIntentions();
		init();
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
			buttonMethod = parent.getClass().getMethod(name+"Clicked",null);
		} catch (Exception e) {
			PApplet.println("The "+name+"Clicked method has not been implemented.Nothing to do here");
		}
	}
	
	/**
	 * Inits the button
	 */
	private void init() {
		layout = new RelativeLayout(parent);
		RelativeLayout.LayoutParams btnparams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		btnparams.setMargins(2,2,2,2);
		if(resize){
			btnparams.height = height;
			btnparams.width = width;
		}
		layout.addView(this,btnparams);
		parent.runOnUiThread(new Runnable() {
			
			public void run() {
				parent.addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
				setText(label);
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

	/**
	 * 
	 * Set the button text
	 * @param _label the button text
	 */
	public void setLabel(String _label) {
		label = _label;
		this.setText(label);
}
}
