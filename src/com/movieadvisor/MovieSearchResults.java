package com.movieadvisor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MovieSearchResults extends ListActivity {

	ArrayList<String> movies;
	ArrayList<String> ids;
	ArrayList<String> search;
	static boolean oneResult = false;
	private ProgressDialog dialog;
	private String movieSearch;
	public String pagelimit;
	static String apiKey = "97hce5svy2pqxcj6kbrqkktb";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		 
		pagelimit = prefs.getString("resultsnumber","30");
		
		movies = new ArrayList<String>();
		ids = new ArrayList<String>();
		search = new ArrayList<String>();

		Bundle b = this.getIntent().getExtras();
		movieSearch = b.getString("movieName");
		movieSearch = movieSearch.replaceAll(Pattern.quote(" "), "+");

		dialog = ProgressDialog.show(this, "", "Loading...",
				true);
		dialog.setCancelable(true);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			   public void onCancel(DialogInterface dialog) {
			    finish();
			   }
			  });
		new RetrieveMovies().execute(movieSearch);
		// search.add("Searching...");
		setListAdapter(new ArrayAdapter<String>(this,
				R.layout.moviesearchresults, search));

	}

	public static String getJSONLine(URL url) throws IOException {
		BufferedReader in;

		URLConnection tc = url.openConnection();
		tc.setDoInput(true);
		tc.setDoOutput(true);
		in = new BufferedReader(new InputStreamReader(tc.getInputStream()));
		String line = in.readLine();

		while ((line = in.readLine()).equals(""))
			;

		return line;
	}

	class RetrieveMovies extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... arg0) {
			String uri = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey="
					+ apiKey + "&q=" + arg0[0]+"&page_limit="+pagelimit;
			System.out.println(uri);
			URL url = null;
			try {
				url = new URL(uri);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String line = null;
			JSONArray jsa = null;
			JSONObject jo = null;
			try {
				line = getJSONLine(url);
				jo = new JSONObject(line);
				jsa = jo.getJSONArray("movies");
				for (int i = 0; i < jsa.length(); i++) {
					JSONObject movie;
					JSONObject movieLink = null;
					String movieName = null;

					movie = jsa.getJSONObject(i);
					movieLink = movie.getJSONObject("links");
					movieName = movie.getString("title");
					movies.add(movieName);
					ids.add(movieLink.getString("self"));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		// Called once the background activity has completed
		@Override
		protected void onPostExecute(String result) { //

			if (movies.size() == 1) {
				oneResult = true;
				movieDetails(null, MovieSearchResults.this);
			}
			
			if (movies.size() == 0) {
				setResult(RESULT_FIRST_USER);
				finish();
				
			} else {
				

			ListView lv = MovieSearchResults.this.getListView();
			lv.setTextFilterEnabled(true);
			setListAdapter(new ArrayAdapter<String>(MovieSearchResults.this,
					R.layout.moviesearchresults, movies));

			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					movieDetails(view, null);
				}

			});
			dialog.dismiss();
			}

		}

	}


	private void movieDetails(View view, MovieSearchResults md) {
		Intent intent;
		String movieName;
		if (md == null) {
			intent = new Intent(view.getContext(), MovieDetails.class);
			movieName = ((TextView) view).getText().toString();
		} else {
			intent = new Intent(md, MovieDetails.class);
			movieName = movies.get(0);
		}

		Bundle b = new Bundle();

		int i;
		for (i = 0; i < movies.size(); i++)
			if (movies.get(i).compareTo(movieName) == 0)
				break;

		b.putString("movieName", movieName);

		String movieId = ids.get(i);
		b.putString("movieLink", movieId);
		intent.putExtras(b);
		startActivity(intent);
	}
	
	
	protected void onResume() {

		super.onResume();
		
		if(oneResult)
		{
			oneResult = false;
			finish();
		}

	}

}
