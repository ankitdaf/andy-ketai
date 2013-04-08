/**
 * 
 * Code for the Android device mounted on Andyrobo
 *
 * Uses wifi for communication
 * 
 * Updated: 2013-04-08
 * Ankit Daftery
 * @ankitdaf
 */

import ketai.ui.*;
import android.view.MotionEvent;
import com.andyrobo.motor.*;
import com.andyrobo.audio.*;

import com.andyrobo.wifi.*;

WifiServer wf;
AndyMotorController c;
KetaiGesture gesture;

int count=0;

void setup() {
  orientation(PORTRAIT);
  gesture = new KetaiGesture(this);
  showFace("smile");
  //start listening for Wifi connections
  wf = new WifiServer(this,12012);
  wf.startListening();
  c = new AndyMotorController(this);
  c.begin();
  keepScreenOn();
}

void draw() {
}

void onWifiDataReceived(WifiServer wf) {
  int[] ws = (int[]) wf.getDataObject();
  wf.sendData((Object)"Got it, boss");
  println("L: "+ws[0]+" R: "+ws[1]);
  c.andyMove(ws[0],ws[1]);
  c.andyStopAfter(1000);
  if(count==0) showFace("confused");
  else if (count==1) showFace("laugh");
  else if (count==2) showFace("smile");
  else if (count==3) showFace("scared");
  else if (count==4) showFace("angry");
  count=count+1;
  if(count==5) count=0;
}
