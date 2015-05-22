package ro.pub.cs.systems.pdsd.practicaltest02var05;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02var05MainActivity extends Activity {

	public int PORT = 12351;
	private Button queryBtn;
	private EditText keyText, valueText, methodText;
	private TextView textView;
	private ServerThread serverThread;
	private EditText     serverPortEditText       = null;
	private Button       connectButton            = null;
	
	private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
	private class ConnectButtonClickListener implements Button.OnClickListener {
		
		@Override
		public void onClick(View view) {
			String serverPort = serverPortEditText.getText().toString();
			if (serverPort == null || serverPort.isEmpty()) {
				Toast.makeText(
					getApplicationContext(),
					"Server port should be filled!",
					Toast.LENGTH_SHORT
				).show();
				return;
			}
			PORT = Integer.parseInt(serverPort);
			serverThread = new ServerThread(Integer.parseInt(serverPort));
			if (serverThread.getServerSocket() != null) {
				serverThread.start();
			}
			
		}
	}
	
	private class ButtonListener implements OnClickListener {

		private ClientThread clientThread;

		@Override
		public void onClick(View v) {
			String text = methodText.getText().toString() + ',' + keyText.getText().toString() + ',' + valueText.getText().toString();
			clientThread = new ClientThread(
					"localhost",
					PORT,
					text,
					textView);
			clientThread.start();
		}
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practical_test02_var05_main);
		
		queryBtn = (Button)findViewById(R.id.button);
		keyText = (EditText)findViewById(R.id.key);
		valueText = (EditText)findViewById(R.id.value);
		methodText = (EditText)findViewById(R.id.method);
		textView = (TextView)findViewById(R.id.textView);
		serverPortEditText = (EditText)findViewById(R.id.port);
		connectButton = (Button)findViewById(R.id.connect);
		connectButton.setOnClickListener(connectButtonClickListener);
		
		queryBtn.setOnClickListener(new ButtonListener());
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
