package com.movieadvisor;

import java.io.IOException;
import java.net.MalformedURLException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

import com.database.DataHelper;
import com.facebook.android.Facebook;
import com.twitter.android.TwitterApp;

public class PrefsActivity extends PreferenceActivity { //
	
	private DataHelper dh;
	
	public CharSequence[] getUsername(){
	    AccountManager manager = AccountManager.get(this); 
	    Account[] accounts = manager.getAccountsByType("com.google"); 
	    CharSequence[] possibleEmails = new CharSequence[accounts.length];

	    int i = 0;
	    for (Account account : accounts) {
	      possibleEmails[i] = account.name;
	      i++;
	    }
	    return possibleEmails;
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) { //
	super.onCreate(savedInstanceState);
	addPreferencesFromResource(R.xml.prefs); //
	
	Preference showmovie = (Preference) findPreference("showposter");
	showmovie.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	    
		public boolean onPreferenceClick(final Preference preference) {
			return false;
		}
	});
	
	
	Preference donate = (Preference) findPreference("donate");
	donate.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	    
		public boolean onPreferenceClick(final Preference preference) {
			
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"movieadvisor@gmail.com"});
			i.putExtra(Intent.EXTRA_SUBJECT, "Movie Advisor Contribution");
			i.putExtra(Intent.EXTRA_TEXT   , "Hi! I would like to donate to Movie Advisor. Please contact me!");
			try {
			    startActivity(Intent.createChooser(i, "Send mail..."));
			} catch (android.content.ActivityNotFoundException ex) {
			    Toast.makeText(preference.getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
			}

			return true;
	
		}
	});
	
	Preference twitter = (Preference) findPreference("erasetwitterdata");
	twitter.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	    
		public boolean onPreferenceClick(final Preference preference) {
			new AlertDialog.Builder(preference.getContext())
		    .setTitle("Confirmation")
		    .setMessage("Are you sure you want to erase your Twitter login information?")
		    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		      

				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					TwitterApp mTwitter = new TwitterApp(preference.getContext(), Utility.twitter_consumer_key,Utility.twitter_secret_key);
					mTwitter.resetAccessToken();
					Toast.makeText(preference.getContext(), "Information deleted!", Toast.LENGTH_LONG)
					.show();
				}

			
		     }).setNegativeButton("No", new DialogInterface.OnClickListener() {
			      

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}

				
			     })
		     .show();
			return true;
	    }
	});
	
	Preference database = (Preference) findPreference("erasedatabasedata");
	database.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	    
		public boolean onPreferenceClick(final Preference preference) {
			new AlertDialog.Builder(preference.getContext())
		    .setTitle("Confirmation")
		    .setMessage("Are you sure you want to erase your Database information?")
		    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		      

				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					dh = new DataHelper(PrefsActivity.this);
					dh.deleteAll();
					Toast.makeText(preference.getContext(), "Information deleted!", Toast.LENGTH_LONG)
					.show();
				}

			
		     }).setNegativeButton("No", new DialogInterface.OnClickListener() {
			      

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}

				
			     })
		     .show();
			return true;
	    }
	});
	
	Preference myPref = (Preference) findPreference("erasefacebookdata");
	myPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	    
		public boolean onPreferenceClick(final Preference preference) {
			new AlertDialog.Builder(preference.getContext())
		    .setTitle("Confirmation")
		    .setMessage("Are you sure you want to erase your Facebook login information?")
		    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		      

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Facebook facebook = new Facebook(Utility.APP_ID);
					try {
						facebook.logout(preference.getContext());
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Toast.makeText(preference.getContext(), "Information deleted!", Toast.LENGTH_LONG)
					.show();
				}

			
		     }).setNegativeButton("No", new DialogInterface.OnClickListener() {
			      

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}

				
			     })
		     .show();
			return true;
	    }
	});

	
	ListPreference account = (ListPreference) findPreference("accountchooser");
	CharSequence[] accounts = getUsername();
	account.setEntries(accounts);
	account.setEntryValues(accounts);
	account.setDefaultValue(null);
	account.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference,
                Object newValue) {
        	Utility.ID_DEFAULT =(String) newValue;
            return true;
        }
    });

	
	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    if (dh != null) {
	        dh.close();
	    }
	}
	
}