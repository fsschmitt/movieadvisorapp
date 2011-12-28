package com.movieadvisor;

import java.util.ArrayList;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class MainActivity extends TabActivity {
	static MainActivity self;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//SharedPreferences prefs = PreferenceManager	.getDefaultSharedPreferences(MainActivity.this);
		//Utility.ID_DEFAULT = prefs.getString("accountchooser", null);
		Utility.ID_DEFAULT = "default";
		setContentView(R.layout.main);
		self=this;
		Utility.client = new MyHttpClient(getApplicationContext()); // Instantiate the custom HttpClient
		insertTabs(0);
	}

	public TabHost insertTabs(int currentTab) {
		final TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab
		Bundle b;
		
		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, MovieAdvisor.class);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost
				.newTabSpec("movieadvisor")
				.setIndicator("Home")
				.setContent(intent);
		tabHost.addTab(spec);

		
		// Do the same for the other tabs
	    intent = new Intent().setClass(this, Lists.class);
	    Utility.ids.add(new ArrayList<Integer>());
	    Utility.names.add(new ArrayList<String>());
	    b = new Bundle();
	    b.putInt("cod", 0);
	    intent.putExtra("listType", "ToSeeList");
	    intent.putExtras(b);
	    spec = tabHost.newTabSpec("toseelist").setIndicator("To See")
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, Lists.class);
	    Utility.ids.add(new ArrayList<Integer>());
	    Utility.names.add(new ArrayList<String>());
	    b = new Bundle();
	    b.putInt("cod", 1);
	    intent.putExtra("listType", "SeenList");
	    intent.putExtras(b);
	    spec = tabHost.newTabSpec("alreadyseenlist").setIndicator("Already Seen")
	                  .setContent(intent);
	    tabHost.addTab(spec);   
		
		tabHost.setCurrentTab(currentTab);
		
		tabHost.setOnTabChangedListener(new OnTabChangeListener(){
			@Override
			public void onTabChanged(String tabId) {
				/*if(!tabId.equals("movieadvisor"))
					Utility.checkAccount(tabHost, MainActivity.this);*/
			}
		
		});
		return tabHost;
	}
}
