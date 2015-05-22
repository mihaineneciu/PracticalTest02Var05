package ro.pub.cs.systems.pdsd.practicaltest02var05;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;

import android.util.Log;

public class ServerThread extends Thread {
	
	private static final String TAG = null;
	private int          port         = 0;
	private ServerSocket serverSocket = null;
	public HashMap<String, Value> tabel;
	
	public ServerThread(int port) {
		this.port = port;
		this.tabel = new HashMap<String,Value>();
		try {
			this.serverSocket = new ServerSocket(port);
		} catch (IOException ioException) {
			Log.e(TAG, "An exception has occurred: " + ioException.getMessage());
				ioException.printStackTrace();
		}
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setServerSocker(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	
	@Override
	public void run() {
		try {		
			while (!Thread.currentThread().isInterrupted()) {
				Log.i(TAG, "[SERVER] Waiting for a connection...");
				Socket socket = serverSocket.accept();
				Log.i(TAG, "[SERVER] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());
				CommunicationThread communicationThread = new CommunicationThread(this, socket);
				communicationThread.start();
			}			
		} catch (ClientProtocolException clientProtocolException) {
			Log.e(TAG, "An exception has occurred: " + clientProtocolException.getMessage());
				clientProtocolException.printStackTrace();
		} catch (IOException ioException) {
			Log.e(TAG, "An exception has occurred: " + ioException.getMessage());
				ioException.printStackTrace();
		}
	}
	
}
