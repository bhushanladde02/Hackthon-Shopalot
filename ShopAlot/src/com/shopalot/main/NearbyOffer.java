package com.shopalot.main;

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
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class NearbyOffer  extends Activity{
	private String[] rank;
	private String[] country;
	private int[] color;
	public Map<String,Integer> hashMap = new HashMap<String, Integer>();
	private WifiManager mWifi;
	private List<ScanResult> wifiList;
	private String trainingID;
	private Context context;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager_main);
		context = this;
		// Generate sample data
				rank = new String[] { "1", "2", "3"};

				Intent intent = getIntent();
				String regionName = intent.getStringExtra("regionname");
				
				if(regionName==null)
					regionName="";
				
				
				int[] flag = getoffersbyRegion(regionName);
				
				color=new int[]{0xff69359c, 0xff96ca63, 0xfff5725b, 0xff9f00c5, 0xffff6600, 0xff0012bf, 0xff69359c, 0xff96ca63, 0xfff5725b, 0xff9f00c5};
				// Locate the ViewPager in viewpager_main.xml
				viewPager = (ViewPager) findViewById(R.id.pager);
				// Pass results to ViewPagerAdapter Class
				ViewPagerAdapter adapter = new ViewPagerAdapter(NearbyOffer.this, rank, flag, color);
				// Binds the Adapter to the ViewPager
				viewPager.setAdapter(adapter);
				
	//Start Async thread for  Caputring Wifi Signals and throwing nearby offers to user		
				//Get training ID from settings menu
				trainingID = "hack2";
				new GetNames().execute(trainingID);
	}
	
	public int[] getoffersbyRegion(String region){
		int flagoffers[] = new int[] {R.drawable.generaloffer,R.drawable.generaloffer1,R.drawable.generaloffer2,R.drawable.generaloffer3,R.drawable.generaloffer,R.drawable.generaloffer1,R.drawable.generaloffer2,R.drawable.generaloffer3};
		
		System.out.println("Region is from HomeActivity: "+region);
		
		int[] flagA = new int[] { R.drawable.regiona, R.drawable.regiona1,
				R.drawable.regiona2, R.drawable.regiona3,
				R.drawable.regiona, R.drawable.regiona1,
				R.drawable.regiona2, R.drawable.regiona3};
		
		int[] flagB = new int[] { R.drawable.regionb, R.drawable.regionb1,
				R.drawable.regionb2, R.drawable.regionb3,
				R.drawable.regionb, R.drawable.regionb1,
				R.drawable.regionb, R.drawable.regionb1};
				
		int[] flagC = new int[] { R.drawable.regionc, R.drawable.regionc1,
				R.drawable.regionc2, R.drawable.regionc3,
				R.drawable.regionc, R.drawable.regionc1,
				R.drawable.regionc, R.drawable.regionc1};
		
		//A  - shoploat
		if(region.equalsIgnoreCase("ShopALot")){
			flagoffers=flagA;
		//B -  Mitek
		}else if(region.equalsIgnoreCase("Mitek")){
			flagoffers=flagB;
		//C - Microsoft
		}else if(region.equalsIgnoreCase("Microsoft")){
			flagoffers=flagC;
		}
		return flagoffers;
	}
	
	class GetNames extends AsyncTask<String, Void, String[]>{

		private String[] wifiNames;
		@Override
		protected String[] doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String trainingID = params[0];
			
			HttpGet method = new HttpGet("http://onine.in:8080/ShopAlotBackend/servlet/GetWifiNames?trainingID=" + trainingID);
			HttpClient client = new DefaultHttpClient();
			try {
				HttpResponse res = client.execute(method);
				final StringBuilder string = inputStreamToString(res.getEntity().getContent());				
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
//		mText = (TextView) findViewById(R.id.textLabel);
		mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		StringBuilder _sb = new StringBuilder();
		
		
		registerReceiver(new BroadcastReceiver() {

			private int signalCounter;
			private String getResponse;

			public void onReceive(Context c, Intent i) {
				WifiManager w = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
				wifiList = w.getScanResults();
				int strengths[] = {0,0,0,0,0};
				HashMap<String,Integer> ssidIndexMap = new HashMap<String, Integer>();
				
				for (int m = 0; m < wifiList.size(); m++) {
					//modify this part
					ssidIndexMap.put(wifiList.get(m).BSSID, m);
				}

				
				Iterator<Entry<String, Integer>> iterator = hashMap.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, Integer> entry = iterator.next();
					if(ssidIndexMap.get(entry.getKey())!=null){
					int index=ssidIndexMap.get(entry.getKey());
					strengths[entry.getValue()]=wifiList.get(index).level;
					}
				}
				
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
						+ trainingID+"~~";
				
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
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//Intent intent =new Intent(context,ShopAlotTabActivity.class);
					//intent.putExtra("regionname", result.toString());
					//startActivity(intent);
					
					String region = result.toString();
					Log.i("Region deted based on signals ", region); 
					
					int[] flag = getoffersbyRegion(region);
					
					ViewPagerAdapter adapter = new ViewPagerAdapter(NearbyOffer.this, rank, flag, color);
					viewPager.setAdapter(adapter);
					
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
