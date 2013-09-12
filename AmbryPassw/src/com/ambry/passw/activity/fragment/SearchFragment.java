package com.ambry.passw.activity.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import com.ambry.passw.R;
import com.ambry.passw.activity.MainActivity;
import com.ambry.passw.activity.MyAdapter;
import com.ambry.passw.dbase.Operate_DB;

/**
 * 
 * @author SKOBELEV
 *
 */
public class SearchFragment extends DialogFragment {
	final String LOG_TAG = "myLogs";
	RadioButton rButnSearchByLogin;
	RadioButton rBtnSearchByComment;
	EditText eText;
	private String secretWord = "";


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getDialog().setTitle(
				getResources().getString(R.string.title_fragment_search));
		View view = inflater.inflate(R.layout.fragment_search, null);
		eText = (EditText) view.findViewById(R.id.editTextSearch);
		view.findViewById(R.id.radio_btn_search_by_login);
		view.findViewById(R.id.radio_btn_search_by_comment);
		view.findViewById(R.id.buttonSearch).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						search();
					}
				});

		rButnSearchByLogin = (RadioButton) view
				.findViewById(R.id.radio_btn_search_by_login);
		rBtnSearchByComment = (RadioButton) view
				.findViewById(R.id.radio_btn_search_by_comment);
		Operate_DB dsource = new Operate_DB(getDialog().getContext());
		secretWord = dsource.getCheckupPass();
		dsource.closeDb();
		return view;
	}

	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		Log.d(LOG_TAG, " onDismiss");
	}

	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		Log.d(LOG_TAG, "Dialog 1: onCancel");
	}

	

	private void search() {

		Operate_DB oper = new Operate_DB(getDialog().getContext());
		ListView lView;
		if (rButnSearchByLogin.isChecked()) {

			oper.findItemsByLogin(MainActivity.data, eText.getText().toString());
			 lView = (ListView) getActivity().findViewById(R.id.list);

			MyAdapter adapter = new MyAdapter(getActivity(),MainActivity.data, secretWord);
			lView.setAdapter(adapter);
			adapter.notifyDataSetChanged();

		} else {
			oper.findItemsByComment(MainActivity.data, eText.getText().toString());
			 lView = (ListView) getActivity().findViewById(R.id.list);
			 MyAdapter adapter = new MyAdapter(getActivity(), MainActivity.data, secretWord);
			lView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			
		}
		lView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				menu.add("Del item");
			
			}
			
		});
		
		
		eText.setText("");
		rButnSearchByLogin.setChecked(true);
		oper.closeDb();
		dismiss();
	}

}
