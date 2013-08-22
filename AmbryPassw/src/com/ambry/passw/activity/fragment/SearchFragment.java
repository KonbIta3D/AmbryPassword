package com.ambry.passw.activity.fragment;

import com.ambry.passw.R;

import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

public class SearchFragment extends DialogFragment implements OnClickListener{
	final String LOG_TAG = "myLogs";
	RadioButton rButnSearchByLogin;
	RadioButton rBtnSearchByComment;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		getDialog().setTitle(
				getResources().getString(R.string.title_fragment_search));
		View view = inflater.inflate(R.layout.fragment_search, null);
		view.findViewById(R.id.radio_btn_search_by_login);
		view.findViewById(R.id.radio_btn_search_by_comment);
		view.findViewById(R.id.buttonSearch).setOnClickListener( this);
		
		rButnSearchByLogin=(RadioButton)view.findViewById(R.id.radio_btn_search_by_login);
		rBtnSearchByComment=(RadioButton)view.findViewById(R.id.radio_btn_search_by_comment);
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



	
	@Override
	public void onClick(View v) {
		search();
		dismiss();
	}
	
	private void search(){
		if(rButnSearchByLogin.isChecked()){
			//TODO
			//search by login method
		}
		else{
			//TODO
			//search by comment method
		}
	}

}
