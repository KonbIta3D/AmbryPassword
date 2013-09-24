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
import android.widget.Toast;

import com.ambry.passw.R;
import com.ambry.passw.dbase.Operate_DB;
import com.ambry.passw.security.S_md5_Class;

public class RemindPasswordFragment extends DialogFragment {
	private EditText eTextSecurQuestion;
	private EditText eTextAnswer;
	private Button okButton;
	private Button cancelButton;
	private Operate_DB operate_db;
	// private String currentPass;
	private S_md5_Class cript = new S_md5_Class();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_security_qstn, null);
		getDialog().setTitle(
				getResources().getString(R.string.title_remind_passwd));
		eTextSecurQuestion = (EditText) view.findViewById(R.id.secur_qstn);

		eTextAnswer = (EditText) view.findViewById(R.id.secur_answr);
		okButton = (Button) view.findViewById(R.id.button_ok_secur);
		cancelButton = (Button) view.findViewById(R.id.button_cancel_secur);

		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				checkMyAnswer(eTextAnswer.getText().toString());
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

		eTextSecurQuestion.setText(operate_db.getQuestion());
		eTextSecurQuestion.setEnabled(false);

		// currentPass = operate_db.getCheckupPass();

		return view;
	}

	protected void checkMyAnswer(String answer) {
		String currentAnswer = operate_db
				.getAnswerForQuestion(eTextSecurQuestion.getText().toString());
		if (cript.md5(answer).equals(currentAnswer)) {
			showDialogChangePassword();
		} else {
			Toast mistake = Toast.makeText(getActivity(), getResources()
					.getString(R.string.wrong_answer), Toast.LENGTH_SHORT);
			mistake.show();
		}

	}

	private void showDialogChangePassword() {
		ChangePasswordFragment chPswdFrgmnt = new ChangePasswordFragment();
		chPswdFrgmnt.setIsRemindPasswd();
		chPswdFrgmnt.show(getFragmentManager(), "change");
	}

	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		operate_db.closeDb();

	}

	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);

		operate_db.closeDb();

	}
}
