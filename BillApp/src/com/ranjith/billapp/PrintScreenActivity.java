package com.ranjith.billapp;

import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
public class PrintScreenActivity extends ActionBarActivity {

	LinearLayout container;
	Button print;
	Spinner spinner;
	TextView display_prev_credit,display_balance,display_vat,display_total;
	EditText enterd_discount,enterd_amt_paying;
	DataHandler handler;
	String storeName,unit_price;
	String [] items,prices,vats,qtys;
	Float totalWithOutDiscount=0.0f,totalVat=0.0f,discoutPercent=0.0f,discountRupees=0.0f,balance;
	int i=0;
	int length=0;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print_screen);

		container = (LinearLayout) findViewById(R.id.display_bill);
		handler = new DataHandler(getBaseContext());
		spinner = (Spinner) findViewById(R.id.spinner1);
		print = (Button) findViewById(R.id.print_bill);
		display_vat = (TextView) findViewById(R.id.total_vat);
		display_total = (TextView) findViewById(R.id.total);
		display_prev_credit = (TextView) findViewById(R.id.prev_credit);
		display_balance = (TextView) findViewById(R.id.balance);
		enterd_discount = (EditText) findViewById(R.id.discount);
		enterd_amt_paying = (EditText) findViewById(R.id.amt_paying);



		handler.open();
		Cursor crs = handler.returnStore();
		String[] labels = handler.getStores(crs);
		handler.close();

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
		spinner.setAdapter(dataAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				storeName = parent.getItemAtPosition(position).toString();


			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		items = new String [100];
		prices = new String [100];
		qtys = new String [100];
		vats = new String [100];

		items = getIntent().getStringArrayExtra("itemName");
		prices = getIntent().getStringArrayExtra("price_with_vat");
		qtys = getIntent().getStringArrayExtra("itemquantity");
		vats = getIntent().getStringArrayExtra("vat_value");
		length = getIntent().getIntExtra("count", 0);


		for(i=0;i<length;i++){

			handler.open();
			Cursor cursor = handler.getItemByName(items[i]);
			if(cursor.moveToFirst()){
				do{
					unit_price = cursor.getString(1);
				}while(cursor.moveToNext());
			}

			LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View addview = layoutInflater.inflate(R.layout.row_print, null);
			TextView itemName = (TextView) addview.findViewById(R.id.text_name);
			TextView itemqty = (TextView) addview.findViewById(R.id.text_qty);
			TextView itemprice = (TextView) addview.findViewById(R.id.text_price);



			itemName.setText(items[i]);
			itemqty.setText(qtys[i]);
			itemprice.setText(prices[i]);


			container.addView(addview);
			totalWithOutDiscount = totalWithOutDiscount+Float.parseFloat(prices[i]);
			totalVat= totalVat+Float.parseFloat(vats[i]);

		}

		display_vat.setText(String.valueOf(totalVat));
		display_total.setText(String.valueOf(totalWithOutDiscount));
		display_balance.setText(String.valueOf(totalWithOutDiscount));
		display_prev_credit.setText("0.0");

		enterd_discount.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				CharSequence enterd_value = s;
				Float disc = Float.parseFloat(String.valueOf(enterd_value));
				balance = totalWithOutDiscount-(totalWithOutDiscount*(disc/100));
				display_balance.setText(String.valueOf(balance));
				discountRupees = (totalWithOutDiscount)*(disc/100);

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
		enterd_amt_paying.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				CharSequence enterd_amnt_value =s;
				if(String.valueOf(enterd_amnt_value).equals(display_balance.getText().toString())){
					display_prev_credit.setText("0.0");
				}
				else{
					Float credit = Float.parseFloat(display_balance.getText().toString())-Float.parseFloat(String.valueOf(enterd_amnt_value));
					display_prev_credit.setText(String.valueOf(credit));
				}

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
		print.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent sendData = new Intent(PrintScreenActivity.this,BluetoothActivity.class);
				sendData.putExtra("storeName", storeName);
				sendData.putExtra("items", items);
				sendData.putExtra("price", prices);
				sendData.putExtra("quantity", qtys);
				sendData.putExtra("total_vat", String.valueOf(totalVat));
				sendData.putExtra("total_amount", String.valueOf(totalWithOutDiscount));
				sendData.putExtra("discount", String.valueOf(discountRupees));
				sendData.putExtra("amount_paying",display_balance.getText().toString());
				sendData.putExtra("previous_credit",display_prev_credit.getText().toString());
				sendData.putExtra("length_array", length);
				startActivity(sendData);
			}
		});

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.print_screen, menu);
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
