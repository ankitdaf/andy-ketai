import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import android.os.AsyncTask;

class ImageBroadCaster extends AsyncTask {
  private static final int PREVIEW_PORT = 9020;

  public final static int HEADER_SIZE = 5;
  public final static int DATAGRAM_MAX_SIZE = 1450 - HEADER_SIZE;
  int frame_nb = 0;
  public InetAddress ipAddress;

  Bitmap mBitmap;
  int width_ima, height_ima;

  private DatagramSocket socket;

  public ImageBroadCaster() {
    try {
      this.socket = new DatagramSocket();
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private final void sendUDP(PImage image, String _ipAddress) {
    try {
    ipAddress = InetAddress.getByName(_ipAddress);
    }
    catch (UnknownHostException e)
    {
      println("Invalid host");
      e.printStackTrace();
    }
    mBitmap = (Bitmap) image.getNative();
    if (mBitmap != null) {
      int size_p = 0, i;
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

      // change compression rate to change packet size
      mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteStream);

      byte data[] = byteStream.toByteArray();

      int nb_packets = (int) Math.ceil(data.length
        / (float) DATAGRAM_MAX_SIZE);
      int size = DATAGRAM_MAX_SIZE;

      /* Loop through slices */
      for (i = 0; i < nb_packets; i++) {
        if (i > 0 && i == nb_packets - 1)
          size = data.length - i * DATAGRAM_MAX_SIZE;

        /* Set additional header */
        byte[] data2 = new byte[HEADER_SIZE + size];
        data2[0] = (byte) frame_nb;
        data2[1] = (byte) nb_packets;
        data2[2] = (byte) i;
        data2[3] = (byte) (size >> 8);
        data2[4] = (byte) size;

        /* Copy current slice to byte array */
        System.arraycopy(data, i * DATAGRAM_MAX_SIZE, data2, 
        HEADER_SIZE, size);

        try {
          size_p = data2.length;
          DatagramPacket packet = new DatagramPacket(data2, size_p, ipAddress, PREVIEW_PORT);
          socket.send(packet);
        } 
        catch (Exception e) {
          e.printStackTrace();
        }
      }
      frame_nb++;

      if (frame_nb == 128)
        frame_nb = 0;
    }
  }

@Override
protected Object doInBackground(Object... arg0) {
  sendUDP((PImage) arg0[0], (String) arg0[1]);
  return null;
}

}


