package com.ranjith.billapp;


import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.R.string;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class UpdateActivity extends Activity {
	DataHandler handler;
	ListView listview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		
		listview = (ListView)findViewById(R.id.list);
		handler = new DataHandler(getBaseContext());
		handler.open();
		Cursor cursor = handler.returnItem();
		String[] values = handler.getItems(cursor);
		handler.close();
		//System.out.println("arr: " + Arrays.toString(values));
		
		//String [] values ={"ranjith","varma"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_list_item_1,android.R.id.text1,values);
		listview.setAdapter(adapter);
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String itemValue = (String)listview.getItemAtPosition(position);
				Intent intent = new Intent(UpdateActivity.this, UpdateEditActivity.class);
				intent.putExtra("itemName", itemValue);
				startActivity(intent);
				
			}
		});		    
		

		
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
