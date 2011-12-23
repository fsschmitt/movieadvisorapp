
package com.sugestor;

import java.util.ArrayList;

public class MovieSugest implements Comparable<Object> {
	
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
	public int id;
	int year;
	double sumA = 0, sumD = 0, sumK = 0, sumG=0,sumS=0;
	 public String imageUrl;
	    public MovieSugest (ArrayList<Integer> catI, 
                ArrayList<Integer> actorsI,
                ArrayList<Integer> directorsI, 
                ArrayList<String> keys,
                double rating, String name, 
                int idMovie,
                ArrayList<String> genres,
                ArrayList<String> actors,
                ArrayList<String> directors,int year,String imageUrl){
        this.genres=genres;
        this.actors = actors;
        this.directors = directors;
        this.genresIDs=catI;
        this.actorsIDs = actorsI;
        this.directorsIDs = directorsI;
        this.rating=rating;
        this.name = name;
        this.id = idMovie;
        this.year=year;
        this.keywords=keys;
        this.imageUrl = imageUrl;
    
}

	
	MovieSugest(int id,String name, int year ,ArrayList<String> genres,	ArrayList<String> actors,ArrayList<String> directors,double rating,String synop,String img){
		this.genres=genres;
		this.actors = actors;
		this.directors = directors;
		this.rating=rating;
		this.name = name;
		this.id = id;
		this.year=year;
		this.keywords = new ArrayList<String>();
		this.imageUrl = img;
	    this.synopsis=synop;
	}
	public MovieSugest(ArrayList<Integer> cat, ArrayList<Integer> actors,
			ArrayList<Integer> directors, ArrayList<String> keys,
			double rating, String name, int idMovie) {
		
		this.genresIDs=cat;
		this.actorsIDs = actors;
		this.directorsIDs = directors;
		this.rating=rating;
		this.name = name;
		this.id = idMovie;
		
	}
	public MovieSugest (ArrayList<Integer> catI, 
			ArrayList<Integer> actorsI,
			ArrayList<Integer> directorsI, 
			ArrayList<String> keys,
			double rating, String name, 
			int idMovie,
			ArrayList<String> genres,
			ArrayList<String> actors,
			ArrayList<String> directors,int year){
		this.genres=genres;
		this.actors = actors;
		this.directors = directors;
		this.genresIDs=catI;
		this.actorsIDs = actorsI;
		this.directorsIDs = directorsI;
		this.rating=rating;
		this.name = name;
		this.id = idMovie;
		this.year=year;
		this.keywords=keys;
	}
	public void addPoints(double d){
		this.points+=d;
	}
	@Override
	public int compareTo(Object arg) {
		MovieSugest m = (MovieSugest)arg;
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
		String r = "ID : " + id + "\nNome : " + name + "\nAno : " + year + "\nRating : " +rating+ "\nPoints : "+points;
		r +="\nActors :\n";
		for(String a:actors){
			r+="Nome : " + a+"\n";
		}
		r +="\nDirectores :\n";
		for(String a:directors){
			r+="Nome : " + a+"\n";
		}
		r +="\nKeywords :\n";
		for(String a:keywords){
			r+= a+"\n";
		}
		r+="Synopsis : \n"+synopsis+"\n";
		r+= "This film got "+ sumA + " points due to its actors, "+ sumD +" points due to its director(s), "+sumG+" points due to its genres , " +0.1*rating +" points due to its rating and " + sumS + " points due to the number of suggestions\n";
		return r;
	}
	public void resetPoints() {
		points=0;
	}
	@Override
	public boolean equals(Object obj){
		return this.id == ((MovieSugest)obj).id;
	}
	@Override
	public int hashCode(){
		return this.id;
	}

	

}
