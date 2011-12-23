package com.sugestor;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.main.Utility;

public class MovieSugestor {

	static int maxResults = 50;
	static int minVotes = 5;
	static User user;
	static String apiKey = "97hce5svy2pqxcj6kbrqkktb";
	public MovieSugestor() throws IOException, ParseException{
		int[] prefs = new int[RatedItems.values().length];
		prefs[RatedItems.Actor.ordinal()] = 85;
		prefs[RatedItems.Director.ordinal()] = 70;
		prefs[RatedItems.Genre.ordinal()] = 75;
		prefs[RatedItems.Keywords.ordinal()] = 80;
		prefs[RatedItems.Writer.ordinal()] = 20;
		user = new User(prefs);
	}
	
	public static ArrayList<MovieSugest> getSimilar(MovieSugest movie) throws IOException, ParseException, JSONException{
		ArrayList<MovieSugest> ret = new ArrayList<MovieSugest>();
		String uri = "http://api.rottentomatoes.com/api/public/v1.0/movies/"+movie.id+"/similar.json?apikey="+apiKey+"&limit=5";
		URL url = new URL(uri);
		String line = Utility.getJSONLine(url);
		JSONObject jo = new JSONObject(line);
		JSONArray jsa =jo.getJSONArray("movies");
		
		ExecutorService executor = Executors.newFixedThreadPool(10);
		ArrayList<Future<MovieSugest>> list = new ArrayList<Future<MovieSugest>>();
		for (int i = 0; i < jsa.length(); i++) {
			JSONObject jsoMovie = jsa.getJSONObject(i);
			JSONObject movieLink = jsoMovie.getJSONObject("links");
			
			Callable<MovieSugest> worker = new GetMovieDetailsRT(movieLink.getString("self"),-1);
			Future<MovieSugest> submit = executor.submit(worker);
			list.add(submit);
		}
		// Now retrieve the result
		for (Future<MovieSugest> future : list) {
			try {
				ret.add(future.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		executor.shutdown();
		Collections.sort(ret);
		return ret;
	}
	
	public ArrayList<MovieSugest> suggestMovie(ArrayList<MovieSugest> likedMovies) throws IOException, ParseException, JSONException {
		ArrayList<MovieSugest> similars = new ArrayList<MovieSugest>();
		for(MovieSugest m : likedMovies){
			ArrayList<MovieSugest> s = getSimilar(m);
			for(MovieSugest mo : s){
				mo.resetPoints();
				if(similars.contains(mo)){
					double points = similars.get(similars.indexOf(mo)).points;
					similars.remove(mo);
					mo.addPoints(20+points);
					mo.sumS+=20+points;
				}
				if(!likedMovies.contains(mo))
					similars.add(mo);
			}
		}
		
		ArrayList<String> allGenres = new ArrayList<String>();
		for(MovieSugest m : likedMovies)
			allGenres.addAll(m.genres);
		HashMap<String,Integer> genreImp = getImportance(allGenres);
		for(String s : genreImp.keySet())
			System.out.println(s+" - "+genreImp.get(s));
		ArrayList<String> allActors = new ArrayList<String>();
		for(MovieSugest m : likedMovies)
			allActors.addAll(m.actors);
		HashMap<String,Integer> actorImp = getImportance(allActors);
		for(String s : actorImp.keySet())
			System.out.println(s+" - "+actorImp.get(s));
		ArrayList<String> allDirectors = new ArrayList<String>();
		for(MovieSugest m : likedMovies)
			allDirectors.addAll(m.directors);
		HashMap<String,Integer> directorImp = getImportance(allDirectors);
		for(String s : directorImp.keySet())
			System.out.println(s+" - "+directorImp.get(s));
		
		
		@SuppressWarnings("unchecked")
		ArrayList<MovieSugest> similarsNoDup = removeDuplicate(similars);
		
		for(MovieSugest m: similarsNoDup)
			System.out.println(m.name);
		ExecutorService executor = Executors.newFixedThreadPool(similarsNoDup.size());
		
		for(MovieSugest m: similarsNoDup)
			executor.execute(new RateMovieThread(m,directorImp,actorImp,genreImp,user));
		
		executor.shutdown();
		while(!executor.isTerminated());
		
		Collections.sort(similars);
		return similars;
	}
	
	public static HashMap<String, Integer> getImportance(
			ArrayList<String> all) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for(String s : all)
			if(map.containsKey(s)){
				int value = map.get(s);
				value++;
				map.put(s,value);
			}else
				map.put(s, 1);
		
		return map;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList removeDuplicate(ArrayList arlList)
	  {
		
	   HashSet h = new HashSet(arlList);
	   return new ArrayList(h);
	
	  }
	
}
