package com.ambry.passw.activity;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ambry.passw.R;
import com.ambry.passw.security.Crypto_Code;

/**
 * Created by YAKOVLEV on 25.07.13.
 */
public class MyAdapter extends BaseAdapter {

	LayoutInflater inflater;
	ArrayList<Item> items;
	private Context context;
	private String secretWord = "";
	private Crypto_Code crypt;

	public MyAdapter(Context context, ArrayList<Item> items, String secretWord) {
		this.context = context;
		this.items = items;
		this.secretWord = secretWord;
		crypt = new Crypto_Code();
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public MyAdapter(Context context, ArrayList<Item> items) {
		this.context = context;
		this.items = items;
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public Item getMyItem(int position) {
		return (Item) getItem(position);
	}

	public int getCount() {
		return items.size();
	}

	public Object getItem(int position) {
		return items.get(position);
	}

	public long getItemId(int position) {
		return items.get(position).getId();
	}

	public View getView(int position, View view, ViewGroup viewGroup) {

		View myListView = view;
		if (myListView == null) {
			myListView = inflater.inflate(R.layout.item, viewGroup, false);
		}
		Item item = (Item) getItem(position);
		((TextView) myListView.findViewById(R.id.tvTextLogin)).setText(item
				.getLogin());
		if (secretWord.equals(""))
			((TextView) myListView.findViewById(R.id.tvTextPassword))
					.setText(item.getPassword().toString());
		else
			((TextView) myListView.findViewById(R.id.tvTextPassword))
					.setText(crypt.decrypt(item.getPassword().toString()
							.getBytes(), secretWord));
		((TextView) myListView.findViewById(R.id.tvTextComment)).setText(item
				.getComment());

		return myListView;

	}

}
