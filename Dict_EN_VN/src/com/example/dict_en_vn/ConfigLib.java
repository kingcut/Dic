package com.example.dict_en_vn;

import android.os.Environment;

public class ConfigLib {
	public final static String SDCARD_FOLDER  = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Viet Dic/";
	public final static String DIC_NAME = "dic_en";
	public final static String DIC_TYPE_FILE = ".zip";
	public final static String DIC_FUNTION_NAME = "dic_funtion";
	public final static String DATABASE_TYPE_FILE = ".db";
	public final static String ASSETS_DATA = "DicData";
	
	public final static int TYPE_GET_DATA_ONLY_FIRST = 1;
	public final static int TYPE_GET_DATA_ALL= 2;
	public final static int TYPE_INSERT_FAVORITE= 3;
	public final static int TYPE_GET_DATA_FAVORITE= 4;
	public final static int TYPE_DELETE_DATA_FAVORITE= 5;
	public final static int TYPE_DELETE_DATA_FAVORITE_ALL= 6;
	public final static int TYPE_INSERT_DATA_TEST= 7;
	public final static int TYPE_INSERT_DATA_TEST_LIST= 8;
	public final static int TYPE_DELETE_TABLE= 9;
	public final static int TYPE_INSERT_MEMO= 10;
	public final static int TYPE_GET_MEMO= 11;
	// name data dic rename
	// key change type data

	
	//test
	public final static String TABLE_TEST = "test_";
	public final static String KEY_START_FIRST = "start_first";
	public final static String KEY_START_FIRST_V2 = "start_first_v2";
	
	public final static String KEY_LANGUAGE_DEFAUFT = "language_default";
}
