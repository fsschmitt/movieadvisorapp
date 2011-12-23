package com.database;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


import java.util.ArrayList;
import java.util.HashMap;
 
public class DataHelper {
 
   private static final String DATABASE_NAME = "movieadvisor.db";
   private static final int DATABASE_VERSION = 5;
   private static final String TABLE_SEEN = "table_seen";
   private static final String TABLE_TOSEE = "table_tosee";
   
   private Context context;
   private SQLiteDatabase db;
   private OpenHelper openHelper;
 
   private SQLiteStatement insertStmt;
  
   
 
   public DataHelper(Context context) {
      this.context = context;
      openHelper = new OpenHelper(this.context);
      this.db = openHelper.getWritableDatabase();    
   }
   
   public void close() {
	    if (openHelper != null) {
	        openHelper.close();
	    }
	}
 
   public long insert(String table_name, String movieId, String movieName, String movieLink) {
	  String INSERT = "insert into "
			      + table_name + "(movieId, movieIdRT, movieName, movieLink) values (?, ?, ?, ?)";
	  this.insertStmt = this.db.compileStatement(INSERT);
	  this.insertStmt.bindString(1, movieId);
	  this.insertStmt.bindString(2, movieId);
      this.insertStmt.bindString(3, movieName);
      this.insertStmt.bindString(4, movieLink);
      return this.insertStmt.executeInsert();
   }
   
   public void update(String table_name, String movieId, String movieName, String movieLink) {

	   ContentValues values = new ContentValues();
	   values.put("movieId", movieId);
	   values.put("movieID", movieId);
	   values.put("movieName", movieName);
	   values.put("movieLink", movieLink);
	   db.update(table_name, values, "movieId = ?", new String[] {String.valueOf(movieId)});
	   }
   
   public void deleteID(String table_name, String movieId){
	   this.db.delete(table_name, "movieId = ?", new String[] {String.valueOf(movieId)});
	   }
 
   public void deleteAll(String table_name) {
      this.db.delete(table_name, null, null);
   }
   
   public void deleteAll() {
	      this.db.delete("table_tosee", null, null);
	      this.db.delete("table_seen", null, null);
	   }
 
   public HashMap<String, ArrayList<String>> selectAll(String table_name) {
      HashMap<String, ArrayList<String>> list = new HashMap<String, ArrayList<String>>();
      Cursor cursor = this.db.query(table_name, null, 
        null, null, null, null, null);
      if (cursor.moveToFirst()) {
         do {
        	 ArrayList<String> info = new ArrayList<String>();
        	 for(int i = 1; i < cursor.getColumnNames().length; i++)
        		 info.add(cursor.getString(i));
        	 list.put(cursor.getString(0), info);
         } while (cursor.moveToNext());
      }
      if (cursor != null && !cursor.isClosed()) {
         cursor.close();
      }
      return list;
   }
   
   public HashMap<String, ArrayList<String>> selectAllByNames(String table_name) {
	      HashMap<String, ArrayList<String>> list = new HashMap<String, ArrayList<String>>();
	      Cursor cursor = this.db.query(table_name, new String[]{"movieId", "movieIdRT", "movieName", "movieLink"}, 
	        null, null, null, null, "movieName asc");
	      if (cursor.moveToFirst()) {
	         do {
	        	 ArrayList<String> info = new ArrayList<String>();
	        	 for(int i = 1; i < cursor.getColumnNames().length; i++)
	        		 info.add(cursor.getString(i));
	        	 list.put(cursor.getString(0), info);
	         } while (cursor.moveToNext());
	      }
	      if (cursor != null && !cursor.isClosed()) {
	         cursor.close();
	      }
	      return list;
	   }
   
   public ArrayList<String> selectMovie(String table_name, int movieId) {
	     
	      Cursor cursor = this.db.query(table_name, null, 
	        "movieId = ?", new String[] {String.valueOf(movieId)}, null, null, null);
	      ArrayList<String> info = new ArrayList<String>();
	      if (cursor.moveToFirst()) {
        	 info = new ArrayList<String>();
        	 for(int i = 1; i < cursor.getColumnNames().length; i++)
        		 info.add(cursor.getString(i));
	      }
	      if (cursor != null && !cursor.isClosed()) {
	         cursor.close();
	      }
	      return info;
	   }
   
 
   private static class OpenHelper extends SQLiteOpenHelper {
 
      OpenHelper(Context context) {
         super(context, DATABASE_NAME, null, DATABASE_VERSION);
      }
 
      @Override
      public void onCreate(SQLiteDatabase db) {
         db.execSQL("CREATE TABLE " + TABLE_SEEN + 
        		 "(movieId TEXT PRIMARY KEY, " +
        		 "movieIdRT TEXT," + 
        		 "movieName TEXT," +
        		 "movieLink TEXT" +
        		 ")");
         db.execSQL("CREATE TABLE " + TABLE_TOSEE + 
        		 "(movieId TEXT PRIMARY KEY, " +
        		 "movieIdRT TEXT," + 
        		 "movieName TEXT," +
        		 "movieLink TEXT" +
        		 ")");
      }
 
      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEEN);
         db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOSEE);
         onCreate(db);
      }
   }
}