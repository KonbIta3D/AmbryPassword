package com.ambry.passw.activity.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ambry.passw.R;
import com.ambry.passw.dbase.Operate_DB;
import com.ambry.passw.security.S_md5_Class;

public class ChangeSecurityQuestionFragment extends DialogFragment {
	private EditText eTextSecurQuestion;
	private EditText eTextAnswer;
	private Button okButton;
	private Button cancelButton;
	private Operate_DB operate_db;
	private boolean isSecretQuestion = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_security_qstn, null);
		eTextSecurQuestion = (EditText) view.findViewById(R.id.secur_qstn);
		eTextAnswer = (EditText) view.findViewById(R.id.secur_answr);
		okButton = (Button) view.findViewById(R.id.button_ok_secur);
		cancelButton = (Button) view.findViewById(R.id.button_cancel_secur);

		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				isSecretQuestion = updateSecurData();
				eTextSecurQuestion.setText("");
				eTextAnswer.setText("");
				dismiss();
			}

		});

		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				eTextSecurQuestion.setText("");
				eTextAnswer.setText("");
				dismiss();
			}
		});

		operate_db = new Operate_DB(view.getContext());

		String curruntPass = operate_db.getCheckupPass();
		if (curruntPass.equals("")) {
			getDialog().setTitle(
					getResources().getString(R.string.title_new_secur_qstn));
		} else {
			getDialog().setTitle(
					getResources().getString(R.string.title_chng_secur_qstn));
		}
		return view;
	}

	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		operate_db.closeDb();

	}

	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		operate_db.closeDb();

	}

	public boolean isSecurQuestnSet() {
		return isSecretQuestion;
	}

	private boolean updateSecurData() {
		S_md5_Class crypt = new S_md5_Class();
		String question = eTextSecurQuestion.getText().toString();
		String answer = crypt.md5(eTextAnswer.getText().toString());
		return operate_db.updateSecurData(question, answer);

	}

}
