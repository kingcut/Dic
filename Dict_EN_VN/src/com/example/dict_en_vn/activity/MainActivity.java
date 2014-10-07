package com.example.dict_en_vn.activity;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dict_en_vn.ConstantKey;
import com.example.dict_en_vn.R;
import com.example.dict_en_vn.Utils.MasterDatabaseUtil;
import com.example.dict_en_vn.Utils.PreferenceUtil;
import com.example.dict_en_vn.activity.adapter.SearchResultAdapter;
import com.example.dict_en_vn.db.dao.DaoMaster;
import com.example.dict_en_vn.db.dao.DaoMaster.DevOpenHelper;
import com.example.dict_en_vn.db.dao.DaoSession;
import com.example.dict_en_vn.db.dao.Note;
import com.example.dict_en_vn.db.dao.NoteDao;

public class MainActivity extends FragmentActivity implements OnClickListener, OnFocusChangeListener, OnItemClickListener{
	private ProgressDialog progress;
	private EditText mSearchEdt;
	private String textInput;
	private TextView mContentTxt;
	private SearchDataAsyTask mSearchDataAsyTask;
	private DaoMaster daoMaster;
    private DaoSession daoSession;
    private NoteDao noteDao;
    private SQLiteDatabase mDb;
    private Cursor cursor;
    private SearchResultAdapter mAdapter;
    private Note mNoteSelection;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);
		progress = new ProgressDialog(this);
		progress.setMessage("Processing...");
		progress.setCanceledOnTouchOutside(false);
		progress.setCancelable(false);
		mSearchEdt = (EditText) findViewById(R.id.main_edt);
		mContentTxt = (TextView) findViewById(R.id.main_content);
		findViewById(R.id.main_add).setOnClickListener(this);
		findViewById(R.id.main_edit).setOnClickListener(this);
		if (!PreferenceUtil.getBoolean(this, PreferenceUtil.COPY_DATA)) {
			new MoveFileToSDCard().execute();
		}else {
			init();
		}
		
	}
	private void init(){
		mSearchEdt.addTextChangedListener(textWatcher);
		mSearchEdt.setOnFocusChangeListener(MainActivity.this);
		mSearchEdt.clearFocus();
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(MainActivity.this, "notes-db", null);
        mDb = helper.getWritableDatabase();
        daoMaster = new DaoMaster(mDb);
        daoSession = daoMaster.newSession();
        noteDao = daoSession.getNoteDao();
        mAdapter = new SearchResultAdapter(MainActivity.this);
        ListView listView = (ListView) findViewById(R.id.main_lst);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
	}
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
			editVocablary(mNoteSelection.getText());
			break;

		default:
			break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		mNoteSelection = mAdapter.getListItem().get(arg2);
		mContentTxt.setText(mNoteSelection.getText() + "\n" +mNoteSelection.getComment());
		mAdapter.getListItem().clear();
		mAdapter.notifyDataSetChanged();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK && mNoteSelection != null) {
			mNoteSelection = noteDao.getDataWithID(mDb, mNoteSelection.getId());
			mContentTxt.setText(mNoteSelection.getText() + "\n" +mNoteSelection.getComment());
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
			return noteDao.getListLimit(db, input);
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
}
