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

public class RegisterActivity extends ActionBarActivity {

	EditText name,phone,adress,agency_name,tin_n;
	Button submit;
	DataHandler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		 name = (EditText)findViewById(R.id.editText1);
		 phone = (EditText)findViewById(R.id.editText2);
		 adress = (EditText)findViewById(R.id.editText3);
		 agency_name = (EditText)findViewById(R.id.agency);
		 tin_n = (EditText) findViewById(R.id.tin_number);
		 submit = (Button)findViewById(R.id.button1);
		submit.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				String getName = name.getText().toString();
				String getPhone = phone.getText().toString();
				String getAdress = adress.getText().toString();
				String getAgency = agency_name.getText().toString();
				String getTin = tin_n.getText().toString();
				handler = new DataHandler(getBaseContext());
				handler.open();
				long id = handler.insertData(getName,getPhone, getAdress,getAgency,getTin);
				Toast.makeText(getBaseContext(), "Registerd Successfully!", Toast.LENGTH_LONG).show();
				handler.close();
				Intent jumpback = new Intent(RegisterActivity.this,FrontActivity.class);
				startActivity(jumpback);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
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
