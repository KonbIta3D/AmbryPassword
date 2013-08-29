package com.ambry.passw.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ambry.passw.R;
import com.ambry.passw.activity.Item;
import com.ambry.passw.activity.MainActivity;
import com.ambry.passw.activity.MyAdapter;
import com.ambry.passw.dbase.Operate_DB;
import com.ambry.passw.security.Crypto_Code;

public class AddNewItemFragment extends DialogFragment implements
		OnClickListener {
	final String LOG_TAG = "myLogs";
	private EditText eTxLogin;
	private EditText eTxPassord;
	private EditText eTxComment;
	private Button saveButton;
	private Button cancelButton;
	private String KEYWORD = "";
	public static MyAdapter fAdapter;
	private Operate_DB dSourse;
	private Crypto_Code cript;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getDialog().setTitle(getResources().getString(R.string.add_item_title));
		View view = inflater.inflate(R.layout.fragment_add_item, null);
		view.findViewById(R.id.editTextLogin);
		view.findViewById(R.id.editTextPassword);
		view.findViewById(R.id.editTextComment);
		view.findViewById(R.id.buttonSave).setOnClickListener(this);
		view.findViewById(R.id.buttonCancel).setOnClickListener(this);

		KEYWORD = MainActivity.getSecretWord();
		dSourse = new Operate_DB(getActivity().getApplicationContext());
		cript = new Crypto_Code();

		eTxLogin = (EditText) view.findViewById(R.id.editTextLogin);
		eTxPassord = (EditText) view.findViewById(R.id.editTextPassword);
		eTxComment = (EditText) view.findViewById(R.id.editTextComment);
		saveButton = (Button) view.findViewById(R.id.buttonSave);
		cancelButton = (Button) view.findViewById(R.id.buttonCancel);

		saveButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonSave:
			Item item = null;
			if (KEYWORD == null)
				item = new Item(eTxLogin.getText().toString(), eTxPassord
						.getText().toString(), eTxComment.getText().toString(),
						dSourse.getNextId());
			else
				item = new Item(eTxLogin.getText().toString(), cript.encrypt(
						eTxPassord.getText().toString(), KEYWORD), eTxComment
						.getText().toString(), dSourse.getNextId());
			dSourse.insertData(item);
			Log.d(LOG_TAG, "inserted new item with login: "+ item.getLogin());
			dSourse.closeDb();
			dismiss();
			break;
		case R.id.buttonCancel:
			dSourse.closeDb();
			dismiss();
			break;
		}
	}

}
