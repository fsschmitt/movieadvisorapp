package com.sugestor;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.movieadvisor.Utility;

public class GetMovieDetailsRT implements Callable<MovieSugest> {
	
	String movieLink;
	int id;
	public GetMovieDetailsRT(String link,int id){
		movieLink = link;
		this.id=id;
	}
	private static ArrayList<String> getActors(JSONArray aCast) throws JSONException {
		ArrayList<String> actors = new ArrayList<String>();
		for(int i = 0; i< aCast.length();i++ ){
			JSONObject jo = (JSONObject)aCast.get(i);
			actors.add(jo.getString("name"));
			
		}
		return actors;
	}
	private static ArrayList<String> getGenres(JSONArray genres) throws JSONException {
		ArrayList<String> key = new ArrayList<String>();
		for(int i = 0; i< genres.length();i++ ){
			key.add(genres.getString(i));	
		}
		
		return key;
	}
	
	@Override
	public MovieSugest call() throws Exception {
		URL url=null;
		if(this.id==-1)
			url = new URL(movieLink+"?apikey="+MovieSugestor.apiKey);
		else
			url = new URL("http://api.rottentomatoes.com/api/public/v1.0/movies/"+id+".json?apikey="+MovieSugestor.apiKey);
		String line = Utility.getJSONLine(url);
		
		JSONObject mov = new JSONObject(line);
		String name = mov.getString("title");
		JSONArray gen = mov.getJSONArray("genres");
		ArrayList<String> genres = getGenres(gen);
		int year = mov.getInt("year");
		int id = mov.getInt("id");
		JSONObject ratings = mov.getJSONObject("ratings");
		double criticsR = ratings.getDouble("critics_score");
		double audienceR = ratings.getDouble("audience_score");
		double rating = 0.7*criticsR+0.3*audienceR;
		ArrayList<String> actors =  new ArrayList<String>();
		if(mov.has("abridged_cast")){
			JSONArray aCast = mov.getJSONArray("abridged_cast");
			actors = getActors(aCast);
		}
		ArrayList<String> directors = new ArrayList<String>();
		if(mov.has("abridged_directors")){
			JSONArray aCrew = mov.getJSONArray("abridged_directors");
			directors = getActors(aCrew);
		}
		String synopsis = mov.getString("synopsis");
		System.out.println(synopsis);
		JSONObject posters = mov.getJSONObject("posters");
		String imgLink = posters.getString("profile");
	
		MovieSugest m = new MovieSugest(id, name,  year ,genres,actors, directors, rating,synopsis,imgLink);
		
		return m;
	}

}
