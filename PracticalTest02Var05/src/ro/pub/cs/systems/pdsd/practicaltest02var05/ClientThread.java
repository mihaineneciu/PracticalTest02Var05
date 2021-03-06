package ro.pub.cs.systems.pdsd.practicaltest02var05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import android.util.Log;
import android.widget.TextView;

public class ClientThread extends Thread {
	
	private static final String TAG = null;
	private String   address;
	private int      port;
	private String   text;
	private TextView weatherForecastTextView;
	
	private Socket   socket;
	
	public ClientThread(
			String address,
			int port,
			String text,
			TextView weatherForecastTextView) {
		this.address  = address;
		this.port = port;
		this.text = text;
		this.weatherForecastTextView = weatherForecastTextView;
	}
	
	@Override
	public void run() {
		try {
			socket = new Socket(address, port);
			if (socket == null) {
				Log.e(TAG, "[CLIENT THREAD] Could not create socket!");
			}
			
			BufferedReader bufferedReader = Utilities.getReader(socket);
			PrintWriter    printWriter    = Utilities.getWriter(socket);
			if (bufferedReader != null && printWriter != null) {
				printWriter.println(text);
				printWriter.flush();
				
				String result;
				while ((result = bufferedReader.readLine()) != null) {
					final String finalizedWeatherInformation = result;
					weatherForecastTextView.post(new Runnable() {
						@Override
						public void run() {
							weatherForecastTextView.setText(finalizedWeatherInformation + "\n");
						}
					});
				}
			} else {
				Log.e(TAG, "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
			}
			socket.close();
		} catch (IOException ioException) {
			Log.e(TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
				ioException.printStackTrace();
		}
	}
}
