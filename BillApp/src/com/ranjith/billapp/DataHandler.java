package com.ranjith.billapp;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DataHandler {

	public static final  String NAME = "name";
	public static final String PHONE = "phone";
	public static final String ADRESS = "adress";
	public static final String AGENCY="agency";
	public static final String TIN="tin";
	public static final  String ITEMNAME = "itemname";
	public static final  String ITEMPRICE = "itemprice";
	public static final  String ITEMVAT = "itemvat";
	public static final String STORENAME="storename";
	public static final String STORELOCATION="storelocation";
	public static final String STOREPHONE ="storephone";
	public static final String TABLE_NAME_ITEM = "itemtable";
	public static final String TABLE_NAME = "usertable";
	public static final String TABLE_STORE="storetable";
	public static final String DATA_BASE_NAME ="billdb";
	public static final int DATA_BASE_VERSION = 1;
	public static final String TABLE_CREATE ="create table usertable(name text not null,phone text not null,adress text not null,agency text not null,tin text not null);";
	public static final String TABLE_CREATE_ITEM ="create table itemtable(itemname text not null,itemprice real not null,itemvat real not null);";
	public static final String TABLE_CREATE_STORE = "create table storetable(storename text not null,storelocation text not null,storephone int not null);";
	
	DataBaseHelper dbhelper;
	Context ctx;
	SQLiteDatabase db;
	public DataHandler(Context ctx){
		
		this.ctx = ctx;
		dbhelper = new DataBaseHelper(ctx);
	}
	
	private static class DataBaseHelper extends SQLiteOpenHelper{

		
		public DataBaseHelper(Context ctx){
			super(ctx, DATA_BASE_NAME, null, DATA_BASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(TABLE_CREATE);
				db.execSQL(TABLE_CREATE_ITEM);
				db.execSQL(TABLE_CREATE_STORE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS usertable");
			db.execSQL("DROP TABLE IF EXISTS itemtable");
			db.execSQL("DROP TABLE IF EXISTS storetable");
			onCreate(db);
			
		}
		
	}
	
	public DataHandler open()
	{
		db =dbhelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		dbhelper.close();
	}
	
	public long insertData(String name,String phone,String adress,String agency,String tinNum){
		
		ContentValues content = new ContentValues();
		content.put(NAME, name);
		content.put(PHONE, phone);
		content.put(ADRESS, adress);
		content.put(AGENCY, agency);
		content.put(TIN,tinNum);
		return db.insert(TABLE_NAME, null, content);
	
	}
	
public long insertItem(String name,Float price,Float vat){
		
		ContentValues content = new ContentValues();
		content.put(ITEMNAME, name);
		content.put(ITEMPRICE, price);
		content.put(ITEMVAT, vat);
		return db.insert(TABLE_NAME_ITEM, null, content);
		
	}

public long insertStore(String name,String location,String phone){
	
	ContentValues cv = new ContentValues();
	cv.put(STORENAME, name);
	cv.put(STORELOCATION, location);
	cv.put(STOREPHONE, phone);
	
	return db.insert(TABLE_STORE, null, cv);
}
	
	public Cursor returnData(){
		return db.query(TABLE_NAME, new String[]{NAME,PHONE,ADRESS,AGENCY,TIN}, null, null, null, null, null);
	}
	
	public Cursor returnItem(){
		return db.query(TABLE_NAME_ITEM,new String[]{ITEMNAME,ITEMPRICE,ITEMVAT},null,null, null,null,null);
	}
	public Cursor returnStore(){
		return db.query(TABLE_STORE, new String[]{STORENAME, STORELOCATION,STOREPHONE}, null, null, null, null,null);
	}
	public String[] getItems(Cursor crs){
		String[] array = new String[crs.getCount()];
		int i = 0;
		while(crs.moveToNext()){
		    String uname = crs.getString(crs.getColumnIndex(ITEMNAME));
		    array[i] = uname;
		    i++;
		}
	    return array;
	}
	
	public String[] getStores(Cursor crs){
		String[] array = new String[crs.getCount()];
		int i = 0;
		while(crs.moveToNext()){
		    String uname = crs.getString(crs.getColumnIndex(STORENAME));
		    array[i] = uname;
		    i++;
		}
	    return array;
	}
	
	public Cursor getItemByName(String name){
		return db.query(TABLE_NAME_ITEM, new String[]{ITEMNAME,ITEMPRICE,ITEMVAT},ITEMNAME+"=?", new String[]{name}, null, null, null);
		
	}
	
	public int updateByName(String name,Float price)
	{
		ContentValues cv = new ContentValues();
		String where = ITEMNAME + "= ?";
		cv.put(ITEMPRICE, price);
		return db.update(TABLE_NAME_ITEM, cv,where, new String[] {name});
	}
	
	public int updateUserByName(String name,String phone,String adress, String agency,Integer tin){
		
		ContentValues cv = new ContentValues();
		String where = NAME+"= ?";
		cv.put(PHONE, phone);
		cv.put(ADRESS, adress);
		cv.put(AGENCY, agency);
		cv.put(TIN, tin);
		
		return db.update(TABLE_NAME, cv, where, new String[] {name});
				
		
	}
	
	
}
