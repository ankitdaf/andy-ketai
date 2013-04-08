/**
 * Updated: 2013-04-08
 * Ankit Daftery
 * @ankitdaf
 */

void showFace(String whichFace) {
  PImage img;
  img = loadImage(whichFace+".jpg");
  image(img,0,0,width,height);
}

void keepScreenOn()
{
  runOnUiThread(new Runnable() {
      
      @Override
      public void run() {
        getWindow().getDecorView().getRootView().setKeepScreenOn(true);
      }
    });
}

