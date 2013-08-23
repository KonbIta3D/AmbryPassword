package com.ambry.passw.activity.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.ambry.passw.R;
import com.ambry.passw.dbase.Operate_DB;

public class ConfigFragment extends DialogFragment implements OnClickListener {
	final String LOG_TAG = "myLogs";

	private EditText save_password1;
	private EditText save_password2;

	private boolean isCheckedBox;

	private Button savePassButton;
	private Button cancelButton;
	private TextView errorText;
	private CheckBox checkBox;
	Operate_DB operate_db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getDialog().setTitle(
				getResources().getString(R.string.title_activity_conf_));
		View view = inflater.inflate(R.layout.fragment_conf, null);
		operate_db = new Operate_DB(view.getContext());

		view.findViewById(R.id.save_checkBox);
		checkBox = (CheckBox) view.findViewById(R.id.save_checkBox);

		isCheckedBox = operate_db.isActivePass();

		if (isCheckedBox) {
			checkBox.setChecked(isCheckedBox);
		}
		view.findViewById(R.id.save_password1);
		view.findViewById(R.id.save_password2);
		view.findViewById(R.id.save_button_conf);
		view.findViewById(R.id.cancel_button_conf);
		view.findViewById(R.id.errorText_conf);

		errorText = (TextView) view.findViewById(R.id.errorText_conf);
		save_password1 = (EditText) view.findViewById(R.id.save_password1);
		save_password2 = (EditText) view.findViewById(R.id.save_password2);
		savePassButton = (Button) view.findViewById(R.id.save_button_conf);
		savePassButton.setOnClickListener(this);
		cancelButton = (Button) view.findViewById(R.id.cancel_button_conf);
		cancelButton.setOnClickListener(this);
		return view;
	}

	public void onClick(View v) {
		if (v.equals(savePassButton)) {
			String currentPassword = operate_db.getCheckupPass();
			if (currentPassword.equals("")) {
				insertNewPassword();
			} else {
				updatePassword();
			}

		}
		if (v.equals(cancelButton)) {
			dismiss();
			Log.d(LOG_TAG, "fragment window canceled");
		}

	}

	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		clearPassword();
		Log.d(LOG_TAG, " onDismiss");
	}

	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		clearPassword();
		Log.d(LOG_TAG, "Dialog 1: onCancel");
	}

	private void updatePassword() {
		if (isPasswordsIsMatch(save_password1.getText().toString(),
				save_password2.getText().toString())) {
			operate_db.updatePassword(checkBox.isChecked(), save_password1
					.getText().toString());
			dismiss();
		} else {
			errorText.setText(getResources()
					.getString(R.string.passwIsNotValid));
			hideKeyBoard(save_password2);
			clearPassword();
		}
	}

	private void insertNewPassword() {
		if (isPasswordsIsMatch(save_password1.getText().toString(),
				save_password2.getText().toString())) {
			operate_db.insertPassword(save_password1.getText().toString());
			clearPassword();
			Log.d(LOG_TAG, "password saved");
			dismiss();
		} else {
			errorText.setText(getResources()
					.getString(R.string.passwIsNotValid));
			hideKeyBoard(save_password2);
			clearPassword();

		}

	}

	private boolean isPasswordsIsMatch(String s, String c) {
		String ss = new String(s);
		String cc = new String(c);
		if (ss.equals(cc))
			return true;
		else
			return false;
	}

	private void hideKeyBoard(EditText etext) {
		InputMethodManager inputMethodManager = (InputMethodManager) getDialog()
				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(etext.getWindowToken(), 0);
	}

	private void clearPassword() {
		save_password1.setText("");
		save_password2.setText("");
	}

}
