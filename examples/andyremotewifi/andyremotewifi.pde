 /**
 *
 * An Android remote to communicate with and control an Andy-robot
 *
 * Made with a lot of help from the Ketai BluetoothCursors example
 *
 * Updated: 2013-04-08
 * Ankit Daftery
 * @ankitdaf
 */

import ketai.ui.*;
import android.view.MotionEvent;
import com.andyrobo.ui.*;
import com.andyrobo.motor.*;
import com.andyrobo.audio.*;

import com.andyrobo.wifi.*;

AndyMotorController c;
KetaiGesture gesture;
KetaiButton conn;
KetaiImageButton up,down,left,right;
WifiClient wc;

ArrayList<String> devicesDiscovered = new ArrayList();
boolean isConfiguring = true;
String UIText;


void setup()
{   
  orientation(PORTRAIT);
  background(255);
  stroke(255);
  textSize(24);
  //start listening for Wifi connections  
  wc = new WifiClient(this,"192.168.2.10",12012);
  gesture = new KetaiGesture(this);
  conn = new KetaiButton(this,"conn","Connect");
  conn.setPosition(505,30);
  imageMode(CENTER);
  up = new KetaiImageButton(this,"up.jpg","up");
  up.setPosition(width/2,height/8*3);
  down = new KetaiImageButton(this,"down.jpg","down");
  down.setPosition(width/2,height/8*5);
  left = new KetaiImageButton(this,"left.jpg","left");
  left.setPosition(width/8*2,height/8*4);
  right = new KetaiImageButton(this,"right.jpg","right");
  right.setPosition(width/8*6,height/8*4);
  c = new AndyMotorController(this);
  
}

public boolean surfaceTouchEvent(MotionEvent event) {

  //call to keep mouseX, mouseY, etc updated
  super.surfaceTouchEvent(event);
  //forward event to class for processing
  return gesture.surfaceTouchEvent(event);
}


void draw(){
}

void onFlick(float x,float y,float px,float py,float v) {
  getFlingDirection(x-px,py-y);
}


void connClicked() {
      wc.connect();
}

void upClicked() {
  int[] ws = c.getWheelSpeeds(255,0);
  wc.sendData((Object) ws);
}

void downClicked() {
  int[] ws = c.getWheelSpeeds(255,-180);
  wc.sendData((Object) ws);
}

void leftClicked() {
  int[] ws = c.getWheelSpeeds(255,90);
  wc.sendData((Object) ws);
}

void rightClicked() {
  int[] ws = c.getWheelSpeeds(255,-90);
  wc.sendData((Object) ws);
}

void onWifiDataReceived(WifiClient wcl) {
  println((String) wcl.getDataObject());
}
