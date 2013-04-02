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

void getFlingDirection(float velocityX,float velocityY) {
    float velX = normalizeVelocity(velocityX, displayWidth);
    float velY = normalizeVelocity(velocityY, displayHeight);

    double a = Math.atan2(velX, velY);
    a = snapAngle(Math.toDegrees(a));

    final double angle = Math.toRadians(a);
    int[] ws = c.getWheelSpeeds(255,angle);
    c.andyMove(ws[0],ws[1]);
    c.andyStopAfter(1000); 
}

float normalizeVelocity(float velocity,int maxValue) {
    return (float) (velocity / maxValue);
}

double snapAngle(double d) {
    if (d < -155) {
      return -180;
    }

    if (d < -115) {
      return -135;
    }

    if (d < -70) {
      return -90;
    }

    if (d < -20) {
      return -45;
    }

    if (d > 155) {
      return 180;
    }

    if (d > 115) {
      return 135;
    }

    if (d > 70) {
      return 90;
    }

    if (d > 20) {
      return 45;
    }

    return 0;
  }
  


