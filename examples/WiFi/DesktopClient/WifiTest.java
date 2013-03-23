import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;


public class WifiTest{

	public static String ipAddress = "192.168.2.2";
	static Socket kkSocket ;
	static int i=0;

	public static void main(String[] args) {
		try {
			kkSocket = new Socket(ipAddress, 12000);
			System.out.println("Connected");
			final ObjectOutputStream oos = new ObjectOutputStream(kkSocket.getOutputStream());
			final ObjectInputStream ois = new ObjectInputStream(kkSocket.getInputStream());

			new Runnable() {

				@Override
				public void run() {

					System.out.println("before while");

					try {
						while(true) {
							try{
								System.out.println((String) ois.readObject());
							} catch (EOFException e) {
								System.out.println("Done");
							} catch (SocketException e) {
								System.out.println("Socket closed");
							}
							i++;
							System.out.println(i);
							if(i==3) {
								new Runnable() {

									@Override
									public void run() {
										try {
											oos.writeObject((Object)"Count reached");
											i=0;
										} catch (IOException e) {
											e.printStackTrace();
										}

									}
								}.run();
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();	
					}


				}
			}.run();

		} catch (UnknownHostException e) {
			System.out.println("Unknown host" + ipAddress); 
		} catch (IOException e) {
			System.out.println("Couldn't get I/O for the connection to: " + ipAddress);
			e.printStackTrace();
		}
	}
}
