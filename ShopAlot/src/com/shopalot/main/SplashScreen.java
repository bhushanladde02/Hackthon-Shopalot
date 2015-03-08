package com.shopalot.main;

import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashScreen extends Activity {
	
	private static int SPLASH_TIME_OUT = 3500;
	private static final String SENDER_ID = "768408904888"; //GCM Sender ID for hackthon
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);

		
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
//		final String regId = GCMRegistrar.getRegistrationId(this);
//		System.out.println("Registration ID is "+ regId);
//		GCMRegistrar.register(this, SENDER_ID);
		
		
		
		
		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				Intent i = new Intent(SplashScreen.this, ShopAlotTabActivity.class);
				startActivity(i);

				// close this activity
				finish();
			}
		}, SPLASH_TIME_OUT);
	}	
}
