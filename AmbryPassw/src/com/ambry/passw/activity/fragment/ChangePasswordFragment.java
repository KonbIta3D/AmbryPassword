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
import android.widget.EditText;
import android.widget.TextView;

import com.ambry.passw.R;
import com.ambry.passw.dbase.Operate_DB;
import com.ambry.passw.security.S_md5_Class;

/**
 * 
 * @author SKOBEELV
 *
 */
public class ChangePasswordFragment extends DialogFragment {

	final String LOG_TAG = "myLogs";

	private EditText editTxtOldPassword;
	private EditText editTxtNewPass;
	private EditText editTxtConfirmPassword;

	private Button savePassButton;
	private Button cancelButton;
	private TextView errorText;
	
	Operate_DB operate_db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getDialog().setTitle(
				getResources().getString(R.string.title_activity_conf_));
		View view = inflater.inflate(R.layout.fragment_chng_pswd, null);
		operate_db = new Operate_DB(view.getContext());

		editTxtOldPassword = (EditText) view.findViewById(R.id.old_password);
		editTxtNewPass = (EditText) view.findViewById(R.id.new_password);
		editTxtConfirmPassword = (EditText) view
				.findViewById(R.id.confirm_new_password);

		savePassButton = (Button) view.findViewById(R.id.save_button_conf);
		savePassButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

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

		errorText = (TextView) view.findViewById(R.id.errorText_conf);

		return view;
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

		if (editTxtNewPass.getText().toString()
				.equals(editTxtConfirmPassword.getText().toString())) {
			if (editTxtNewPass.getText().toString().length() >= 8) {

				S_md5_Class crypt = new S_md5_Class();

				String newPassword = crypt.md5(editTxtNewPass.getText()
						.toString());

				String oldPassword = crypt.md5(editTxtOldPassword.getText()
						.toString());
				if (oldPassword.equals(operate_db.getCheckupPass())) {
					operate_db.updateMyTable(newPassword, oldPassword);
					operate_db.updatePassword(newPassword);
					
					Log.d(LOG_TAG, "updated password to "
							+ editTxtOldPassword.getText().toString());
					hideKeyBoard(editTxtConfirmPassword);
					dismiss();
				} else {
					errorText.setText("Old password is not equals to new");
					editTxtOldPassword.setText("");
					editTxtNewPass.setText("");
					editTxtConfirmPassword.setText("");
					hideKeyBoard(editTxtConfirmPassword);
				}

			} else {
				errorText.setText(getResources().getString(
						R.string.error_passw_is_short));
				Log.d(LOG_TAG, "password is equals and is so short");
				clearPassword();
				hideKeyBoard(editTxtConfirmPassword);
			}
		} else {
			errorText.setText(getResources().getString(
					R.string.error_pass_is_not_equal));
			Log.d(LOG_TAG, "passwords are not equal");
			clearPassword();
			hideKeyBoard(editTxtConfirmPassword);
			
		}

		operate_db.closeDb();


	}

	private void insertNewPassword() {
		boolean isInsertedPass = false;
		if (isPasswordsIsMatch(editTxtOldPassword.getText().toString(),
				editTxtConfirmPassword.getText().toString())) {
			if (isLonger8(editTxtOldPassword.getText().toString())) {

				S_md5_Class crypt = new S_md5_Class();

				String encriptedPassword = crypt.md5(editTxtOldPassword
						.getText().toString());

				isInsertedPass = operate_db.insertPassword(encriptedPassword);

				if (isInsertedPass)
					Log.d(LOG_TAG, "password saved to "
							+ editTxtOldPassword.getText().toString());
				operate_db.closeDb();
				dismiss();

			} else {
				errorText.setText(getResources().getString(
						R.string.error_passw_is_short));
				Log.d(LOG_TAG, "password so short. not inserted");
				hideKeyBoard(editTxtConfirmPassword);
				clearPassword();
			}
		} else {
			errorText.setText(getResources().getString(
					R.string.error_pass_is_not_equal));
			hideKeyBoard(editTxtConfirmPassword);
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
		editTxtOldPassword.setText("");
		editTxtConfirmPassword.setText("");
	}

	private boolean isLonger8(String line) {
		if ((line.length() < 8 & line.length() > 0) | line.equals(""))
			return false;
		else
			return true;
	}
}
