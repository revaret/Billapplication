package com.ranjith.billapp;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateEditActivity extends ActionBarActivity {

	Button update;
	TextView c_rate;
	EditText up_rate;
	DataHandler handler;
	String itemName,itemRate,itemuprate;
	Float price;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_edit);
		
		update = (Button)findViewById(R.id.button1);
		c_rate = (TextView)findViewById(R.id.earlyrate);
		up_rate= (EditText)findViewById(R.id.editText1);
		
		itemName = getIntent().getExtras().getString("itemName");
		
		
		handler = new DataHandler(getBaseContext());
		handler.open();
		Cursor cursor = handler.getItemByName(itemName);
		if (cursor.moveToFirst()) {
			
			do
			{
				itemRate = cursor.getString(1);
			}while(cursor.moveToNext());
		}
		
		c_rate.setText(itemRate + " Rs/kg");
		
		
		
		
		update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				itemuprate = up_rate.getText().toString();
				if(itemuprate!=null && itemuprate.length()>0)
				{
				price = Float.valueOf(itemuprate);
				int id = handler.updateByName(itemName,price);
				Toast.makeText(getBaseContext(),itemName+"'s rate updated successfully!", Toast.LENGTH_LONG).show();
				handler.close();
				Intent jumpback = new Intent(UpdateEditActivity.this,UpdateActivity.class);
				startActivity(jumpback);
				}
				else
				{
					Toast.makeText(getBaseContext(), "Please provide a value to be updated!", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update_edit, menu);
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
