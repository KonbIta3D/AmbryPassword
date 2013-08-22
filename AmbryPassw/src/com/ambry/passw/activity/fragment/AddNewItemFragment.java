package com.ambry.passw.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.passww.R;

public class AddNewItemFragment extends DialogFragment implements
		OnClickListener {
	final String LOG_TAG = "myLogs";
	private EditText eTxLogin;
	private EditText eTxPassord;
	private EditText eTxComment;
	private Button saveButton;
	private Button cancelButton;

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
		switch (v.getId()){
		case R.id.buttonSave:
			//TODO save item into DataBase
			dismiss();
			break;
		case R.id.buttonCancel:
			dismiss();
			break;
		}
	}
	
	
}
