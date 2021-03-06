package com.example.dict_en_vn.activity;

import com.example.dict_en_vn.ConstantKey;
import com.example.dict_en_vn.R;
import com.example.dict_en_vn.activity.adapter.SearchResultAdapter;
import com.example.dict_en_vn.db.dao.DaoMaster;
import com.example.dict_en_vn.db.dao.DaoSession;
import com.example.dict_en_vn.db.dao.Note;
import com.example.dict_en_vn.db.dao.NoteDao;
import com.example.dict_en_vn.db.dao.DaoMaster.DevOpenHelper;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
public class AddVocabularyActivity extends FragmentActivity implements OnClickListener{
	private EditText mEdtWord;
	private EditText mEdtContent;
	private DaoMaster daoMaster;
    private DaoSession daoSession;
    private NoteDao noteDao;
    private SQLiteDatabase mDb;
    private Note note;
    private boolean isAdd;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_add_vocabulary);
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ConstantKey.DATABASE_NAME, null);
        mDb = helper.getWritableDatabase();
        daoMaster = new DaoMaster(mDb);
        daoSession = daoMaster.newSession();
        noteDao = daoSession.getEN_VNDao();
		Bundle bundle = getIntent().getExtras();
		
		String text = bundle.getString(ConstantKey.INTENT_ADD_VOCABULARY);
		note = noteDao.getDataWithValue(mDb, NoteDao.Properties.Word.columnName, text);
		if (note == null) {
			note = new Note(null, text, null, null, null, null, null,null);
		}
//		isAdd = bundle.getBoolean(ConstantKey.INTENT_IS_ADD);
		Button button = (Button) findViewById(R.id.add_ok);
		button.setOnClickListener(this);
//		if (isAdd) {
//			button.setText("Add");
//		} else {
//			button.setText("Edit");
//		}
		mEdtWord = (EditText) findViewById(R.id.add_vocabulary_word);
		mEdtContent = (EditText) findViewById(R.id.add_vocabulary_content);
		if (note.getWord() != null) {
			mEdtWord.setText(note.getWord());
		}
		if (note.getContent() != null) {
			mEdtContent.setText(note.getContent());
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_ok:
			Note temp = new Note((long)5, mEdtWord.getText().toString(), mEdtContent.getText().toString(), "a", "b", "c", (long)1,(long) 1);
			note = noteDao.getDataWithValue(mDb, NoteDao.Properties.Word.columnName, temp.getWord());
			if (note == null) {
				note = temp;
				noteDao.insert(note);
			} else {
				note.setContent(temp.getContent());
				noteDao.update(note);
			}
			Bundle bundle = new Bundle();
			bundle.putSerializable(ConstantKey.INTENT_RESULT_DATA, note);
			Intent intent = new  Intent();
			intent.putExtras(bundle);
			setResult(RESULT_OK,intent);
			finish();
			break;

		default:
			break;
		}
		
	}
}
