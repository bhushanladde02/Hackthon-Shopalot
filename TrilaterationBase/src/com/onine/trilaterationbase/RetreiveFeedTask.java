package com.onine.trilaterationbase;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;
//async module
class RetreiveFeedTask extends AsyncTask<String, Void, Void> {

	@Override
	protected Void doInBackground(String... strings) {
		// TODO Auto-generated method stub
			String getResponse = strings[0]; 			
			// TODO Auto-generated method stub
			System.out.println("Responce is: "+getResponse);
			Log.i("Log the RetrieveFeedTask", getResponse);
		
			HttpGet method = new HttpGet(
					"http://onine.in:8080/servlet/ServletML?any=" + getResponse);
			HttpClient client = new DefaultHttpClient();
			try {
				client.execute(method);
				getResponse="";
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return null;
	}

   
}
