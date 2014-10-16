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

public class RegEditActivity extends ActionBarActivity {

	Button update ;
	EditText editAdress,editPhone,editAgency,editTin;
	TextView name;
	DataHandler handler;
	int upTin;
	String getName,getPhone,getAdress,upphone,upadress,getAgency,getTin,upAgency;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg_edit);
		
		update = (Button)findViewById(R.id.button1);
		editPhone =(EditText)findViewById(R.id.phone);
		editAdress = (EditText)findViewById(R.id.adress);
		editAgency = (EditText) findViewById(R.id.agency_edit);
		editTin = (EditText) findViewById(R.id.tin_number);
		name = (TextView)findViewById(R.id.name);
		handler = new DataHandler(getBaseContext());
		handler.open();
		Cursor cursor = handler.returnData();
		if(cursor.moveToFirst()){
			do{
				getName = cursor.getString(0);
				getPhone = cursor.getString(1);
				getAdress = cursor.getString(2);
				getAgency = cursor.getString(3);
				getTin = cursor.getString(4);
			}while(cursor.moveToNext());
		}
		name.setText(getName);
		editPhone.setText(getPhone);
		editAdress.setText(getAdress);
		editAgency.setText(getAgency);
		editTin.setText(getTin);
		
		
		update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				upphone = editPhone.getText().toString();
				upadress = editAdress.getText().toString();
				upAgency = editAgency.getText().toString();
				upTin = Integer.valueOf(editTin.getText().toString());
				int id = handler.updateUserByName(getName, upphone, upadress,upAgency,upTin);
				Toast.makeText(getBaseContext(),getName+" updated successfully!", Toast.LENGTH_LONG).show();
				Intent jumpback = new Intent(RegEditActivity.this,FrontActivity.class);
				startActivity(jumpback);
				handler.close();
				
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reg_edit, menu);
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
