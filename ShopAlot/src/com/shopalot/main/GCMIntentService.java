package com.shopalot.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.android.gcm.GCMBaseIntentService;
import com.shopalot.gcm.CloudConnect;
import com.shopalot.main.R;
import com.shopalot.main.ShopAlotTabActivity;
	
public class GCMIntentService extends GCMBaseIntentService {
	private static final String SENDER_ID = "768408904888"; //GCM Sender ID for Google Cloud Messaging
	
	 public GCMIntentService() {
	        super(SENDER_ID);
	    }
	
	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMessage(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		
		// Prepare intent which is triggered if the
		// notification is selected
//		startActivity(new Intent(this,GodSplash.class));
		Intent intent = new Intent(this,ShopAlotTabActivity.class);
		String regionName = arg1.getStringExtra("regionname");
		intent.putExtra("regionname", arg1.getStringExtra("regionname"));
		intent.putExtra("frenzy", "yes");
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		
		NotificationCompat.Builder mBuilder =
	        new NotificationCompat.Builder(this)
			.setSmallIcon(R.drawable.ic_launcher) 
	        .setContentTitle("Frenzy Nearby Found")
	        .setContentText("30 % discount near "+regionName)
	        .setContentIntent(pendingIntent);
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		try {
	        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
	        r.play();
	    } catch (Exception e) {}
		
		mBuilder.getNotification().flags = Notification.FLAG_AUTO_CANCEL;
		
		notificationManager.notify(0, mBuilder.getNotification());
	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		CloudConnect.registerDevice(arg1, "1");
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		CloudConnect.registerDevice(arg1, "0");
	}
	
	

	
	
}
