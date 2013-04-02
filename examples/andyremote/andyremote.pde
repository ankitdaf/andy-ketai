 /**
 *
 * An Android remote to communicate with and control an Andy-robot
 *
 * Made with a lot of help from the Ketai BluetoothCursors example
 *
 * Updated: 2013-04-02
 * Ankit Daftery
 * @ankitdaf
 */

//required for BT enabling on startup
import android.content.Intent;
import android.os.Bundle;

import ketai.net.bluetooth.*;
import ketai.ui.*;
import ketai.net.*;
import android.view.MotionEvent;
import com.andyrobo.ui.*;
import com.andyrobo.motor.*;
import com.andyrobo.audio.*;

import oscP5.*;

KetaiBluetooth bt;
String info = "";
KetaiList klist;
PVector remoteMouse = new PVector();

AndyMotorController c;
KetaiGesture gesture;
KetaiButton interact;
KetaiButton discover,mkdiscover,paired,connect;
KetaiImageButton up,down,left,right;

ArrayList<String> devicesDiscovered = new ArrayList();
boolean isConfiguring = true;
String UIText;

//********************************************************************
// The following code is required to enable bluetooth at startup.
//********************************************************************
void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  bt = new KetaiBluetooth(this);
}

void onActivityResult(int requestCode, int resultCode, Intent data) {
  bt.onActivityResult(requestCode, resultCode, data);
}

//********************************************************************

void setup()
{   
  orientation(PORTRAIT);
  background(255);
  stroke(255);
  textSize(24);
  //start listening for BT connections
  bt.start();
  
  gesture = new KetaiGesture(this);
  discover = new KetaiButton(this,"discover","Discover");
  discover.setPosition(5,30);
  mkdiscover = new KetaiButton(this,"mkdiscover","Make Discoverable");
  mkdiscover.setPosition(105,30);
  connect = new KetaiButton(this,"connect","Connect");
  connect.setPosition(505,30);
  interact = new KetaiButton(this,"interact","Interact");
  interact.setPosition(605,30);
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


void draw()
{
}

void onFlick(float x,float y,float px,float py,float v) {
  getFlingDirection(x-px,py-y);
}

void discoverClicked() {
  bt.discoverDevices();
}

void mkdiscoverClicked() {
  bt.makeDiscoverable();
}

void connectClicked() {
      //If we have not discovered any devices, try prior paired devices
    if (bt.getDiscoveredDeviceNames().size() > 0)
      klist = new KetaiList(this, bt.getDiscoveredDeviceNames());
    else if (bt.getPairedDeviceNames().size() > 0)
      klist = new KetaiList(this, bt.getPairedDeviceNames());

}

void upClicked() {
  int[] ws = c.getWheelSpeeds(255,0);
  OscMessage m = new OscMessage("/remoteMouse/");
  m.add(ws[0]);
  m.add(ws[1]);
  bt.broadcast(m.getBytes());
}

void downClicked() {
  int[] ws = c.getWheelSpeeds(255,-180);
  OscMessage m = new OscMessage("/remoteMouse/");
  m.add(ws[0]);
  m.add(ws[1]);
  bt.broadcast(m.getBytes());

}

void leftClicked() {
  int[] ws = c.getWheelSpeeds(255,90);
  OscMessage m = new OscMessage("/remoteMouse/");
  m.add(ws[0]);
  m.add(ws[1]);
  bt.broadcast(m.getBytes());
}

void rightClicked() {
  int[] ws = c.getWheelSpeeds(255,-90);
  OscMessage m = new OscMessage("/remoteMouse/");
  m.add(ws[0]);
  m.add(ws[1]);
  bt.broadcast(m.getBytes());

}
