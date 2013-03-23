import com.andyrobo.wifi.*;

WifiServer wf;

void setup() {
  wf = new WifiServer(this,12000);
  wf.startListening();
}

void draw() {
}

void onPause() {
  super.onPause();
  wf.stopServer();
}
 
void mousePressed() {
  println("Sending");
  wf.sendData("Beam me up, Scottie");
}

void onWifiDataReceived(Object stringObj) {
  textSize(32);
  text((String) stringObj, 100,height/2);
}
