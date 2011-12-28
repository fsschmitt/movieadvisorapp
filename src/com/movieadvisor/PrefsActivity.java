package com.movieadvisor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.channels.FileChannel;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
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
	
	
	Preference backupdb = (Preference) findPreference("backupdb");
	backupdb.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	    
		public boolean onPreferenceClick(final Preference preference) {
			new AlertDialog.Builder(preference.getContext())
		    .setTitle("Confirmation")
		    .setMessage("Do you want to backup your database?")
		    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		      

				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					
					try {
				        File sd = Environment.getExternalStorageDirectory();

				        if (sd.canWrite()) {
				            String currentDBPath = "/data/data/com.movieadvisor/databases/movieadvisor.db";
				            String backupDBPath = "movieadvisor.db";
				            File currentDB = new File(currentDBPath);
				            File backupDB = new File(sd, backupDBPath);
				            if (currentDB.exists()) {
				                FileChannel src = new FileInputStream(currentDB).getChannel();
				                FileChannel dst = new FileOutputStream(backupDB).getChannel();
				                dst.transferFrom(src, 0, src.size());
				                src.close();
				                dst.close();
				            }
				            else throw new Exception();
				        }
				        else
				        	throw new Exception();
				        
				        Toast.makeText(preference.getContext(), "Database saved on the root of your SD Card", Toast.LENGTH_LONG)
						.show();
				    } catch (Exception e) {
				    	
				    	Toast.makeText(preference.getContext(), "Error. Do you have a SD Card?", Toast.LENGTH_LONG)
						.show();
				    }
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
	
	
	
	Preference restoredb = (Preference) findPreference("restoredb");
	restoredb.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	    
		public boolean onPreferenceClick(final Preference preference) {
			new AlertDialog.Builder(preference.getContext())
		    .setTitle("Confirmation")
		    .setMessage("Do you want to restore your database?")
		    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		      

				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					
					try {
				        File sd = Environment.getExternalStorageDirectory();

				        if (sd.canWrite()) {
				            String backupDBPath = "/data/data/com.movieadvisor/databases/movieadvisor.db";
				            String currentDBPath = "movieadvisor.db";
				            File currentDB = new File(sd, currentDBPath);
				            File backupDB = new File(backupDBPath);
				            if (currentDB.exists()) {
				                FileChannel src = new FileInputStream(currentDB).getChannel();
				                FileChannel dst = new FileOutputStream(backupDB).getChannel();
				                dst.transferFrom(src, 0, src.size());
				                src.close();
				                dst.close();
				            }
				            else throw new Exception();
				        }
				        else
				        	throw new Exception();
				        
				        Toast.makeText(preference.getContext(), "Database restored.", Toast.LENGTH_LONG)
						.show();
				    } catch (Exception e) {
				    	
				    	Toast.makeText(preference.getContext(), "Error. Do you have a SD Card and a previous backup?", Toast.LENGTH_LONG)
						.show();
				    }
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


	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    if (dh != null) {
	        dh.close();
	    }
	}
	
}