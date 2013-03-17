/**
 * Updated: 2013-03-17
 * Ankit Daftery
 * @ankitdaf
 */

import ketai.camera.*;
import ketai.net.*;
import android.graphics.Bitmap;
import com.andyrobo.ui.*;

KetaiText kt;
KetaiCamera cam;
KetaiButton ok;

boolean isSpyModeOn=false;

void setup() {
  orientation(PORTRAIT);
  imageMode(CORNER);
  cam = new KetaiCamera(this, 500, 500, 24);
  kt = new KetaiText(this,"ip",KetaiNet.getIP());
  kt.setPosition(width/4,600);
  ok = new KetaiButton(this,"ipb","SpyMode Off");
  ok.setPosition(width*3/4,600);
  cam.start();
}

void draw() {
  image(cam, width/4, 0);
}

void ipClicked() {
  println("IP clicked");
  kt.updateText("IP Address","Please Enter a New IP");
}

void ipbClicked() {
  isSpyModeOn = !isSpyModeOn;
  if(isSpyModeOn)ok.setText("SpyMode On");
  else ok.setText("SpyMode Off");
}

void onPause()
{
  super.onPause();
  //Make sure to releae the camera when we go
  //  to sleep otherwise it stays locked
  if (cam != null && cam.isStarted())
    cam.stop();
}

void onCameraPreviewEvent()
{
  cam.read();
  if(isSpyModeOn)
 {
   new ImageBroadCaster().execute(cam, kt.getText());
 }
}

void exit() {
  cam.stop();
}

// start/stop camera preview by tapping the screen
void mousePressed()
{
  if (cam.isStarted())
  {
    cam.stop();
  }
  else
    cam.start();
}
