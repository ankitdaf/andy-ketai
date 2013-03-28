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
 * void onWifiDataReceived(WifiServer wf) - the connection on which the data was received, call wf.getDataObject() to get the data object<br />
 * void sendData(Object obj) - the data object to be sent to connected client<br />
 * void getDataObject()<br />
 * 
 * @author ankitdaf
 *
 */
public class WifiServer {

	/** Network socket variables. */
    private String IP = "0.0.0.0";
    private int PORT = 9090 ;
    private ServerSocket serverSocket;
    private Thread serverThread = null;
    private Socket client;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    boolean isStreamInitialized=false;

    /**  Sketch specific variables. */
    private static PApplet parent;
    private boolean shouldStop=false;
    private Object dataObject;
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
				onWifiDataReceivedMethod.invoke(parent, new Object[] {this});
		} catch (Exception e) {
		}
	}

	/**
	 * 
	 * Get the IP Address of this device 
	 * 
	 * @return The string identifying the IP address
	 */
	public String getIP() {
		return IP;
	}
	
	/**
	 * 
	 * Set the ip address string identifying self. 
	 * Any functionality other than setting the variable not guaranteed
	 * Should consider removing it, purpose is unclear
	 *  
	 * @param iP self ip address
	 */
	public void setIP(String iP) {
		IP = iP;
	}
	
	/**
	 * 
	 * Returns the PORT number of current connection
	 * 
	 * @return PORT port number of current connection
	 */
	public int getPort() {
		return PORT;
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
			onWifiDataReceivedMethod=parent.getClass().getMethod("onWifiDataReceived", new Class[] {WifiServer.class});
		}
		catch (NoSuchMethodException e) {
			parent.println("onWifiDataReceived method not defined");
		}
	}
	
}
