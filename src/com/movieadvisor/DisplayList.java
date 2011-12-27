package com.movieadvisor;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class DisplayList extends ListActivity{
	ArrayList<Integer> ids = new ArrayList<Integer>();
	ArrayList<String> names = new ArrayList<String>();
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getIntent().getExtras();
        ids = b.getIntegerArrayList("ids");
        names = b.getStringArrayList("names");
        setListAdapter(new ArrayAdapter<String>(this,
				R.layout.moviesearchresults, names));
        ListView lv = this.getListView();
        
        lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				movieDetails(view,position);
			}

		});
        
	}
private void movieDetails(View view,int position) {
		
		Intent intent = new Intent(view.getContext(), MovieDetails.class);
		
		Bundle b = new Bundle();

		
		b.putString("movieName", names.get(position));

		String movieId = ids.get(position)+"";
		b.putString("movieId", movieId);
		intent.putExtras(b);
		startActivity(intent);
	}
	

}
