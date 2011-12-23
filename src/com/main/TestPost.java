/**
 * This example shows how to post status to Twitter.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 * 
 * http://www.londatiga.net
 */

package com.main;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.twitter.android.TwitterApp;
import com.twitter.android.TwitterApp.TwDialogListener;

public class TestPost extends Activity {
	private TwitterApp mTwitter;
	private String username = "";
	private boolean postToTwitter = true;
	
	private static final String twitter_consumer_key = "UOWHQYywr7y4HiCohJHovg";
	private static final String twitter_secret_key = "eq6t9CLVYlZAaJ7RabDa02lbEZeBKCmiz3kZ2SdXaGc";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.post);
		mTwitter = new TwitterApp(this, twitter_consumer_key,twitter_secret_key);
		if (!mTwitter.hasAccessToken()) {
			mTwitter.authorize();
		}
		else {
			username 	= mTwitter.getUsername();
			Toast.makeText(TestPost.this, "Connected to Twitter as " + username, Toast.LENGTH_LONG).show();
		}
			
		
		Button postBtn 				= (Button) findViewById(R.id.button1);
		final EditText reviewEdit   = (EditText) findViewById(R.id.revieew);
	
		Bundle b = this.getIntent().getExtras();
		String text = b.getString("text");
		reviewEdit.setText(text);
		
		postBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String review = reviewEdit.getText().toString();
				
				if (review.equals("")) return;
				
				if (postToTwitter) postToTwitter(review);
			}
		});

		
		
		mTwitter.setListener(mTwLoginDialogListener);
	
	}
	
	private void postToTwitter(final String review) {
		new Thread() {
			@Override
			public void run() {
				int what = 0;
				
				try {
					mTwitter.updateStatus(review);
				} catch (Exception e) {
					what = 1;
				}
				
				mHandler.sendMessage(mHandler.obtainMessage(what));
			
			}
		}.start();
		
	}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String text = (msg.what == 0) ? "Posted to Twitter" : "Post to Twitter failed";	
			Toast.makeText(TestPost.this, text, Toast.LENGTH_SHORT).show();
			finish();
		}
	};
	private final TwDialogListener mTwLoginDialogListener = new TwDialogListener() {
		@Override
		public void onComplete(String value) {
			username 	= mTwitter.getUsername();
			username	= (username.equals("")) ? "No Name" : username;
		
			postToTwitter = true;
			
			Toast.makeText(TestPost.this, "Connected to Twitter as " + username, Toast.LENGTH_LONG).show();
		}
		
		@Override
		public void onError(String value) {
			
			Toast.makeText(TestPost.this, "Twitter connection failed", Toast.LENGTH_LONG).show();
		}
	};
}