package com.main;

import java.util.ArrayList;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PersonView extends BaseAdapter {

	private ArrayList<String> persons;

	public PersonView(ArrayList<String> persons) {
		this.persons = persons;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout layout;
		layout = new LinearLayout(parent.getContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		TextView person = new TextView(parent.getContext());
		person.setText(persons.get(position));
		person.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		person.setLines(1);
		layout.addView(person);

		return layout;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return persons.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
