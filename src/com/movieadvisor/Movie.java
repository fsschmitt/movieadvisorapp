
package com.movieadvisor;

import java.util.ArrayList;
import java.util.HashMap;

public class Movie implements Comparable<Object> {
	
	public ArrayList<String> genres;
	public ArrayList<String> actors;
	public ArrayList<String> directors;
	public ArrayList<String> keywords;
	public ArrayList<Integer> genresIDs;
	public ArrayList<Integer> actorsIDs;
	public ArrayList<Integer> directorsIDs;
	public String synopsis;
	public String name;
	double rating;
	double points = -1;
	private String trailerLink = null;
	public HashMap<String,String> personsFotosLinks = new HashMap<String,String>();
	public int idRT;
	public int idTMDB;
	public String idIMDB;
	public String imdbLink;
	int year;
	double sumA = 0, sumD = 0, sumK = 0, sumG=0,sumS=0;
	 public String imageUrl;
	
	Movie(int id,String name, int year ,ArrayList<String> genres,	ArrayList<String> actors,ArrayList<String> directors,double rating,String synop,String img,String idIMDB,String imdbLink){
		this.genres=genres;
		this.actors = actors;
		this.directors = directors;
		this.rating=rating;
		this.name = name;
		this.idRT = id;
		this.year=year;
		this.keywords = new ArrayList<String>();
		this.imageUrl = img;
	    this.synopsis=synop;
	    this.idIMDB = idIMDB;
	    this.imdbLink=imdbLink;
	    this.trailerLink = null;
	}
	public void addPoints(double d){
		this.points+=d;
	}
	@Override
	public int compareTo(Object arg) {
		Movie m = (Movie)arg;
		if(points!=-1){
		if(points<m.points)
			return 1;
		if(points>m.points)
			return -1;
		return 0;
		}
		if(rating<m.rating)
			return 1;
		if(rating>m.rating)
			return -1;
		return 0;
		
	}
	@Override
	public String toString(){
		String r = "ID : " + idRT + "\nNome : " + name + "\nAno : " + year + "\nRating : " +rating+ "\nPoints : "+points;
		r +="\nActors :\n";
		for(String a:actors){
			
			r+="Nome : " + a+" "+personsFotosLinks.get(a)+"\n";
		}
		r +="\nDirectores :\n";
		for(String a:directors){
			r+="Nome : " + a+" "+personsFotosLinks.get(a)+"\n";
		}
		r +="\nKeywords :\n";
		for(String a:keywords){
			r+= a+"\n";
		}
		r+="Synopsis : \n"+synopsis+"\n";
		r+="Trailer : "+trailerLink+"\n";
		r+= "This film got "+ sumA + " points due to its actors, "+ sumD +" points due to its director(s), "+sumG+" points due to its genres , " +0.1*rating +" points due to its rating and " + sumS + " points due to the number of suggestions\n";
		return r;
	}
	public void resetPoints() {
		points=0;
	}
	@Override
	public boolean equals(Object obj){
		return this.idRT == ((Movie)obj).idRT;
	}
	@Override
	public int hashCode(){
		return this.idRT;
	}


	public void setTrailerLink(String trailerLink) {
		this.trailerLink = trailerLink;
	}


	public String getTrailerLink() {
		return trailerLink;
	}


	public void setPersonsFotosLinks(HashMap<String,String> personsFotosLinks) {
		this.personsFotosLinks = personsFotosLinks;
	}


	public HashMap<String,String> getPersonsFotosLinks() {
		return personsFotosLinks;
	}


	public void setIdTMDB(int idTMDB) {
		this.idTMDB = idTMDB;
	}


	public int getIdTMDB() {
		return idTMDB;
	}

	

}
