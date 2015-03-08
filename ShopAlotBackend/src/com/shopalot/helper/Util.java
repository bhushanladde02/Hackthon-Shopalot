package com.shopalot.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

//Can use this code for Broad casting gali

public class Util{
	public static void broadcast() { 
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet("http://onine.in:8080/ShopAlotBackend/servlet/ShopAlotBroadCast");
		try {
			HttpResponse response = client.execute(get);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
