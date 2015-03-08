package com.shopalot.gcm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class CloudConnect {
	public static void registerDevice(String deviceId,String mode)
	{
		HttpClient client = new DefaultHttpClient();
	    HttpPost post = new HttpPost("http://onine.in:8080/GODCloudBackend/GODRegistrator");
	    try {
	      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	      nameValuePairs.add(new BasicNameValuePair("deviceId",deviceId));
	      Log.i("Device id is ",deviceId);
	      nameValuePairs.add(new BasicNameValuePair("mode",mode));
	      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	 
	      HttpResponse response = client.execute(post);
	      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	      String line = "";
	      while ((line = rd.readLine()) != null) {
	        System.out.println(line);
	      }

	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	}
	
	public static String retriveGaalis(int id)
	{
	    String responseString = "";
		HttpClient client = new DefaultHttpClient();
	    HttpGet  get = new HttpGet("http://onine.in:8080/GODCloudBackend/History?id="+id);
	    try {
	      HttpResponse response = client.execute(get);
	      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	        responseString=rd.readLine();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    return responseString;
	}
}
