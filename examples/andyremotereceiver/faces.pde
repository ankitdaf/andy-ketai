/**
 * Updated: 2013-04-02
 * Ankit Daftery
 * @ankitdaf
 */

void showFace(String whichFace) {
  PImage img;
  img = loadImage(whichFace+".jpg");
  image(img,0,0,width,height);
}

