package com.cheng.test.quickindexbar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

	private ArrayList<Friend> friendsList;
	private Context context;

	public MyAdapter(Context context, ArrayList<Friend> friendsList) {
		this.friendsList = friendsList;
		this.context = context;

	}

	@Override
	public int getCount() {

		return friendsList.size();
	}

	@Override
	public Object getItem(int position) {

		return friendsList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.list_item, null);
		}

		holder = ViewHolder.getViewHolder(convertView);

		Friend friend = friendsList.get(position);
		holder.name.setText(friend.getName());
		holder.firstWord.setText(friend.getPinyin().charAt(0) + "");
		if (position > 0) {
			String lastWord = friendsList.get(position - 1).getPinyin().charAt(0) + "";
			if (lastWord.equals(friend.getPinyin().charAt(0) + "")) {
				holder.firstWord.setVisibility(View.GONE);
			} else {
				// 由于布局为复用的，所以在需要显示的地方要设置为显示
				holder.firstWord.setVisibility(View.VISIBLE);
			}
		}

		return convertView;
	}

	static class ViewHolder {
		private TextView name;
		private TextView firstWord;

		public ViewHolder(View convertView) {
			name = (TextView) convertView.findViewById(R.id.name);
			firstWord = (TextView) convertView.findViewById(R.id.first_word);
		}

		public static ViewHolder getViewHolder(View convertView) {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			if (holder == null) {
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}

			return holder;
		}
	}
}
