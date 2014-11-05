package com.example.dict_en_vn.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dict_en_vn.ConfigLib;
import com.example.dict_en_vn.ConstantKey;
import com.example.dict_en_vn.R;
import com.example.dict_en_vn.Utils.MasterDatabaseUtil;
import com.example.dict_en_vn.Utils.PreferenceUtil;
import com.example.dict_en_vn.Utils.Utility;
import com.example.dict_en_vn.activity.adapter.SearchResultAdapter;
import com.example.dict_en_vn.db.dao.DaoMaster;
import com.example.dict_en_vn.db.dao.DaoMaster.DevOpenHelper;
import com.example.dict_en_vn.db.dao.DaoSession;
import com.example.dict_en_vn.db.dao.Note;
import com.example.dict_en_vn.db.dao.NoteDao;
import com.example.dict_en_vn.db.dao.VN_ENDao;
import com.example.dict_en_vn.task.UnzipTask;

public class MainActivity extends FragmentActivity implements OnClickListener, OnFocusChangeListener, OnItemClickListener{
	private ProgressDialog progress;
	private EditText mSearchEdt;
	private String textInput;
	private SearchDataAsyTask mSearchDataAsyTask;
	private DaoMaster daoMaster;
    private DaoSession daoSession;
    private NoteDao noteDao;
    private VN_ENDao vn_ENDao;
    private SQLiteDatabase mDb;
    private Spinner mSpinner;
    private Cursor cursor;
    private SearchResultAdapter mAdapter;
    private Note mNoteSelection;
    boolean isEN_VN = true;
    private static final long SIZE_DATA_EXTERNAL = 215000000;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);
		progress = new ProgressDialog(this);
		progress.setMessage("Processing...");
		progress.setCanceledOnTouchOutside(false);
		progress.setCancelable(false);
		mSearchEdt = (EditText) findViewById(R.id.main_edt);
		findViewById(R.id.main_add).setOnClickListener(this);
		findViewById(R.id.main_edit).setOnClickListener(this);
		mSpinner = (Spinner) findViewById(R.id.main_type_dic_spinner);
		setupSpinner();
