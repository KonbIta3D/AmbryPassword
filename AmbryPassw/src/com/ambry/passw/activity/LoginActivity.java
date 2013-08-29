package com.ambry.passw.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.ambry.passw.R;
import com.ambry.passw.dbase.Operate_DB;
import com.ambry.passw.security.S_md5_Class;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends SherlockFragmentActivity {

	private static final String INCOM_PASS = "myPass";

	EditText firstPassword;
	EditText secondPassword;
	Button saveNewPassButton;
	Button cancelButton;

	EditText mPasswordView;
	Button sing_in;
	private Operate_DB dSourse;
	private S_md5_Class crypt;
	private String currentPassword = null;
	private boolean isActivePassw = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dSourse = new Operate_DB(this);
		crypt = new S_md5_Class();

		currentPassword = dSourse.getCheckupPass();
		isActivePassw = dSourse.isActivePass();

		if (currentPassword.equals("") || !isActivePassw) {

			setContentView(R.layout.create_password_dialog);
			setTitle("Ambry Password");
			firstPassword = (EditText) findViewById(R.id.passw1_cr_dialog);
			secondPassword = (EditText) findViewById(R.id.passw2_cr_dialog);
			saveNewPassButton = (Button) findViewById(R.id.save_button_cr_dialog);
			cancelButton = (Button) findViewById(R.id.cancel_button_cr_dialog);

			saveNewPassButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (firstPassword.getText().toString()
							.equals(secondPassword.getText().toString())) {
						if (firstPassword.getText().toString().length() >= 8) {

							dSourse.insertPassword(crypt.md5(firstPassword
									.getText().toString()));

							hideKeyBoard(secondPassword);

							dSourse.closeDb();
							Intent intent = new Intent(getApplicationContext(),
									MainActivity.class);
							intent.putExtra(INCOM_PASS, firstPassword.getText()
									.toString());
							startActivity(intent);
							finish();
						} else {
							Toast erMes = Toast.makeText(
									getApplicationContext(),
									getResources().getString(
											R.string.error_passw_is_short),
									Toast.LENGTH_SHORT);
							erMes.show();
							firstPassword.setText("");
							secondPassword.setText("");
							hideKeyBoard(secondPassword);
						}

					} else {
						Toast erMes = Toast.makeText(
								getApplicationContext(),
								getResources().getString(
										R.string.error_pass_is_not_equal),
								Toast.LENGTH_SHORT);
						erMes.show();
						firstPassword.setText("");
						secondPassword.setText("");
						hideKeyBoard(secondPassword);
					}
				}
			});

			cancelButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dSourse.closeDb();
					finish();
				}
			});

		} else {
			setContentView(R.layout.activity_login);
			mPasswordView = (EditText) findViewById(R.id.password);
			sing_in = (Button) findViewById(R.id.sign_in_button);
			sing_in.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (currentPassword.equals(crypt.md5(mPasswordView
							.getText().toString()))) {

						hideKeyBoard(mPasswordView);
						dSourse.closeDb();

						Intent intent = new Intent(getApplicationContext(),
								MainActivity.class);
						intent.putExtra(INCOM_PASS, mPasswordView.getText()
								.toString());
						startActivity(intent);
						finish();

					} else {
						mPasswordView.setText("");
						Toast erMes = Toast.makeText(
								getApplicationContext(),
								getResources().getString(
										R.string.error_invalid_password),
								Toast.LENGTH_LONG);
						erMes.show();
					}

				}
			});

		}

	}

	private void hideKeyBoard(EditText etext) {
		if (etext.equals(null))
			return;
		InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(etext.getWindowToken(), 0);
	}
}
