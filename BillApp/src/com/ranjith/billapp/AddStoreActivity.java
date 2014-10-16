package com.ranjith.billapp;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddStoreActivity extends ActionBarActivity {

	Button addStore;
	EditText storeName,storeLocation,storePhone;
	DataHandler handler;
	String getName,getLocation,getPhone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_store);
		
		addStore = (Button)findViewById(R.id.button1);
		storeName = (EditText)findViewById(R.id.editText1);
		storeLocation = (EditText)findViewById(R.id.editText2);
		storePhone = (EditText)findViewById(R.id.editText3);
		
		addStore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				getName = storeName.getText().toString();
				getLocation = storeLocation.getText().toString();
				getPhone = storePhone.getText().toString();
				if(getName!=null && getName.length()>0)
				{
				storeName.setText("");
				storeLocation.setText("");
				storePhone.setText("");
				
				//int phone = Integer.parseInt(getPhone);
				
				handler = new DataHandler(getBaseContext());
				handler.open();
				long id = handler.insertStore(getName, getLocation, getPhone);
				Toast.makeText(getBaseContext(),getName+" store added to your device!", Toast.LENGTH_LONG).show();
				handler.close();
				Intent jumpback = new Intent(AddStoreActivity.this,FrontActivity.class);
				startActivity(jumpback);
				}
				else
				{
					Toast.makeText(getBaseContext(), "Please Enter a Store name to add", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_store, menu);
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