//		startFirst();
		if (!PreferenceUtil.getBoolean(this, PreferenceUtil.COPY_DATA)) {
			new MoveFileToSDCard().execute();
		}else {
			init();
		}
		
	}
	private void setupSpinner(){
		List<String> list = Arrays.asList(getResources().getStringArray(R.array.type_arrays));
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(dataAdapter);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (arg2 == 0) {
					isEN_VN = true;
				} else {
					isEN_VN = false;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private void init(){
		mSearchEdt.addTextChangedListener(textWatcher);
		mSearchEdt.setOnFocusChangeListener(MainActivity.this);
		mSearchEdt.clearFocus();
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(MainActivity.this, ConstantKey.DATABASE_NAME, null);
        mDb = helper.getWritableDatabase();
        daoMaster = new DaoMaster(mDb);
        daoSession = daoMaster.newSession();
        noteDao = daoSession.getEN_VNDao();
        vn_ENDao = daoSession.getVN_ENDao();
        mAdapter = new SearchResultAdapter(MainActivity.this);
        ListView listView = (ListView) findViewById(R.id.main_lst);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
	}
//	private void getDao(int dao){
//		if (dao == 0) {
//			
//		} else {
//
//		}
//	}
	private void dialogAddWord(){
		// Creating alert Dialog with one Button
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        //AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("Add new Vocabulary");

        // Setting Dialog Message
        alertDialog.setMessage("Enter Vocabulary");
        final EditText input = new EditText(MainActivity.this);  
		  LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
		                        LinearLayout.LayoutParams.MATCH_PARENT,
		                        LinearLayout.LayoutParams.MATCH_PARENT);
		  input.setLayoutParams(lp);
		  alertDialog.setView(input);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        // Write your code here to execute after dialog
//                        Intent myIntent1 = new Intent(MainActivity.this, Show.class);
//                        startActivityForResult(myIntent1, 0);
                    	String text = input.getText().toString();
                    	if (!text.equals("")) {
                    		
//                    		if (mNoteSelection != null && text.equals(mNoteSelection.getText())) {
                    			editVocablary(text);
//							} else {
//								Note note = new Note(null, text, null, null);
//								Intent myIntent1 = new Intent(MainActivity.this, AddVocabularyActivity.class);
//		                        myIntent1.putExtra(ConstantKey.INTENT_ADD_VOCABULARY, note);
//		                        myIntent1.putExtra(ConstantKey.INTENT_IS_ADD, true);
//		                        startActivity(myIntent1);
//							}
						}
                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });

        // closed

        // Showing Alert Message
        alertDialog.show();
	}
	private void editVocablary(String note){
		Intent myIntent1 = new Intent(MainActivity.this, AddVocabularyActivity.class);
        myIntent1.putExtra(ConstantKey.INTENT_ADD_VOCABULARY, note);
//        myIntent1.putExtra(ConstantKey.INTENT_IS_ADD, false);
		startActivityForResult(myIntent1, 0);
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.main_add:
			dialogAddWord();
			break;
		case R.id.main_edit:
			editVocablary(mNoteSelection.getWord());
			break;

		default:
			break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		mNoteSelection = mAdapter.getListItem().get(arg2);
		mAdapter.getListItem().clear();
		mAdapter.notifyDataSetChanged();
		Intent intent = new Intent(this, DetailActivity.class);
		intent.putExtra(ConstantKey.INTENT_DETAIL_WORD, mNoteSelection);
		intent.putExtra(ConstantKey.INTENT_DETAIL_TYPE_DIC, isEN_VN);
		startActivity(intent);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK && mNoteSelection != null) {
			mNoteSelection = noteDao.getDataWithID(mDb, mNoteSelection.getId());
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mDb != null) {
			mDb.close();
		}
		if (mSearchDataAsyTask != null) {
			mSearchDataAsyTask.cancel(true);
		}
	};
	TextWatcher textWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
				textInput = s.toString().trim();
				if (!textInput.equals("")) {
					if (mSearchDataAsyTask != null) {
						mSearchDataAsyTask.cancel(true);
					}
					mSearchDataAsyTask = new SearchDataAsyTask(textInput,mDb);
					mSearchDataAsyTask.execute();
				}else {
					mAdapter.getListItem().clear();
					mAdapter.notifyDataSetChanged();
				}
				
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			System.out.println("--------------before");
		}
		@Override
		public void afterTextChanged(Editable s) {
			System.out.println("--------------after");
//			mSearchResultList.setVisibility(View.GONE);
		}
	};
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			textInput = mSearchEdt.getText().toString().trim();
			if (!textInput.equals("")) {
				if (mSearchDataAsyTask != null) {
					mSearchDataAsyTask.cancel(true);
				}
				mSearchDataAsyTask = new SearchDataAsyTask(textInput, mDb);
				mSearchDataAsyTask.execute();
			}else {
				mAdapter.getListItem().clear();
				mAdapter.notifyDataSetChanged();
			}
		}
	}
	class SearchDataAsyTask extends AsyncTask<String, Void, List<Note>>{
		String input;
		SQLiteDatabase db;
		public SearchDataAsyTask(String input,SQLiteDatabase db) {
			this.input = input;
			this.db = db;
		}
		@Override
		protected List<Note> doInBackground(String... arg0) {
			if (isEN_VN) {
				return noteDao.getListLimit(db, input);
			}else {
				return vn_ENDao.getListLimit(db, input);
			}
			
		}
		@Override
		protected void onPostExecute(List<Note> result) {
			super.onPostExecute(result);
			mAdapter.getListItem().clear();
			mAdapter.getListItem().addAll(result);
			mAdapter.notifyDataSetChanged();
		}
	}
	class MoveFileToSDCard extends AsyncTask<Void, Void, Boolean>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress.show();
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			
//			return copyAssets();
			return MasterDatabaseUtil.existDatabase(MainActivity.this);
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				PreferenceUtil.setBoolean(MainActivity.this, PreferenceUtil.COPY_DATA, true);
			}
			init();
			progress.dismiss();
			
		}
	}
	private void startFirst() {
//		boolean restoredText =  getPreferences(MODE_PRIVATE).getBoolean(ConfigLib.KEY_START_FIRST, true); 
//		boolean restoredTextV2 =  getPreferences(MODE_PRIVATE).getBoolean(ConfigLib.KEY_START_FIRST_V2, true); 
//		if (!restoredTextV2) {
			checkUnZip();
//		}
		
	}
	private void openDialogFullStorage() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage("full memory"); 
		alert.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				dialog.cancel();
			}
		});
		AlertDialog alertDialog = alert.create();
		alertDialog.show();
	}
	 private void checkUnZip()
	    {
		 if (Utility.getAvailableExternalMemorySize() < SIZE_DATA_EXTERNAL) {
				openDialogFullStorage();
			}else {
				if (!new File(ConfigLib.SDCARD_FOLDER +ConfigLib.ASSETS_DATA+"/"+ConfigLib.DIC_NAME+ConfigLib.DATABASE_TYPE_FILE).exists()) {
					new UnzipTask(this, ConfigLib.ASSETS_DATA + ConfigLib.DIC_TYPE_FILE).execute();
				}
			}
	    }
}
