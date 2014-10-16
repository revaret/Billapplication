package com.ranjith.billapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

import android.support.v7.app.ActionBarActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class BluetoothActivity extends ActionBarActivity {

	ImageView connect_device,print_bill,close_connection;
	DataHandler dbhandler;
	int length,billno;
	String agency,address,agentName,mobile,tinNo,total_vat,total,discount,balance,previous_credit,store_name,bill;
	String[] itemNames,itemQtys,itemPrices;

	BluetoothAdapter mBluetoothAdapter;
	BluetoothSocket mmSocket;
	BluetoothDevice mmDevice;

	OutputStream mmOutputStream;
	InputStream mmInputStream;
	Thread workerThread;

	byte[] readBuffer;
	int readBufferPosition;
	volatile boolean stopWorker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		
		SharedPreferences pref = this.getSharedPreferences("share",Context.MODE_PRIVATE);
		int count = pref.getInt("your key", 200); 
		count++;
		billno = 200+count;
		SharedPreferences.Editor edit = pref.edit();
		edit.putInt("your key", count);
		edit.commit();

		try{
			
			connect_device = (ImageView) findViewById(R.id.connect);
			print_bill = (ImageView) findViewById(R.id.print);
			close_connection = (ImageView) findViewById(R.id.close);

			connect_device.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						findBT();
						openBT();
					} catch (IOException ex) {
					}

				}
			});

			print_bill.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						sendData();
					} catch (IOException ex) {
					}

				}
			});

			close_connection.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						closeBT();
					} catch (IOException ex) {
					}

				}
			});
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	void findBT() {

		try {
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

			if (mBluetoothAdapter == null) {
				Toast.makeText(getBaseContext(), "No bluetooth adapter available!", Toast.LENGTH_LONG).show();
			}

			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableBluetooth = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBluetooth, 0);
			}

			Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
					.getBondedDevices();
			if (pairedDevices.size() > 0) {
				for (BluetoothDevice device : pairedDevices) {

					// MP300 is the name of the bluetooth printer device
					if (device.getName().equals("BlueTooth Printer")) {
						mmDevice = device;
						break;
					}
				}
			}
			Toast.makeText(getBaseContext(), "Bluetooth Device Found!", Toast.LENGTH_LONG).show();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void openBT() throws IOException {
		try {
			// Standard SerialPortService ID
			UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
			mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
			mmSocket.connect();
			mmOutputStream = mmSocket.getOutputStream();
			mmInputStream = mmSocket.getInputStream();

			beginListenForData();

			Toast.makeText(getBaseContext(), "Bluetooth Opened!", Toast.LENGTH_LONG).show();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void beginListenForData() {
		try {
			final Handler handler = new Handler();

			// This is the ASCII code for a newline character
			final byte delimiter = 10;

			stopWorker = false;
			readBufferPosition = 0;
			readBuffer = new byte[1024];

			workerThread = new Thread(new Runnable() {
				public void run() {
					while (!Thread.currentThread().isInterrupted()
							&& !stopWorker) {

						try {

							int bytesAvailable = mmInputStream.available();
							if (bytesAvailable > 0) {
								byte[] packetBytes = new byte[bytesAvailable];
								mmInputStream.read(packetBytes);
								for (int i = 0; i < bytesAvailable; i++) {
									byte b = packetBytes[i];
									if (b == delimiter) {
										byte[] encodedBytes = new byte[readBufferPosition];
										System.arraycopy(readBuffer, 0,
												encodedBytes, 0,
												encodedBytes.length);
										final String data = new String(
												encodedBytes, "US-ASCII");
										readBufferPosition = 0;

										handler.post(new Runnable() {
											public void run() {
												Toast.makeText(getBaseContext(), data, Toast.LENGTH_LONG).show();
											}
										});
									} else {
										readBuffer[readBufferPosition++] = b;
									}
								}
							}

						} catch (IOException ex) {
							stopWorker = true;
						}

					}
				}
			});

			workerThread.start();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void sendData() throws IOException {
		try {
			dbhandler = new DataHandler(getBaseContext());
			String[] itemNames = new String[100];
			String[] itemQtys = new String[100];
			String[] itemPrices = new String[100];
			
			dbhandler.open();
			Cursor crs = dbhandler.returnData();
			if(crs.moveToFirst())
			{
				do{
					agentName = crs.getString(0);
					mobile = crs.getString(1);
					address = crs.getString(2);
					agency = crs.getString(3);
					tinNo = crs.getString(4);
				}while(crs.moveToNext());
			}
			
			length = getIntent().getIntExtra("length_array", 0);
			total_vat = getIntent().getStringExtra("total_vat");
			store_name = getIntent().getStringExtra("storeName");
			total = getIntent().getStringExtra("total_amount");
			discount = getIntent().getStringExtra("discount");
			balance = getIntent().getStringExtra("amount_paying");
			previous_credit = getIntent().getStringExtra("previous_credit");
			itemNames = getIntent().getStringArrayExtra("items");
			itemQtys = getIntent().getStringArrayExtra("quantity");
			itemPrices = getIntent().getStringArrayExtra("price");
			Calendar c = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String formattedDate = df.format(c.getTime());

			// the text typed by the user
			 bill = "          "+agency+"\n";
			 bill = bill +address+"\n\n";
			 bill = bill+"Bill Number :"+billno+"\n";
			 bill = bill+"Date :"+formattedDate+"\n";
			 bill = bill+"Name :"+store_name+"\n";
			 bill = bill+"--------------------------------\n";
			 bill = bill+"Item           Qty    Price\n";
			 bill = bill+"--------------------------------\n";
			 for(int i=0;i<length;i++)
			 {
				 bill = bill+itemNames[i]+"                  "+itemQtys[i]+"    "+itemPrices[i]+"\n";
				 
			 }
			 bill = bill+"\n";
			 bill = bill+"VAT :"+"               "+total_vat+"\n";
			 bill = bill+"Total Amount :"+"          "+total+"\n";
			 bill = bill+"Discount :"+"           "+discount+"\n";
			 bill = bill+"Amount Paying :"+"         "+balance+"\n";
			 bill = bill+"Previous Credit :"+"          "+previous_credit+"\n";
			 bill = bill+"A/c Balance :"+"              "+"0.0\n";
			 bill = bill+"\n\n\n";
			 bill = bill+"--------------------------------\n";
			 bill = bill+"Agent Name:"+agentName+"\n";
			 bill = bill+"Mob:"+mobile+"\n";
			 bill = bill+"TIN NO:"+tinNo+"\n";
			 bill = bill+"      'Computer Generated Bill'\n";
			 bill = bill+"      Powered By accupa.com\n";
			 bill = bill+"--------------------------------\n";
			 
			 
			 
			 
			

			mmOutputStream.write(bill.getBytes());

			// tell the user data were sent
			Toast.makeText(getBaseContext(), "Printing...", Toast.LENGTH_LONG).show();

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void closeBT() throws IOException {
		try {
			stopWorker = true;
			mmOutputStream.close();
			mmInputStream.close();
			mmSocket.close();
			Toast.makeText(getBaseContext(), "Bluetooth closed!", Toast.LENGTH_LONG).show();
			Intent jumpback = new Intent(BluetoothActivity.this,BillActivity.class);
			startActivity(jumpback);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bluetooth, menu);
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
