/**
 * Updated: 2013-03-17
 * Ankit Daftery
 * @ankitdaf
 */

import ketai.ui.*;
import android.view.MotionEvent;
import com.andyrobo.*;
import com.andyrobo.motor.*;
import com.andyrobo.audio.*;

AndyMotorController c;
AndyGesture gesture;

int count=0;
void setup() {
  orientation(PORTRAIT);
  gesture = new AndyGesture(this);
  showFace("smile");
  c = new AndyMotorController(this);
  c.begin();
}

void draw() {
}


public boolean surfaceTouchEvent(MotionEvent event) {

  //call to keep mouseX, mouseY, etc updated
  super.surfaceTouchEvent(event);
  //forward event to class for processing
  return gesture.surfaceTouchEvent(event);
}

void onFling(float x,float y,float px,float py,float vx,float vy) {
  getFlingDirection(vx,vy);
  if(count==0) showFace("confused");
  else if (count==1) showFace("laugh");
  else if (count==2) showFace("smile");
  else if (count==3) showFace("scared");
  else if (count==4) showFace("angry");
  count=count+1;
  if(count==5) count=0;
}

void onPause() {
  c.finish();
  super.onPause();
}