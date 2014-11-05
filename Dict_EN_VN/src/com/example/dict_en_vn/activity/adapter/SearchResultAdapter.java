package com.example.dict_en_vn.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dict_en_vn.R;
import com.example.dict_en_vn.db.dao.Note;

public class SearchResultAdapter extends BaseAdapter{
	private Activity mActivity;
	private List<Note> mList = new ArrayList<Note>();
	public SearchResultAdapter(Activity activity) {
		this.mActivity = activity;
	}
	public List<Note> getListItem(){
		return mList;
	}
	@Override
	public int getCount() {
		return mList.size();
	}
	@Override
	public Object getItem(int arg0) {
		return null;
	}
	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		Note note = mList.get(position);
		Holder holder = null;
		if (view == null) {
			view = LayoutInflater.from(mActivity).inflate(R.layout.search_list_result_item, null);
			holder = new Holder();
			holder.word = (TextView) view.findViewById(R.id.search_list_result_item_word);
			holder.content = (TextView) view.findViewById(R.id.search_list_result_item_content);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		holder.word.setText(note.getWord());
		holder.content.setText(note.getContent());
		return view;
	}
	class Holder {
		TextView word;
		TextView content;
	}
}