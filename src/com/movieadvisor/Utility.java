package com.movieadvisor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
public class Utility {
	
	public static String ID_DEFAULT = null;
	public static String IP_DEFAULT = "172.30.89.202";
	public static String PORT_DEFAULT = "8082";	
	public static final String APP_ID = "168201746572549";
	public static String apiKey = "97hce5svy2pqxcj6kbrqkktb";
	public static final String twitter_consumer_key = "UOWHQYywr7y4HiCohJHovg";
	public static final String twitter_secret_key = "eq6t9CLVYlZAaJ7RabDa02lbEZeBKCmiz3kZ2SdXaGc";
	public static DefaultHttpClient client;
	public static ArrayList<ArrayList<Integer>> ids = new ArrayList<ArrayList<Integer>>();
	public static ArrayList<ArrayList<String>> names = new ArrayList<ArrayList<String>>();
	public static ArrayList<Lists> lists = new ArrayList<Lists>();
	public static ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
    
	
	public static void setListViewHeightBasedOnChildren(ListView listView,
			ArrayList<String> elements) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.AT_MOST);
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}
	

	public static void checkAccount(final TabHost tabHost, Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String account = prefs.getString("accountchooser",null);
		if (account == null) {
			
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage("Please choose an account to synchonize in the configurations!")
			       .setCancelable(false)
			       .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			                tabHost.setCurrentTab(0);
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
		}
	}
	
	public static String getListServer(int idList){
		try{
        	
        	HttpGet get = new HttpGet("https://"+Utility.IP_DEFAULT+":"+Utility.PORT_DEFAULT+"/List?" +"id="+Utility.ID_DEFAULT+"&idList="+idList);
        	HttpResponse getResponse = Utility.client.execute(get); // Execute the GET call and obtain the response
        	HttpEntity responseEntity = getResponse.getEntity();
	        BufferedReader myIn = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
	        String str = myIn.readLine();
	        System.out.println(str);
	        return str;
	       
        }catch(Exception e){
        	return "Not Found";
        }
	}
	
	public static void checkAccount(final Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String account = prefs.getString("accountchooser",null);
		if (account == null) {
			
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage("Please choose an account to synchonize in the configurations!")
			       .setCancelable(false)
			       .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			                Intent intent = new Intent(context,
			    					PrefsActivity.class);
			    			context.startActivity(intent);
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
		}
	}
	
	public static String getJSONLine(URL url) throws IOException,	ParseException {
		
		String response = null;
		HttpClient httpclient = null;
		try {
		    HttpGet httpget = new HttpGet(url.toURI());
		    httpclient = new DefaultHttpClient();
		    HttpResponse httpResponse = httpclient.execute(httpget);

		    final int statusCode = httpResponse.getStatusLine().getStatusCode();
		    if (statusCode != HttpStatus.SC_OK) {
		        throw new Exception("Got HTTP " + statusCode 
		            + " (" + httpResponse.getStatusLine().getReasonPhrase() + ')');
		    }

		    response = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);

		} catch (Exception e) {
		    e.printStackTrace();

		} finally {
		    if (httpclient != null) {
		        httpclient.getConnectionManager().shutdown();
		        httpclient = null;
		    }
		}
		return response;
	}
	
	public static Pair<ArrayList<Integer>, ArrayList<String>> getSimilar(int id) throws Exception
	{
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ArrayList<String> names = new ArrayList<String>();
		String uri = "http://api.rottentomatoes.com/api/public/v1.0/movies/"+id+"/similar.json?apikey="+Utility.apiKey+"&limit=5";
		URL url = new URL(uri);
		String line = Utility.getJSONLine(url);
		JSONObject jo = new JSONObject(line);
		JSONArray jsa =jo.getJSONArray("movies");
		
		for (int i = 0; i < jsa.length(); i++) {
			JSONObject jsoMovie = jsa.getJSONObject(i);
			ids.add(jsoMovie.getInt("id"));
			names.add(jsoMovie.getString("title"));
		}
		return new Pair<ArrayList<Integer>, ArrayList<String>>(ids, names);
	}
}
