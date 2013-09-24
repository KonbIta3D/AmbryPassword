package com.ambry.passw.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.ambry.passw.R;
import com.ambry.passw.activity.fragment.ChangeSecurityQuestionFragment;
import com.ambry.passw.activity.fragment.RemindPasswordFragment;
import com.ambry.passw.dbase.Operate_DB;
import com.ambry.passw.security.S_md5_Class;

/**
 * Created with IntelliJ IDEA. User: YAKOVLEV Date: 15.08.13 Time: 9:41 Activity
 * which displays a login screen to the user, offering registration as well.
 */
public class LoginActivity extends SherlockFragmentActivity {

	private EditText firstPassword, secondPassword, mPasswordView;
	private Button saveNewPassButton, cancelButton, sing_in;
	private CheckBox chBxUseSecurQstn;
	private Button recallPasswdButton;

	private Operate_DB dSourse;
	private S_md5_Class crypt;
	private String currentPassword = null;
	private ChangeSecurityQuestionFragment securFrag;
	private boolean isSecretQuestion;
	private int counterWrongPassw = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dSourse = new Operate_DB(this);
		crypt = new S_md5_Class();

		currentPassword = dSourse.getCheckupPass();
		isSecretQuestion = false;

		if (currentPassword.equals("")) {

			setContentView(R.layout.activity_first_enter);
			setTitle("Ambry Password");
			firstPassword = (EditText) findViewById(R.id.passw1_cr_dialog);
			secondPassword = (EditText) findViewById(R.id.passw2_cr_dialog);
			saveNewPassButton = (Button) findViewById(R.id.save_button_cr_dialog);
			cancelButton = (Button) findViewById(R.id.cancel_button_cr_dialog);
			chBxUseSecurQstn = (CheckBox) findViewById(R.id.chbx_use_security_question);
			securFrag = new ChangeSecurityQuestionFragment();

			chBxUseSecurQstn
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (buttonView.equals(chBxUseSecurQstn)
									&& isChecked) {
								securFrag.show(getSupportFragmentManager(),
										"secur");

							}

						}
					});

			if (!dSourse.getAnswerForQuestion().equals("")) {
				isSecretQuestion = true;
			}
			chBxUseSecurQstn.setChecked(isSecretQuestion);
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
			recallPasswdButton = (Button) findViewById(R.id.recall_passwd);

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

						startActivity(intent);
						finish();

					} else {
						mPasswordView.setText("");
						Toast erMes = Toast.makeText(
								getApplicationContext(),
								getResources().getString(
										R.string.error_invalid_password),
								Toast.LENGTH_SHORT);
						erMes.show();
						counterWrongPassw++;
						if (counterWrongPassw == 1) {
							recallPasswdButton.setVisibility(Button.VISIBLE);
							recallPasswdButton
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											RemindPasswordFragment rmPswdFrgmnt = new RemindPasswordFragment();
											rmPswdFrgmnt
													.show(getSupportFragmentManager(),
															"remind");
										}
									});
						}
					}

				}
			});

		}

	}

	protected void recallPassword() {
		Button buttonRecall = new Button(getApplicationContext());
		buttonRecall.setText("Remind my password");
		// buttonRecall.setActivated(true);

	}

	private void hideKeyBoard(EditText etext) {
		if (etext.equals(null))
			return;
		InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(etext.getWindowToken(), 0);
	}

	@Override
	protected void onStop() {
		dSourse.closeDb();
		super.onStop();
	}
}
