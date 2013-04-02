/**
 * Updated: 2013-04-02
 * Ankit Daftery
 * @ankitdaf
 */

void getFlingDirection(float velocityX,float velocityY) {
    float velX = normalizeVelocity(velocityX, displayWidth);
    float velY = normalizeVelocity(velocityY, displayHeight);

    double a = -1*Math.atan2(velX, velY);
    a = snapAngle(Math.toDegrees(a));
    final double angle = Math.toRadians(a);
    int[] ws = c.getWheelSpeeds(255,angle);
      //send data to everyone
  //  we could send to a specific device through
  //   the writeToDevice(String _devName, byte[] data)
  //  method.
  OscMessage m = new OscMessage("/remoteMouse/");
  m.add(ws[0]);
  m.add(ws[1]);
  bt.broadcast(m.getBytes());
}

float normalizeVelocity(float velocity,int maxValue) {
    return  (float) velocity / maxValue;
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
  
void onKetaiListSelection(KetaiList klist)
{
  String selection = klist.getSelection();
  bt.connectToDeviceByName(selection);

  //dispose of list for now
  klist = null;
}
