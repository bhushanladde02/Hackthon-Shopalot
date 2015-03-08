package com.example.testertri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TesterActivity extends Activity {


	TextView mText;
	EditText trainingIDEdit;
	EditText wifisConsidered;
	Button startTestingButton;
	WifiManager mWifi;	
	List<ScanResult> wifiList;
	StringBuilder _sb = new StringBuilder();
	int counter=0;
	int signalCounter=0;
	String getResponse="";
	String[] wifiNames;
	Map<String, Integer> hashMap = new HashMap<String, Integer>();


	Button button;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.activity_tester);
		trainingIDEdit=(EditText) findViewById(R.id.trainingID);
		startTestingButton=(Button) findViewById(R.id.startTesting);
		wifisConsidered= (EditText) findViewById(R.id.wifisConsidered);
		
		
		startTestingButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new GetNames().execute(); //async call
			}
		});
		
	}
	

	
	class GetNames extends AsyncTask<String, Void, String[]>{

		@Override
		protected String[] doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpGet method = new HttpGet("http://onine.in:8080/ShopAlotBackend/servlet/GetWifiNames?trainingID=" + trainingIDEdit.getText());
			HttpClient client = new DefaultHttpClient();
			try {
				HttpResponse res = client.execute(method);
				final StringBuilder string = inputStreamToString(res.getEntity().getContent());
				
				runOnUiThread(new Runnable() {
				     public void run() {
				    	 wifisConsidered.setText(string.toString());
				    }
				});
				
				
				
				return string.toString().split(",");
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(String[] result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			wifiNames = result;
			for (int i = 0; i < wifiNames.length && i < 5; i++) {
				hashMap.put(wifiNames[i], i);
			}
			StartTester();
		}
		
	}



	public void StartTester() {
		IntentFilter i = new IntentFilter();
		i.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		mText = (TextView) findViewById(R.id.textLabel);
		mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		_sb = new StringBuilder();
		
		
		registerReceiver(new BroadcastReceiver() {
			public void onReceive(Context c, Intent i) {
				WifiManager w = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
				wifiList = w.getScanResults();
				int strengths[] = {0,0,0,0,0};
				HashMap<String,Integer> ssidIndexMap = new HashMap<String, Integer>();
				
				for (int m = 0; m < wifiList.size(); m++) {
					/*_sb.append((wifiList.get(m)).toString());
					_sb.append("\\n");*/
					
					_sb.append(counter++ +"-");
					
					//modify this part
					ssidIndexMap.put(wifiList.get(m).BSSID, m);
 
					
				}
				
				/*Integer index= hashMap.get(wifiList.get(m).SSID);
				if(index!=null)
					strengths[index]=wifiList.get(m).level;*/
				
				Iterator<Entry<String, Integer>> iterator = hashMap.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, Integer> entry = iterator.next();
					if(ssidIndexMap.get(entry.getKey())!=null){
					int index=ssidIndexMap.get(entry.getKey());
					strengths[entry.getValue()]=wifiList.get(index).level;
					}
				}
				
				/*if(firstStrength>secondStrength)
				mText.setText("Hall - Pradeep"+_sb);
				else
				mText.setText("BedRoom - Bhushan"+_sb);*/
				
				for (int j = 0; j < strengths.length; j++) {
					strengths[j] = strengths[j] * (-1);
				}
				/*
				 * firstStrength=(-1)*firstStrength;
				 * secondStrength=(-1)*secondStrength;
				 * thirdStrength=(-1)*thirdStrength;
				 */
 
				String text = strengths[0] + "," + strengths[1] + ","
						+ strengths[2] + "," + strengths[3] + ","
						+ strengths[4] + ","
						+ trainingIDEdit.getText()+"~~";;
				
				if (signalCounter < 4) { //sending 5 scans for making firm decision on server end
					getResponse+=text;
					++signalCounter;
				}
				else{
					//seperate thread
					new GetRegionNameFromClassifier().execute(getResponse);
					getResponse="";
					signalCounter = 0;
				}
			}
		}, i);

		final WifiManager WM = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WM.setWifiEnabled(true);
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				WM.startScan();
			}
		}, 0, 2000);
		
		
		
 		
	}


	class GetRegionNameFromClassifier extends AsyncTask<String, Void, StringBuilder>{

		@Override
		protected StringBuilder doInBackground(String... inputs) {
			String input = inputs[0];
			
			StringBuilder str = new StringBuilder();
			// TODO Auto-generated method stub
			
			
			HttpGet method = new HttpGet("http://onine.in:8080/ShopAlotBackend/servlet/ServletMLResponse?signals="+input);
			
			Log.i("URL Log", "http://onine.in:8080/ShopAlotBackend/servlet/ServletMLResponse?signals="+input);
			
	        HttpClient client = new DefaultHttpClient();
	        try {
	            HttpResponse httpResponse =  client.execute(method);
		        return inputStreamToString(httpResponse.getEntity().getContent());  
	        } catch (ClientProtocolException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
			return str.append("Good Job");
		}
		
		@Override
		protected void onPostExecute(final StringBuilder result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			runOnUiThread(new Runnable() {
				public void run() {
					mText.setText(mText.getText()+" -- "+ result.toString());
				}
			});
			
		}
		
	}
	

	
	// Fast Implementation
	private StringBuilder inputStreamToString(InputStream is) {
	    String line = "";
	    StringBuilder total = new StringBuilder();
	    
	    // Wrap a BufferedReader around the InputStream
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));

	    // Read response until the end
	    try {
			while ((line = rd.readLine()) != null) { 
			    total.append(line); 
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    // Return full string
	    return total;
	}

    
}
