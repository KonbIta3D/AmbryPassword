package com.ambry.passw.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ambry.passw.R;
import com.ambry.passw.dbase.Operate_DB;
import com.ambry.passw.security.S_md5_Class;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

private static final String INCOM_PASS = "myPass";
//	final String LOGIN[] = { "log" };
//	final String PASSwD[] = { "PAss" };
	SharedPreferences sPref;
	EditText mPasswordView;
	Button sing_in;
	private Operate_DB db;
	private S_md5_Class crypt;
	private String currentPassword = null;
	private boolean isActivePassw = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		db = new Operate_DB(this);
		crypt = new S_md5_Class();

		mPasswordView = (EditText) findViewById(R.id.password);

		currentPassword = db.getCheckupPass();
		isActivePassw = db.isActivePass();

		sing_in = (Button) findViewById(R.id.sign_in_button);
		sing_in.setOnClickListener(this);

		if (currentPassword.equals("") || !isActivePassw) {
			Intent intent = new Intent(this, MainActivity.class);
			
			startActivity(intent);
			db.closeDb();
			finish();
		}

	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.sign_in_button:

			String enteredPassword = crypt.md5(mPasswordView.getText()
					.toString());
			if (currentPassword.equals(enteredPassword)) {
				Intent intent = new Intent(this, MainActivity.class);
				intent.putExtra(INCOM_PASS, mPasswordView.getText().toString());
				
				startActivity(intent);
				finish();
			} else {
				mPasswordView.setText("");
				Toast.makeText(
						getApplicationContext(),
						getResources().getString(
								R.string.error_incorrect_password),
						Toast.LENGTH_LONG).show();
			}
		}

	}

	boolean findPrefText(SharedPreferences pPref) {
		try {

			if (pPref.contains("PASSWD")) {

				return true;
			}
		} catch (Exception e) {
			Toast.makeText(this, "contains - Exeption", Toast.LENGTH_SHORT)
					.show();
		}

		return false;

	}

}
