package com.ranjith.billapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddItemActivity extends ActionBarActivity {
	Button additem;
	EditText itemname,itemprice,itemvat;
	String getitemname,getitemprice,getitemvat;
	DataHandler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);
		
		additem = (Button) findViewById(R.id.button1);
		itemname = (EditText) findViewById(R.id.editText1);
		itemprice = (EditText) findViewById(R.id.editText2);
		itemvat = (EditText) findViewById(R.id.editText3);
		
		additem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getitemname = itemname.getText().toString();
				getitemprice = itemprice.getText().toString();
				getitemvat = itemvat.getText().toString();
				if((getitemname!=null && getitemname.length()>0)&&(getitemprice!=null && getitemprice.length()>0)&&(getitemvat!=null && getitemvat.length()>0)){
				itemname.setText("");
				itemprice.setText("");
				itemvat.setText("");
				float price = Float.parseFloat(getitemprice);
				float vat = Float.parseFloat(getitemvat);
				handler = new DataHandler(getBaseContext());
				handler.open();
				long id = handler.insertItem(getitemname, price, vat);
				Toast.makeText(getBaseContext(), getitemname+" added !",Toast.LENGTH_LONG).show();
				handler.close();
				}
				else{
					Toast.makeText(getBaseContext(), "Please enter all the fields!", Toast.LENGTH_LONG).show();
				}
				
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_item, menu);
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
