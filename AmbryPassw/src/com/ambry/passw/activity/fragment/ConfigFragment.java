package com.ambry.passw.activity.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.ambry.passw.R;
import com.ambry.passw.dbase.Operate_DB;
import com.ambry.passw.security.S_md5_Class;

public class ConfigFragment extends DialogFragment implements
		OnCheckedChangeListener {

	final String LOG_TAG = "myLogs";

	private EditText save_password1;
	private EditText save_password2;

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

		view.findViewById(R.id.save_password1);
		view.findViewById(R.id.save_password2);
		view.findViewById(R.id.save_button_conf);
		view.findViewById(R.id.cancel_button_conf);
		view.findViewById(R.id.errorText_conf);

		errorText = (TextView) view.findViewById(R.id.errorText_conf);
		save_password1 = (EditText) view.findViewById(R.id.save_password1);
		save_password2 = (EditText) view.findViewById(R.id.save_password2);
		savePassButton = (Button) view.findViewById(R.id.save_button_conf);

		savePassButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// if (operate_db.isActivePass())
				if (operate_db.isPasswordPresent())
					updatePassword();
				else
					insertNewPassword();
			}
		});
		cancelButton = (Button) view.findViewById(R.id.cancel_button_conf);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		if (operate_db.isActivePass()) {
			checkBox.setChecked(true);

		} else {
			checkBox.setChecked(false);
			save_password1.setEnabled(false);
			save_password2.setEnabled(false);
			savePassButton.setEnabled(false);
		}
		checkBox.setOnCheckedChangeListener(this);
		return view;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView.equals(checkBox)) {
			if (!operate_db.isActivePass()) {
				save_password1.setEnabled(isChecked);
				save_password2.setEnabled(isChecked);
				savePassButton.setEnabled(isChecked);
			}
			if (operate_db.isActivePass() & !checkBox.isChecked()) {
				operate_db.updatePassword(false);
				Log.d(LOG_TAG, "password deactivated!");
			}
			if (!operate_db.isActivePass() & !checkBox.isChecked()) {
				dismiss();
			}
		}

	}

	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		clearPassword();
		operate_db.closeDb();
		Log.d(LOG_TAG, " onDismiss");
	}

	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		clearPassword();
		operate_db.closeDb();
		Log.d(LOG_TAG, "Dialog 1: onCancel");
	}

	private void updatePassword() {
		boolean isUpdatedPass = false;

		if (checkBox.isChecked()) {
			if (save_password1.getText().toString()
					.equals(save_password2.getText().toString())) {
				if (save_password1.getText().toString().length() >= 8) {

					S_md5_Class crypt = new S_md5_Class();

					String encriptedPassw = crypt.md5(save_password1.getText()
							.toString());

					isUpdatedPass = operate_db.updatePassword(
							checkBox.isChecked(), encriptedPassw);
					Log.d(LOG_TAG, "updated password to "
							+ save_password1.getText().toString());

				} else {
					errorText.setText(getResources().getString(
							R.string.error_passw_is_short));
					Log.d(LOG_TAG, "password is equals and is so short");
					clearPassword();
					hideKeyBoard(save_password2);
				}
			} else {
				errorText.setText(getResources().getString(
						R.string.error_pass_is_not_equal));
				Log.d(LOG_TAG, "passwords are not equal");
				clearPassword();
				hideKeyBoard(save_password2);
			}

			if (isUpdatedPass) {
				operate_db.closeDb();
				dismiss();
			} else
				return;
		}
	}

	private void insertNewPassword() {
		boolean isInsertedPass = false;
		if (isPasswordsIsMatch(save_password1.getText().toString(),
				save_password2.getText().toString())) {
			if (isLonger8(save_password1.getText().toString())) {

				S_md5_Class crypt = new S_md5_Class();

				String encriptedPassword = crypt.md5(save_password1.getText()
						.toString());

				isInsertedPass = operate_db.insertPassword(encriptedPassword);

				if (isInsertedPass)
					Log.d(LOG_TAG, "password saved to "
							+ save_password1.getText().toString());
				operate_db.closeDb();
				dismiss();

			} else {
				errorText.setText(getResources().getString(
						R.string.error_passw_is_short));
				Log.d(LOG_TAG, "password so short. not inserted");
				hideKeyBoard(save_password2);
				clearPassword();
			}
		} else {
			errorText.setText(getResources().getString(
					R.string.error_pass_is_not_equal));
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

	private boolean isLonger8(String line) {
		if ((line.length() < 8 & line.length() > 0) | line.equals(""))
			return false;
		else
			return true;
	}
}
