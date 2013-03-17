package com.andyrobo.ui;

import java.lang.reflect.Method;

import processing.core.PApplet;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * 
 * Creates an Android text box, can be used as a label or editable text field. All methods below are in addition to the native Android methods
 * 
 * To process the buttonClick a sketch can define the following methods :<br /><br />
 * 
 * 		void namePressed()  - "name" is the name of the button supplied in the constructor<br />
 * 
 * To modify the button properties a sketch can call the following methods :<br /><br />
 * 
 * 		void setPosition(float x, float y) - x,y location for the button<br />
 * 		void setVisibility(boolean isVisible) - button visible or invisible<br />
 * 		void setText(String _text) - text of the button <br />
 * 		void setHint(String _hint) - hint to be shown <br />
 * 		void updateText(String title, String message) - title,message of the dialogue box for text input<br />
 * 
 * @author ankitdaf
 *
 */
public class KetaiText extends TextView{
	
	// TODO :
	// This is very generic and the implementation right now takes care of both
	// TextView and EditText (the second one rather crudely)
	// Find a different way to implement this ? 
	// Extending EditText did not work for me

	/** The parent. */
	private PApplet parent;

	/** The name. */
	String name = "KetaiButton";
	
	/** The button text */
	String text = "";
	
	/** Textbox hint */
	String hint = "";
	
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
	 * @param _text the button text
	 * @param _width button width
	 * @param _height button height
	 */
	public KetaiText(final PApplet _parent, final String _name,final String _text, int _width, int _height) {
		super(_parent);
		resize = true;
		width = _width;
		height = _height;
		setupButton(_parent, _name, _text);
	}

	/**
	 * 
	 * @param _parent the PApplet
	 * @param _name the button reference
	 * @param _text the button text
	 */	
	public KetaiText(PApplet _parent, final String _name,final String _text) {
		super(_parent.getApplicationContext());
		setupButton(_parent, _name, _text);
	}

	/**
	 * 
	 * Instantiates a new ketai button with supplied parameters.
	 * 
	 * @param _parent the PApplet
	 * @param _name the name reference
	 * @param _text the button text
	 */
	private void setupButton(PApplet _parent, final String _name,final String _text) {
		parent = _parent;
		name = _name;
		text = _text;
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
		this.setText(text);
	}
	
	/**
	 * Find parent intentions.
	 */
	private void findParentIntentions() {
		try {
			buttonMethod = parent.getClass().getMethod(name+"Clicked",null);
		} catch (Exception e) {
			PApplet.println("The "+name+"Click method has not been implemented.Nothing to do here");
			
		}
	}
	
	/**
	 * Inits the button
	 */
	private void init() {
		layout = new RelativeLayout(parent);
		RelativeLayout.LayoutParams btnparams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		btnparams.setMargins(2,2,2,2);
		//this.setHint(text);
		if(resize){
			btnparams.height = height;
			btnparams.width = width;
		}
		this.setLayoutParams(btnparams);
		layout.addView(this,btnparams);

		parent.runOnUiThread(new Runnable() {
			
			public void run() {
				parent.addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
				setTextColor(Color.BLACK);
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
		if (isVisible) super.setVisibility(VISIBLE);
		else super.setVisibility(INVISIBLE);
	}

	/**
	 * 
	 * Set the button text
	 * @param _text the button text
	 */
	public void setText(String _text) {
		super.setText(_text);
		text = _text;
	}
	
	/**
	 * 
	 * Sets the hint to show for input
	 * @param _hint hint to show
	 */
	public void setHint(String _hint) {
		super.setHint(_hint);
		hint = _hint;
	}

	/**
	 * 
	 * @param title Title for the dialogue box
	 * @param message Message for the dialogue box
	 */
	public void updateText(String title,String message) {
		
		AlertDialog.Builder alert = new AlertDialog.Builder(parent);

		alert.setTitle(title);
		alert.setMessage(message);

		// Set an EditText view to get user input 
		final EditText input = new EditText(parent);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  String value = input.getText().toString();
		  setText(value);
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		  }
		});

		alert.show();
}
	
}
