package com.movieadvisor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.json.JSONException;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.database.DataHelper;
import com.sugestor.GetMovieDetailsRT;
import com.sugestor.MovieSugest;
import com.sugestor.MovieSugestor;

public class Lists extends ListActivity {
	String listType;
	private ProgressDialog dialog;
	private int cod;
	private DataHelper dh;

	public void refreshMyList(String type) {
		if (Utility.ID_DEFAULT != null)
			new RetrieveList().execute(type);
		else
			dialog.dismiss();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dh = new DataHelper(this);
		Utility.lists.add(this);
		Bundle b = this.getIntent().getExtras();
		this.cod = b.getInt("cod");
		dialog = ProgressDialog.show(this, "", "Loading...", true);
		dialog.setCancelable(true);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			   public void onCancel(DialogInterface dialog) {
			    finish();
			   }
			  });
		refreshMyList("get");
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.getId() == getListView().getId()) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(Utility.names.get(this.cod).get(info.position));
			menu.add(Menu.NONE, 0, 0, "Apagar");
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int movie = info.position;

		if (!deleteFromServer(Utility.ids.get(this.cod).get(movie))) {
			Toast.makeText(this, "Error deleting movie from database!",	Toast.LENGTH_LONG).show();
			
			return true;
		}
		
		refreshMyList("get");
		return true;
	}

	private void refreshList() {
		Utility.names.get(this.cod).clear();
		Utility.ids.get(this.cod).clear();
		
		ArrayList<ArrayList<String>> movies = new ArrayList<ArrayList<String>>();
		Collection<ArrayList<String>> info = new ArrayList<ArrayList<String>>();
		System.out.println("listype: "+cod);
		switch(cod){
			case 0:
				movies = dh.selectAllByNames("table_tosee");
				break;
			case 1:
				movies = dh.selectAllByNames("table_seen");
				break;
			default:
				System.out.println("here");
				break;
		}
		if(movies.size()!=0){
			info = movies;
			for (ArrayList<String> i : info) {
				System.out.println("id: "+i.get(0) + "   name: " + i.get(1));
				Utility.ids.get(this.cod).add(Integer.parseInt(i.get(0)));
				Utility.names.get(this.cod).add(i.get(1));
			}
		}
		
	}

	public boolean deleteFromServer(int idRT) {
		try {
			switch(cod){
				case 0:
					dh.deleteID("table_tosee", String.valueOf(idRT));
					break;
				case 1:
					dh.deleteID("table_seen", String.valueOf(idRT));
					break;
				default:
					break;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void movieDetails(View view, int position) {

		Intent intent = new Intent(view.getContext(), MovieDetails.class);

		Bundle b = new Bundle();

		b.putString("movieName", Utility.names.get(this.cod).get(position));

		String movieId = Utility.ids.get(this.cod).get(position) + "";
		b.putString("movieId", movieId);
		intent.putExtras(b);
		startActivity(intent);
	}
	public void onBackPressed() {
		MainActivity.self.getTabHost().setCurrentTab(0);
	}

	class RetrieveList extends AsyncTask<String, Integer, Boolean> {
		
		@Override
		protected Boolean doInBackground(String... arg0) {
				try {
					Log.d("Database","Accessing db");
					refreshList();
					return true;
				} catch (Exception e) {
					return false;
				}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				setListAdapter(new ArrayAdapter<String>(Lists.this,
						R.layout.moviesearchresults, Utility.names.get(cod)));
				ListView lv = Lists.this.getListView();
				
				registerForContextMenu(lv);

				lv.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						movieDetails(view, position);

					}

				});
			}
			
			dialog.dismiss();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.listmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.suggestlist:
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(Intent.EXTRA_SUBJECT, "MovieAdvisor Suggestion");

			String out = "";
			for (int z = 0; z < Utility.names.get(cod).size(); z++) {
				if (z != 0) {
					out += ", ";
				}
				out += Utility.names.get(cod).get(z);
			}
			switch (cod) {
			case 0:
				i.putExtra(Intent.EXTRA_TEXT,
						"Hi! Have you seen my To See list?  Check it out:\n"
								+ out + "\n\nSuggested by Movie Advisor.");
				break;
			case 1:
				i.putExtra(Intent.EXTRA_TEXT,
						"Hi! Have you seen my Already Seen list?  Check it out:\n"
								+ out + "\n\nSuggested by Movie Advisor.");
				break;
			}

			try {
				startActivity(Intent.createChooser(i, "Sending..."));
			} catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(Lists.this,
						"There are no email clients installed.",
						Toast.LENGTH_SHORT).show();
			}

			break;

		case R.id.about:
			String ms1 = "This application was developed by some really cool guys at Faculdade de Engenharia da Universidade do Porto! ";
			String ms2 = "If you like it, feel free to have a chat with us at movieadvisor@gmail.com!";
			new AlertDialog.Builder(this)
					.setTitle("About")
					.setMessage(ms1 + "\n" + ms2)
					.setPositiveButton("Close",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							}).show();
			break;
		case R.id.config:
			Intent intent = new Intent(this, PrefsActivity.class);
			this.startActivity(intent);
			break;

		case R.id.similar:
			dialog = ProgressDialog.show(this, "", "Loading...", true);
			dialog.setCancelable(true);
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				   public void onCancel(DialogInterface dialog) {
				    finish();
				   }
				  });
			new GetSimilarList().execute(cod + "");
			break;

		}
		return true;

	}

	class GetSimilarList
			extends
			AsyncTask<String, Void, Pair<ArrayList<Integer>, ArrayList<String>>> {
		protected Pair<ArrayList<Integer>, ArrayList<String>> doInBackground(
				String... id) {
			try {
				return getSimilarList(Integer.parseInt(id[0]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.println(0, "gettingSimilar", e.getMessage());
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.getMessage();
			} catch (JSONException e) {
				e.getMessage();
			} catch (Exception e) {
				e.getMessage();
			}
			return null;
		}

		protected void onPostExecute(
				Pair<ArrayList<Integer>, ArrayList<String>> result) {
			if (result != null) {
				Intent intent = new Intent(Lists.this, DisplayList.class);
				dialog.dismiss();
				Bundle b = new Bundle();
				b.putIntegerArrayList("ids", result.getFirst());
				b.putStringArrayList("names", result.getSecond());
				intent.putExtras(b);
				startActivity(intent);

			}
			else
				Toast.makeText(Lists.this,
						"There are no movies on the list.",
						Toast.LENGTH_SHORT).show();
			dialog.dismiss();
		}

	}
	private Pair<ArrayList<Integer>,ArrayList<String>> getSimilarList(int id) throws Exception {
		
		ArrayList<MovieSugest> listMovies = new ArrayList<MovieSugest>();
		ArrayList<MovieSugest> similarMovies = new ArrayList<MovieSugest>();
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ArrayList<String> names = new ArrayList<String>();
		HashMap<String,ArrayList<String>> info = new HashMap<String, ArrayList<String>>();
		switch(id){
			case 0:
				info = dh.selectAll("table_tosee");
				break;
			case 1:
				info = dh.selectAll("table_seen");
				break;
			default:
				return null;
		}
		Collection<ArrayList<String>> movies = info.values();
		for(ArrayList<String> mo : movies){
			String linkM = mo.get(2);
			GetMovieDetailsRT worker = new GetMovieDetailsRT(linkM,-1);
			listMovies.add(worker.call());
		}
		MovieSugestor ms = new MovieSugestor();
		similarMovies = ms.suggestMovie(listMovies);
		for(MovieSugest m : similarMovies){
	        	ids.add(m.id);
	        	names.add(m.name);
        }
		return new Pair<ArrayList<Integer>,ArrayList<String>>(ids,names);
	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    if (dh != null) {
	        dh.close();
	    }
	}

}