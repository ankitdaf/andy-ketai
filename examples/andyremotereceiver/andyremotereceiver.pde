/**
 * 
 * Code for the Android device mounted on Andyrobo
 *
 * Builds on the Bluetooth OSCP5 example in Ketai for the Bluetooth communication
 * 
 * Updated: 2013-04-02
 * Ankit Daftery
 * @ankitdaf
 */

import android.view.MotionEvent;
import com.andyrobo.motor.*;
import com.andyrobo.audio.*;

//required for BT enabling on startup
import android.content.Intent;
import android.os.Bundle;

import ketai.net.bluetooth.*;
import ketai.ui.*;
import ketai.net.*;

import oscP5.*;

KetaiBluetooth bt;


AndyMotorController c;
KetaiGesture gesture;

int count=0;

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

void setup() {
  orientation(PORTRAIT);
  gesture = new KetaiGesture(this);
  showFace("smile");
  //start listening for BT connections
  bt.start();
  bt.makeDiscoverable();
  c = new AndyMotorController(this);
  c.begin();
}

void draw() {
}


//Call back method to manage data received
void onBluetoothDataEvent(String who, byte[] data)
{
  //KetaiOSCMessage is the same as OscMessage
  //   but allows construction by byte array
  KetaiOSCMessage m = new KetaiOSCMessage(data);
  if (m.isValid())
  {
    if (m.checkAddrPattern("/remoteMouse/"))
    {
      if (m.checkTypetag("ii"))
      {
        int l = m.get(0).intValue();
        int r = m.get(1).intValue();
        c.andyMove(l,r);
        c.andyStopAfter(1000);
        if(count==0) showFace("confused");
  else if (count==1) showFace("laugh");
  else if (count==2) showFace("smile");
  else if (count==3) showFace("scared");
  else if (count==4) showFace("angry");
  count=count+1;
  if(count==5) count=0;
      }
    }
  }
}

