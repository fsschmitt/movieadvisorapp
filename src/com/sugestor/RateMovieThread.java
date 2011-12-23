package com.sugestor;

import java.util.HashMap;

public class RateMovieThread implements Runnable{
	MovieSugest movieToRate;
	HashMap<String,Integer> directorImp;
	HashMap<String,Integer> actorImp;
	HashMap<String,Integer> genreImp;
	User user;
	public RateMovieThread(MovieSugest movieToRate,HashMap<String,Integer> directorImp,
	HashMap<String,Integer> actorImp,HashMap<String,Integer> genreImp,User user){
		this.movieToRate= movieToRate;
		this.actorImp=actorImp;
		this.directorImp=directorImp;
		this.genreImp=genreImp;
		this.user=user;
	
	}
	public void run(){
		
		evalGenre();
		evalActors();
		evalDirectors();
		movieToRate.addPoints(0.1*movieToRate.rating);
		
		
	}
	
	private void evalDirectors() {
		//double factor = (double)(user.importance[RatedItems.Director.ordinal()])/100.0;
		double factor=1;
		for(String s: movieToRate.directors){
			if(directorImp.containsKey(s)){
				movieToRate.addPoints(directorImp.get(s)*10*factor);
				movieToRate.sumD+=directorImp.get(s)*10*factor;
			}
		}
		
	}
	private void evalActors() {
		//double factor = (double)(user.importance[RatedItems.Actor.ordinal()])/100.0;
		double factor=1;
		for(String s: movieToRate.actors){
			if(actorImp.containsKey(s)){
				movieToRate.addPoints(actorImp.get(s)*5*factor);
				movieToRate.sumA += actorImp.get(s)*5*factor;
			}
		}
		
	}
	private void evalGenre() {
		//double factor = (double)(user.importance[RatedItems.Genre.ordinal()])/100.0;
		double factor=1;
		for(String s: movieToRate.genres){
			if(genreImp.containsKey(s)){
				movieToRate.addPoints(genreImp.get(s)*15*factor);
				movieToRate.sumG+=genreImp.get(s)*15*factor;
			}
		}
		
	}

}
