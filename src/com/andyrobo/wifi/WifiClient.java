package com.andyrobo.wifi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import processing.core.PApplet;

import android.os.AsyncTask;

/**
 * 
 * The Class WifiServer allows you to send/receive data over WiFi sockets
 * The implementation is generic, which means all data types and objects can be exchanged
 * 
 * The following methods can be implemented in a sketch to access data: <br /><br />
 * 
 * void connect()<br />
 * void onWifiDataReceived(WifiServer wf) - the WifiServer on which the data was received, call wf.getDataObject() to get the data object<br />
 * void sendData(Object obj) - the data object to be sent to connected client<br />
 * void getDataObject()<br />
 * 
 * @author ankitdaf
 *
 */
public class WifiClient{
	
	/**  Connection specific parameters  **/
	private Socket socket ;
	private InetAddress connectTo;
	private int PORT;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Object dataObject;
	
	/**  Sketch specific parameters  **/
	private Method onWifiDataReceivedMethod;
	private static PApplet parent;

	/**
	 * 
	 * Instantiate the client and set the connection parameters
	 * 
	 * @param _parent the calling sketch/Activity
	 * @param ipAddress The IP address to connect to
	 * @param port Port number of remote IP on which the connection is to be made
	 */
	public WifiClient(final PApplet _parent,String ipAddress, int port) {
		try {
			connectTo = InetAddress.getByName(ipAddress);
			PORT = port;
			parent = _parent;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * Establish the connection and setup the callback
	 * 
	 */
	public void connect() {
		new wifiConnector().execute();
		findParentIntentions();
	}
	
	/**
	 * 
	 * A private class to handle sending data over the Wifi socket
	 * Network operation is best done in the background
	 * 
	 * @author ankitdaf
	 *
	 */
	private class wifiSender extends AsyncTask {

		@Override
		protected Object doInBackground(Object... arg0) {
			try {
				oos.writeObject(arg0[0]);
				oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		
	}
	public void sendData(Object data) {
		new wifiSender().execute(data);

	}

	/**
	 * 
	 * A background thread to listen for incoming data
	 * 
	 * @author ankitdaf
	 *
	 */
	private class wifiListener implements Runnable {

		public void run() {
			while(true){
			try{
				dataObject=ois.readObject();
				handleWirelessData(dataObject);
			} catch (OptionalDataException e) {
				e.printStackTrace();
				break;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
		}
		
	}
	
	/**
	 * 
	 * A private class to handle the connection operation
	 * Network operation is best done in the background
	 * 
	 * @author ankitdaf
	 *
	 */
	private class wifiConnector extends AsyncTask {
	
		@Override
		protected Object doInBackground(Object... arg0) {
		try {
			socket = new Socket(connectTo, PORT);
			System.out.println("Connected to "+connectTo+" on PORT "+PORT);

			/**
			 * you should always make it a practice in your code to open the ObjectOutputStream and flush it first, before you open the ObjectInputStream.
			 * The ObjectOutputStream constructor will not block, and invoking flush() will force the magic number and version number to travel over the wire.
			 * (http://www.jguru.com/faq/view.jsp?EID=333392)
			 */
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());
			new wifiListener().run();
			return true;
		}
		 catch (UnknownHostException e) {
			System.out.println("Invalid IP Address");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Cannot connect");
			e.printStackTrace();
		}
		return false;
	}
	}
	
	/**
	 * 
	 * Invoke the necessary methods to manipulate the received data
	 * 
	 * @param obj The data object received, typecast it correctly before use 
	 */
	private void handleWirelessData(Object obj)
	{
		try {
			if(onWifiDataReceivedMethod != null)
				onWifiDataReceivedMethod.invoke(parent, new Object[] {this});
		} catch (Exception e) {
			System.out.println("Could not process incoming data");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * Returns the dataObject associated with this connection
	 * 
	 * @return dataObject received dataObject
	 */
	public Object getDataObject() {
		return dataObject;
	}
	
	/**
	 * 
	 *  Find Parent Intentions
	 *  
	 */
	private void findParentIntentions(){
		try {
			onWifiDataReceivedMethod=parent.getClass().getMethod("onWifiDataReceived", new Class[] {WifiClient.class});
		}
		catch (NoSuchMethodException e) {
			System.out.println("onWifiDataReceived method not defined");
		}
	}

}
