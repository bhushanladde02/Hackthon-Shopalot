package com.onine.trilaterationbase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

public class MainActivity extends Activity {
	//main module
	TextView mText;
	WifiManager mWifi;
	List<ScanResult> wifiList;
	StringBuilder _sb = new StringBuilder();
	StringBuilder _wifiString = new StringBuilder();
	int counter = 0;
	String getResponse = "";
	EditText regionText;

	EditText[] wifiNames = new EditText[5];
	EditText trainingID;

	Button button;
	Button buttonWifi;
	Button getWifiNames;
	boolean chooseWifi;
	boolean startFlag;
	Map<String, Integer> hashMap = new HashMap<String, Integer>();
//	String[] wifiNamesList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		regionText = (EditText) this.findViewById(R.id.setArea);

		chooseWifi = true;

		wifiNames[0] = (EditText) findViewById(R.id.setWifi1);
		wifiNames[1] = (EditText) findViewById(R.id.setWifi2);
		wifiNames[2] = (EditText) findViewById(R.id.setWifi3);
		wifiNames[3] = (EditText) findViewById(R.id.setWifi4);
		wifiNames[4] = (EditText) findViewById(R.id.setWifi5);

		trainingID = (EditText) findViewById(R.id.setTrainingID);

		buttonWifi = (Button) findViewById(R.id.buttonwifi);

		button = (Button) findViewById(R.id.button);
		getWifiNames = (Button) findViewById(R.id.getwifiNames);

		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(startFlag==false)
				{
					button.setText("Stop");
					startFlag=true;
				}else
				{
					button.setText("Start");
					startFlag=false;
				}
				StartTester();
			}
		});

		buttonWifi.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (chooseWifi) {
					chooseWifi = false;
					buttonWifi.setText("Update Wifi");
					for (int i = 0; i < wifiNames.length; i++) {
						wifiNames[i].setFocusable(false);
						trainingID.setFocusable(false);
					}
				sendDataCloud(1);
				for (int i = 0; i < wifiNames.length; i++) {
					hashMap.put(wifiNames[i].getText().toString(), i);
				}
				
				
				} else {
					chooseWifi = true;
					buttonWifi.setText("Set Wifi");
					for (int i = 0; i < wifiNames.length; i++) {
						wifiNames[i].setFocusableInTouchMode(true);
						trainingID.setFocusableInTouchMode(true);
					}
				}
			}
		});
		
		getWifiNames.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String trainingName = trainingID.getText().toString(); 
				new GetRegionNameFromClassifier().execute(trainingName);
			}
		});

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

				int[] strenghts = { 0, 0, 0, 0, 0 };
				//wifiNamesList = new String[10];

				for (int m = 0; m < wifiList.size(); m++) {
					/*
					 * _sb.append((wifiList.get(m)).toString());
					 * _sb.append("\\n");
					 */
					// mText.setText(wifiList.size());
					if (chooseWifi) {
						_wifiString.append(wifiList.get(m).toString() + ",");
						//wifiNamesList[m] = wifiList.get(m).SSID;
						//System.out.println(wifiList.get(m).toString());
					}
					
					

						Integer index = hashMap.get(wifiList.get(m).BSSID);
						if (index!=null)
							strenghts[index] = wifiList.get(m).level;
					/*
					 * if(wifiList.get(m).SSID.contains("hello12"))
					 * firstStrength=wifiList.get(m).level; else
					 * if(wifiList.get(m).SSID.contains("3idiots"))
					 * secondStrength=wifiList.get(m).level; else
					 * if(wifiList.get(m).SSID.contains("BSNL_AP"))
					 * thirdStrength=wifiList.get(m).level;
					 */
				}

				if (chooseWifi) {
					/*String wifiArray[] = _wifiString.toString().split(",");
					Set<String> wifiList = new HashSet<String>();
					for (int j = 0, k = 0; j < wifiArray.length && k < 5; j = j + 7, k++) {
						// System.out.println(wifiArray[j].substring(6));
						if (!wifiList.contains(wifiArray[j].substring(6))) {
							wifiNames[k].setText(wifiArray[j].substring(6));
							wifiList.add(wifiArray[j].substring(6));
						}
					}*/
					Set<String> wifiSet = new HashSet<String>();
					for (int m = 0,k=0; m < wifiList.size() && k < 5; m++,k++) {
						if(!wifiSet.contains(wifiList.get(m).BSSID))
						{	
							wifiNames[k].setText(wifiList.get(m).BSSID);
							wifiSet.add(wifiList.get(m).BSSID);
						}	
					}
				}

				/*
				 * if(firstStrength>secondStrength)
				 * mText.setText("Hall - Pradeep"+_sb); else
				 * mText.setText("BedRoom - Bhushan"+_sb);
				 */

				for (int j = 0; j < strenghts.length; j++) {
					strenghts[j] = strenghts[j] * (-1);
				}
				/*
				 * firstStrength=(-1)*firstStrength;
				 * secondStrength=(-1)*secondStrength;
				 * thirdStrength=(-1)*thirdStrength;
				 */
				String text="";
				if(startFlag)
				{
				text = strenghts[0] + "," + strenghts[1] + ","
						+ strenghts[2] + "," + strenghts[3] + ","
						+ strenghts[4] + "," + regionText.getText() + "--"
						+ trainingID.getText() + "~~";

				// Better Logic for above codes could be if we calculate
				// difference between mean and actual signal and compare that
				// with standard deviations
				
				if (counter < 30) {
					getResponse += text;
					++counter;
				} else {
					// send http request
					getResponse += text;
				

					if (chooseWifi == false)
						sendDataCloud(0);

			
					getResponse = "";
					counter = 0;
				}

				mText.setText(text);
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

		// timer.cancel()

	}

	private void sendDataCloud(int flag) {
		if (flag == 1) {
			// set training
			String wifiNamesString = "";
			for (int i = 0; i < wifiNames.length; i++) {
				wifiNamesString += URLEncoder.encode(wifiNames[i].getText().toString())+",";
			}
			wifiNamesString+="&set=SETMODE&trainingID="+trainingID.getText();	
			new RetreiveFeedTask().execute(wifiNamesString); //this will go as array, check syntax of AsyncTask
		}
		else
		//passed to async thread
			new RetreiveFeedTask().execute(getResponse); //this will go as array, check syntax of AsyncTask
		

	}
	
	class GetRegionNameFromClassifier extends AsyncTask<String, Void, StringBuilder>{

		@Override
		protected StringBuilder doInBackground(String... inputs) {
			String input = inputs[0];
			
			StringBuilder str = new StringBuilder();
			// TODO Auto-generated method stub
			
			
			HttpGet method = new HttpGet("http://onine.in:8080/ShopAlotBackend/servlet/GetWifiNames?trainingID="+input);
			
			Log.i("URL Log", "http://onine.in:8080/ShopAlotBackend/servlet/GetWifiNames?trainingID="+input);
			
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
					String[] names = result.toString().split(",");
					for (int i = 0; i < names.length && i < 5; i++) {
						wifiNames[i].setText(names[i]);
					}
					//disable
					chooseWifi = false;
					buttonWifi.setText("Update Wifi");
					for (int i = 0; i < wifiNames.length; i++) {
						wifiNames[i].setFocusable(false);
						trainingID.setFocusable(false);
					}
					for (int i = 0; i < wifiNames.length; i++) {
						hashMap.put(wifiNames[i].getText().toString(), i);
					}
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
