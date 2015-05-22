package ro.pub.cs.systems.pdsd.practicaltest02var05;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

class Value{
	public String value;
	public String time;
}

public class CommunicationThread extends Thread{
	
	private static final String WEB_SERVICE_ADDRESS = "http://www.timeapi.org/utc/now";
	private ServerThread serverThread;
	private Socket       socket;
	
	

	public CommunicationThread(ServerThread serverThread, Socket socket) {
		this.serverThread = serverThread;
		this.socket       = socket;
		
	}
	
	@Override
	public void run() {
		if (socket != null) {
			try {
				BufferedReader bufferedReader = Utilities.getReader(socket);
				PrintWriter    printWriter    = Utilities.getWriter(socket);
				if (bufferedReader != null && printWriter != null) {
					String[] text = bufferedReader.readLine().split(",");
					String result = "aici";
					
					if(text[0].equals("put")){
						if(serverThread.tabel.containsKey(text[1])){
							
							
							HttpClient httpClient = new DefaultHttpClient();
							HttpGet httpGet = new HttpGet(WEB_SERVICE_ADDRESS);
							ResponseHandler<String> responseHandler = new BasicResponseHandler();
							String pageSourceCode = httpClient.execute(httpGet, responseHandler);
							
							String[] t = pageSourceCode.split("[-T:+]");
							
							DateTime d = new DateTime(Integer.parseInt(t[0]),Integer.parseInt(t[1]),
									Integer.parseInt(t[2]),Integer.parseInt(t[3]),Integer.parseInt(t[4]),Integer.parseInt(t[5]));
							
							String[] t2 = serverThread.tabel.get(text[1]).time.split("[-T:+]");
							
							DateTime d2= new DateTime(Integer.parseInt(t2[0]),Integer.parseInt(t2[1]),
									Integer.parseInt(t2[2]),Integer.parseInt(t2[3]),Integer.parseInt(t2[4]),Integer.parseInt(t2[5]));
							
							if(d.toLong() - d2.toLong() > 60){
								Value v = new Value();
								v.value = text[2];
								v.time = pageSourceCode;
								
								serverThread.tabel.put(text[1], v);
								result = "modified";
							}
							else{
								result = "";
							}
						}
						else{
							result = "inserted";
							
							HttpClient httpClient = new DefaultHttpClient();
							HttpGet httpGet = new HttpGet(WEB_SERVICE_ADDRESS);
							ResponseHandler<String> responseHandler = new BasicResponseHandler();
							String pageSourceCode = httpClient.execute(httpGet, responseHandler);
							
							Value v = new Value();
							v.value = text[2];
							v.time = pageSourceCode;
							
							serverThread.tabel.put(text[1], v);
						}
					}
					
					if(text[0].equals("get")){
						if(serverThread.tabel.containsKey(text[1])){
							result = serverThread.tabel.get(text[1]).value;
						}
						else{
							result = "none";
						}
					}
					
					printWriter.write(result + "\n");
					printWriter.flush();
				}
			} catch (Exception e) {
				Log.e("Exeption", e.toString());
			}
		}
	}
}
