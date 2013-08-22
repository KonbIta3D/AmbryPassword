//package com.mycompany.myapp;
package com.ambry.passw.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ambry.passw.security.S_md5_Class;
import com.ambry.passw.R;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

	final String LOGIN[] = { "log" };
	final String PASSwD[] = { "PAss" };
	SharedPreferences sPref;
	EditText mPasswordView;
	Button sing_in;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		// setContentView(R.layout.two);

		mPasswordView = (EditText) findViewById(R.id.password);

		sing_in = (Button) findViewById(R.id.sign_in_button);
		sing_in.setOnClickListener(this);

		sPref = getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);

		if (!findPrefText(sPref)) {
			Toast.makeText(this, "Проверка Pref не успешна", Toast.LENGTH_SHORT)
					.show();
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);

			finish();
		}
		;

	}

	public void onClick(View v) {
		S_md5_Class sd = new S_md5_Class();
		switch (v.getId()) {
		case R.id.sign_in_button:

			String PASS = sPref.getString("PASSWD", "");
			if (PASS.equals(sd.md5(mPasswordView.getText().toString()))) {
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				finish();
			} else {
				mPasswordView.setText("");
				Toast.makeText(getApplicationContext(), "Wrong password!",
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
