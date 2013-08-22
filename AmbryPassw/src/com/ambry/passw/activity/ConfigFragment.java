package com.ambry.passw.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.ambry.passw.R;

import com.ambry.passw.dbase.DBHelper;
import com.ambry.passw.dbase.Operate_DB;
import com.ambry.passw.security.S_md5_Class;


public class ConfigFragment extends DialogFragment implements OnClickListener {
	final String LOG_TAG = "myLogs";

	private SharedPreferences sPref;
	
	public EditText save_password1;
    public EditText save_password2;
    
    CheckBox chb;
    Button savePassButton;
  
    Operate_DB operate_db;
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		
		
		
		
		
		
		getDialog().setTitle(
				getResources().getString(R.string.title_activity_conf_));
		View view = inflater.inflate(R.layout.activity_conf, null);
		view.findViewById(R.id.save_password1);
		view.findViewById(R.id.save_password2);
		view.findViewById(R.id.save_checkBox);
		view.findViewById(R.id.save_button).setOnClickListener(this);
		chb=(CheckBox) view.findViewById(R.id.save_checkBox);
//		try{
		if(sPref.contains("PASSWD")){
			chb.setChecked(true);	
//		return view;}
		}
	//		catch(Exception e){};
        
//		
		else chb.setChecked(false);
		operate_db = new Operate_DB(view.getContext());
		return view;
	}
	

/*	
	boolean isSavedTextTrue(EditText text){
	       // sPref = PreferenceManager.getDefaultSharedPreferences(Context.MODE_PRIVATE);
	        Editor ed =sPref.edit();
	        ed.putString("PASSWD",text.getText().toString());
	        ed.commit();
	        String provtext;
	        provtext = sPref.getString("PASSWD","");
	        if(text.getText().toString().equals(provtext))return true;
	        else return false;
	}*/
	
//TODO checks your password
	private boolean isSavedTextTrue(String s) {
		Editor ed = sPref.edit();
		ed.putString("PASSWD", s);
		ed.commit();
		String provtext;
		provtext = sPref.getString("PASSWD", "");
		if (s.equals(provtext))
			return true;
		else
			return false;
	}

	// TODO
	// this method checks two strings
	private boolean isPasswordsIsMatch(String s, String c) {
		String ss = new String(s);
		String cc = new String(c);
		if (ss.equals(cc))
			return true;
		else
			return false;
	}

	@Override
	public void onClick(View v) {
		//TODO implement this method
		// should respond to the button "Save"
		S_md5_Class sd = new S_md5_Class();

      switch (v.getId()){
          case R.id.save_button:
            //  texts(save_password1.toString(),save_password2.toString());
              if(chb.isChecked()){
                String Pass1 = sd.md5(save_password1.getText().toString());
                  if (!isPasswordsIsMatch(save_password1.getText().toString() ,save_password2.getText().toString()))break;

                  if (operate_db.insertPassword(Pass1))Log.d(LOG_TAG, "Password is saved");


                  else Log.d(LOG_TAG, "Save is error");


                  }
                  //Toast.makeText(this, "Пароль не совпадает",Toast.LENGTH_SHORT).show();

                  break;
              };

      
		dismiss();
	}

	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		Log.d(LOG_TAG, " onDismiss");
	}

	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		Log.d(LOG_TAG, "Dialog 1: onCancel");
	}
}
