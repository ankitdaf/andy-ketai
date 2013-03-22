package com.andyrobo.wifi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import processing.core.PApplet;

import ketai.net.KetaiNet;

/**
 * 
 * The Class WifiServer allows you to send/receive data over WiFi sockets
 * The implementation is generic, which means all data types and objects can be exchanged
 * 
 * The following methods can be implemented in a sketch to access data: <br /><br />
 * 
 * void onWifiDataReceived(Object obj) - the data object received, needs to be type cast appropriately<br />
 * void sendData(Object obj) - the data object to be sent to connected client<br />
 * 
 * @author ankitdaf
 *
 */
public class WifiServer {

	/** Network socket variables. */
    private static String IP = "0.0.0.0";
	public static int PORT = 9090 ;
    private ServerSocket serverSocket;
    private static Thread serverThread = null;
    private Socket client;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    boolean isStreamInitialized=false;

    /**  Sketch specific variables. */
    private static PApplet parent;
    private static boolean shouldStop=false;
    private static Object dataObject;
    private Method onWifiDataReceivedMethod;

    /**
     * 
     * Instantiates a new Wifi Server connection
     * 
     * @param _parent the calling sketch/Activity
     * @param port the port number for the connection
     */
	public WifiServer(final PApplet _parent,int port) {
		PORT = port;
		parent = _parent;
		setIP(KetaiNet.getIP());
		findParentIntentions();
		serverThread = new Thread(new ServerThread());
	}
	
	/**
	 * 
	 * Instantiates a new Wifi Server connection
	 * @param _parent the calling sketch/Activity
	 */
	public WifiServer(PApplet _parent) {
		parent = _parent;
		setIP(KetaiNet.getIP());
		findParentIntentions();
		serverThread = new Thread(new ServerThread());	
	}
	
	/**
	 * 
	 * Threading class to handle socket connections
	 * 
	 * @author ankitdaf
	 *
	 */
    public class ServerThread implements Runnable {
        public void run() {
            try {
                if (IP != null) {
                    serverSocket = new ServerSocket();
                    serverSocket.setReuseAddress(true);
                    serverSocket.bind(new InetSocketAddress(PORT));
                    while (!shouldStop) {
                        // listen for incoming clients
                    	parent.println("Waiting at IP "+IP );
                        client = serverSocket.accept();
                        parent.println("Accepted from" + client.getInetAddress());
                        try {
                        	if(!isStreamInitialized){
                        	ois = new ObjectInputStream(client.getInputStream());
                        	oos = new ObjectOutputStream(client.getOutputStream());
                            isStreamInitialized=true;
                        	}
                        	handleWirelessData(ois.readObject());
                        }
                        catch (Exception e) {
                        	e.printStackTrace();
                        }
                        if(client.isClosed()) isStreamInitialized=false;
                        }
                } else {
                	parent.println("No Wifi");
                }
            } catch (Exception e) {
            	parent.println("Wifi error");
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 
     * Start a Wifi Server listening for data
     * 
     */
	public void startListening() {
		serverThread.start();
	}
	
	/**
	 * 
	 * Stop the Wifi Server
	 * 
	 */
	public void stopServer() {
		try {
			shouldStop=true;
			serverSocket.close();
			parent.println("Stopped server");
		}
		catch (IOException e) {
			parent.println("Error closing socket");
			e.printStackTrace();
		}	
	}
	
	/**
	 * 
	 * Send data to the connected client
	 * 
	 * @param obj Data object to be sent
	 */
	public void sendData(final Object obj) {
		new Runnable() {
			
			public void run() {
				try {
					if(client.isConnected() && !client.isClosed()) {
					oos.writeObject(obj);
					}
					else {
						parent.println("Client unavailable");
						isStreamInitialized=false;
					}
					
				} catch (IOException e) {
					parent.println("Could not send data");
					e.printStackTrace();
				}

			}
		}.run();
	}
	
	/**
	 * 
	 * Invoke the necessary methods to manipulate the received data
	 * 
	 * @param obj The data object received, typecast it correctly before use 
	 */
	public void handleWirelessData(Object obj)
	{
		dataObject = obj;
		try {
			if(onWifiDataReceivedMethod != null)
			onWifiDataReceivedMethod.invoke(parent, new Object[] { dataObject});
		} catch (Exception e) {
		}
	}

	/**
	 * 
	 * Get the IP Address of this device 
	 * 
	 * @return The string identifying the IP address
	 */
	public static String getIP() {
		return IP;
	}
	
	/**
	 * 
	 * Set the ip address string identifying self. 
	 * Any functionality other than setting the variable not guaranteed
	 *  
	 * @param iP self ip address
	 */
	public static void setIP(String iP) {
		IP = iP;
	}
	
	/**
	 * 
	 *  Find Parent Intentions
	 *  
	 */
	private void findParentIntentions(){
		try {
			onWifiDataReceivedMethod=parent.getClass().getMethod("onWifiDataReceived", new Class[] {Object.class});
		}
		catch (NoSuchMethodException e) {
			parent.println("onWifiDataReceived method not defined");
		}
	}
	
}
