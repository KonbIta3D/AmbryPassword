package com.ambry.passw.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.passww.R;

public class ConfigFragment extends DialogFragment implements OnClickListener {
	final String LOG_TAG = "myLogs";

	private SharedPreferences sPref;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		getDialog().setTitle(
				getResources().getString(R.string.title_activity_conf_));
		View view = inflater.inflate(R.layout.activity_conf, null);
		view.findViewById(R.id.save_password1);
		view.findViewById(R.id.save_password2);
		view.findViewById(R.id.save_checkBox);
		view.findViewById(R.id.save_button).setOnClickListener(this);
		return view;
	}

	
//TODO checks your password
	private boolean isSavedTextTrue(String s) {
		Editor ed = sPref.edit();
		ed.putString("PASSWD", s);
		ed.commit();
		String provtext;
		provtext = sPref.getString("PASSWD", "");
		if (s.equals(provtext))
			return true;
		else
			return false;
	}

	// TODO
	// this method checks two strings
	private boolean isPasswordsIsMatch(String s, String c) {
		String ss = new String(s);
		String cc = new String(c);
		if (ss.equals(cc))
			return true;
		else
			return true;
	}

	@Override
	public void onClick(View v) {
		//TODO implement this method
		// should respond to the button "Save"
		dismiss();
	}

	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		Log.d(LOG_TAG, " onDismiss");
	}

	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		Log.d(LOG_TAG, "Dialog 1: onCancel");
	}
}
