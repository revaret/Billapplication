package com.ranjith.billapp;

import java.lang.Character.UnicodeBlock;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams") public class BillActivity extends Activity {

	DataHandler handler;
	ListView listview;
	EditText search,qty;
	Button add;
	Button submit;
	ArrayAdapter<String> adapter;
	Float unit_price,unit_vat;
	String item_name,getItem,getQty;
	LinearLayout container;
	String[] individualItems,qtys,prices,vats;
	int i=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill);

		//linking
		handler = new DataHandler(getBaseContext());
		listview = (ListView) findViewById(R.id.list);
		add = (Button) findViewById(R.id.add_to_bill);
		search = (EditText) findViewById(R.id.editText1);
		qty = (EditText) findViewById(R.id.editText2);
		submit = (Button) findViewById(R.id.submit_bill);
		ArrayList<HashMap<String, String>> productList;
		container = (LinearLayout) findViewById(R.id.billshow);
		individualItems = new String[100];
		qtys = new String[100];
		prices = new String[100];
		vats = new String[100];


		//getting items
		handler.open();
		Cursor cursor = handler.returnItem();
		String[] items = handler.getItems(cursor);
		handler.close();

		//setting adapter
		adapter = new ArrayAdapter<String>(this,
				R.layout.lis_item,R.id.product_name,items);
		listview.setAdapter(adapter);

		//search functionality
		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				BillActivity.this.adapter.getFilter().filter(s);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		//listview
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String itemName = (String) listview.getItemAtPosition(position);
				search.setText(itemName);

			}

		});

		//addbutton onclick

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				getItem = search.getText().toString();
				getQty = qty.getText().toString();
				//String display_qty = getQty;
				if((getQty != null && getQty.length()>0) && (getItem!= null && getItem.length()>0))
				{


				Float calc_Qty = Float.parseFloat(getQty);

				handler.open();
				Cursor cursor = handler.getItemByName(getItem);
				if(cursor.moveToFirst()){
					do{
						item_name = cursor.getString(0);
						unit_price = Float.parseFloat(cursor.getString(1));
						unit_vat = Float.parseFloat(cursor.getString(2));
					}while(cursor.moveToNext());
				}
				//calculations
				Float qty_price = (calc_Qty)*(unit_price);
				Float qty_vat = (qty_price)*(unit_vat/100);

				final Float individual_price = qty_price+qty_vat;
				String indi_price = Float.toString(individual_price);

				LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final View addview = layoutInflater.inflate(R.layout.row, null);
				TextView name = (TextView) addview.findViewById(R.id.name);
				TextView price = (TextView) addview.findViewById(R.id.price);
				TextView quanti = (TextView) addview.findViewById(R.id.quantity);
				name.setText(getItem);
				price.setText(indi_price);
				//Log.d("quantity",getQty);
				quanti.setText(getQty);
				individualItems[i] = item_name;
				qtys[i] = getQty;
				prices[i]= indi_price;
				String vat_string = Float.toString(qty_vat);
				vats[i] = vat_string;
				i++;
				ImageView remove = (ImageView) addview.findViewById(R.id.remove);		
				remove.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						((LinearLayout)addview.getParent()).removeView(addview);
						i=i-1;
						Toast.makeText(getBaseContext(),getItem+" removed!", Toast.LENGTH_LONG).show();

					}});
				search.setText("");
				qty.setText("");
				container.addView(addview);

			}
			else{
				Toast.makeText(getBaseContext(), "Please Enter the Item/Quantity", Toast.LENGTH_LONG).show();
				
				
			}
		}});
		
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent carryData = new Intent(BillActivity.this,PrintScreenActivity.class);
				carryData.putExtra("itemName", individualItems);
				carryData.putExtra("itemquantity", qtys);
				carryData.putExtra("price_with_vat", prices);
				carryData.putExtra("vat_value", vats);
				carryData.putExtra("count", i);
				startActivity(carryData);
			}
		});

		


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bill, menu);
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
