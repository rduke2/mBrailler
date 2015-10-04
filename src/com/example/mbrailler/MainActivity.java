package com.example.mbrailler;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import at.abraxas.amarino.Amarino;
import at.abraxas.amarino.AmarinoIntent;

public class MainActivity extends Activity {

	private static final String TAG = "mBrailler";
	private static final String DEVICE_ADDRESS= "00:06:66:4E:9B:F0";
	private BluetoothReceiver mBTReceiver = new BluetoothReceiver();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		// register broadcast receiver
		registerReceiver(mBTReceiver, new IntentFilter(AmarinoIntent.ACTION_RECEIVED));
		registerReceiver(mBTReceiver, new IntentFilter(AmarinoIntent.ACTION_CONNECTED));
				
		// connects to BT module
		Amarino.connect(getApplicationContext(), DEVICE_ADDRESS);
	}
	
	@Override 
	protected void onStop(){
		super.onStop();
		
		unregisterReceiver(mBTReceiver);
		
		Amarino.disconnect(getApplicationContext(), DEVICE_ADDRESS);
	}
	
	private class BluetoothReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent) {
			
			String action = intent.getAction();
			
			if(action.equalsIgnoreCase(AmarinoIntent.ACTION_CONNECTED))
			{
				Log.v(TAG, "connected");
			}
			else if(action.equalsIgnoreCase(AmarinoIntent.ACTION_RECEIVED))
			{
				final int dataType = intent.getIntExtra(AmarinoIntent.EXTRA_DATA_TYPE, -1);
				
				if(dataType == AmarinoIntent.STRING_EXTRA){
					String data = intent.getStringExtra(AmarinoIntent.EXTRA_DATA);
					
					if(data != null){
						// TODO whatever you want
						Log.v(TAG, "data received: " + data);
						//Toast.makeText(getApplicationContext(), "Received character: " + data, Toast.LENGTH_LONG).show();
					}
				}
			}
			
		}		
	}
	
	/*
	 * Creates menu
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
