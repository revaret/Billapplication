package com.ranjith.billapp;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


public class FrontActivity extends ActionBarActivity {
	Button pbill,upval,additem,addstore;
	ImageView exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);
        //Log.d("LOGTAG", "oncreate");
        boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
        if (firstrun){
        Intent pop = new Intent(FrontActivity.this,RegisterActivity.class);
        getSharedPreferences("PREFERENCE", MODE_PRIVATE)
            .edit()
            .putBoolean("firstrun", false)
            .commit();
        startActivity(pop);
        }
         pbill = (Button)findViewById(R.id.button1);
         upval = (Button)findViewById(R.id.button2);
         additem = (Button)findViewById(R.id.button3);
         addstore = (Button)findViewById(R.id.button4);
         exit = (ImageView)findViewById(R.id.exit_app);
         
        
       pbill.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			Intent tobill = new Intent(FrontActivity.this,BillActivity.class);
			startActivity(tobill);
			
		}
	});
       
       upval.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			Intent toupdate = new Intent(FrontActivity.this,UpdateActivity.class);
			startActivity(toupdate);
			
		}
	});
       additem.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent toadditem = new Intent(FrontActivity.this,AddItemActivity.class);
			startActivity(toadditem);
			
		}
	});
       addstore.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent toaddstore = new Intent(FrontActivity.this,AddStoreActivity.class);
			startActivity(toaddstore);
			
		}
	});
       
       exit.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			
		}
	});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.layout.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()) {
	case R.id.menu_user_profile:
								Intent intent = new Intent(FrontActivity.this,RegEditActivity.class);
								startActivity(intent);
								return true;
	default:return super.onOptionsItemSelected(item);
			}
        
    }
}
