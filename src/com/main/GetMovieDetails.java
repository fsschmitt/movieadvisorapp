package com.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetMovieDetails {

	String movieLink;
	private static String tmdbApiKey = "0aa6927a7b8e1874ee76dea7c1f5543c";
	int id;
	public GetMovieDetails(String link,int id){
		movieLink = link;
		this.id=id;
	}
	public GetMovieDetails(String link) {
		movieLink = link;
		id=-1;
	}

	private static ArrayList<String> getActors(JSONArray aCast) throws JSONException {
		ArrayList<String> actors = new ArrayList<String>();
		for (int i = 0; i < aCast.length(); i++) {
			JSONObject jo = (JSONObject) aCast.get(i);
			actors.add(jo.getString("name"));

		}
		return actors;
	}

	private static ArrayList<String> getGenres(JSONArray genres) throws JSONException {
		ArrayList<String> key = new ArrayList<String>();
		for (int i = 0; i < genres.length(); i++) {
			key.add(genres.getString(i));
		}

		return key;
	}

	private static String getJSONLine(URL url) throws IOException,
			ParseException {
		BufferedReader in;

		HttpURLConnection tc = (HttpURLConnection) url.openConnection();
		tc.setDoInput(true);
		tc.setDoOutput(true);

		in = new BufferedReader(new InputStreamReader(tc.getInputStream()));
		String line = in.readLine();
		if(line==null) return null;
		
		while( line.equals("")&& (line=in.readLine())!=null);

		System.out.println("linha = " + line);

		return line;

	}

	public Movie call() throws Exception {
		URL url=null;
		if(this.id==-1){
			url = new URL(movieLink+"?apikey="+Utility.apiKey);
		}
		else
			url = new URL("http://api.rottentomatoes.com/api/public/v1.0/movies/"+id+".json?apikey="+Utility.apiKey);
		String line = getJSONLine(url);
		
		JSONObject mov = new JSONObject(line);
		String name = mov.getString("title");
		JSONArray gen = mov.getJSONArray("genres");
		ArrayList<String> genres = getGenres(gen);
		int year = mov.getInt("year");
		int id = mov.getInt("id");
		JSONObject ratings = mov.getJSONObject("ratings");
		double criticsR = ratings.getDouble("critics_score");
		double audienceR = ratings.getDouble("audience_score");
		double rating = 0.7 * criticsR + 0.3 * audienceR;
		ArrayList<String> actors = new ArrayList<String>();
		if (mov.has("abridged_cast")) {
			JSONArray aCast = mov.getJSONArray("abridged_cast");
			actors = getActors(aCast);
		}
		ArrayList<String> directors = new ArrayList<String>();
		if (mov.has("abridged_directors")) {
			JSONArray aCrew = mov.getJSONArray("abridged_directors");
			directors = getActors(aCrew);
		}
		String synopsis = mov.getString("synopsis");
		System.out.println(synopsis);
		JSONObject posters = mov.getJSONObject("posters");
		String imgLink = posters.getString("original");
		
		Movie m;
		if(!mov.isNull("alternate_ids"))
		{
			String idIMDB ="tt"+ mov.getJSONObject("alternate_ids").getString("imdb");
			String imdbLink = "http://www.imdb.com/title/"+idIMDB+"/";
	
			m = new Movie(id, name, year, genres, actors, directors, rating,
					synopsis, imgLink,idIMDB,imdbLink);
			completeMovie(m);
		}
		else
		{
			 m = new Movie(id, name, year, genres, actors, directors, rating,
					synopsis, imgLink,null,null);
			 m.setTrailerLink("null");
		}

		return m;
	}
	private static void completeMovie(Movie mo) throws IOException, ParseException, JSONException {
		
		String uri = "http://api.themoviedb.org/2.1/Movie.imdbLookup/en/json/"+tmdbApiKey+"/"+mo.idIMDB;
		URL url = new URL(uri);
		String line = getJSONLine(url);
		if(line==null || line.equals("[\"Nothing found.\"]"))
        {
                mo.setTrailerLink("Not avaliable");
                return;
        }
		JSONArray jsa = new JSONArray(line);
		JSONObject movie = jsa.getJSONObject(0);
		int tmdbID = movie.getInt("id");
		mo.setIdTMDB(tmdbID);
		url = new URL( "http://api.themoviedb.org/2.1/Movie.getInfo/en/json/"+tmdbApiKey+"/"+tmdbID);
		System.out.println(url);
		line = getJSONLine(url);
		jsa = new JSONArray(line);
		movie = jsa.getJSONObject(0);
		if(mo.synopsis.equals(""))
			mo.synopsis = movie.getString("overview");
		JSONArray cast = movie.getJSONArray("cast");
		fillCast(cast,mo);
		mo.setTrailerLink(movie.getString("trailer"));
		
	}
	
	private static void fillCast(JSONArray cast, Movie mo) throws JSONException  {
		for(int i = 0; i< cast.length();i++){
			JSONObject person =  cast.getJSONObject(i);
			String name = person.getString("name");
			String thumb = person.getString("profile");
			if(mo.actors.contains(name) || mo.directors.contains(name))
				mo.personsFotosLinks.put(name, thumb);
		}
		for(String a : mo.actors){
			if(mo.personsFotosLinks.get(a)==null)
				mo.personsFotosLinks.put(a, "");
		}
		for(String a : mo.directors){
			if(mo.personsFotosLinks.get(a)==null)
				mo.personsFotosLinks.put(a, "");
		}
		
	}

}
