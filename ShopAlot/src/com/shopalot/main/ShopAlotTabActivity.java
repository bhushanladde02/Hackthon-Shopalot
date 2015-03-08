package com.shopalot.main;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class ShopAlotTabActivity  extends TabActivity{
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_layout);
		
		TabHost tabHost = getTabHost();
//		tabHost.setup(this.getLocalActivityManager());
	
		// Android tab
		Intent intentNearby = new Intent().setClass(this, NearbyOffer.class);
		intentNearby.putExtra("regionname", this.getIntent().getStringExtra("regionname"));
		TabSpec tabSpecNearby = tabHost
			.newTabSpec("Current")
			.setIndicator("Current")
			.setContent(intentNearby);

		// Apple tab
		Intent intentFrenzy = new Intent().setClass(this, Frenzy.class);
		TabSpec tabSpecFrenzy= tabHost
			.newTabSpec("Frenzy")
			.setIndicator("Frenzy")
			.setContent(intentFrenzy);
		
	
		tabHost.addTab(tabSpecNearby);
		tabHost.addTab(tabSpecFrenzy);
		
		//set Windows tab as default (zero based)
		String frenzy = this.getIntent().getStringExtra("frenzy");
		if(frenzy!=null)
			tabHost.setCurrentTab(1);
		else
			tabHost.setCurrentTab(0);
	}
	
}
