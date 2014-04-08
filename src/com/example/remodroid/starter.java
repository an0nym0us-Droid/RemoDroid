package com.example.remodroid;

import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class starter extends Activity {
	
	EditText e ;
	Socket mouseSocket;
	public final static String EXTRA_MESSAGE = "com.example.mousemove.MESSAGE";
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        e= (EditText) findViewById(R.id.editText1);
        
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
                    if (!resultingTxt.matches ("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) { 
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i=0; i<splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 255) {
                            	return "";
                            }
                        }
                    }
                }
            return null;
            }
        };
        e.setFilters(filters);
	}
	
	public void connectMe(View v){
		Intent in = new Intent(this,MainActivity.class);
		if(e.getText().toString().equals(""))
		Toast.makeText(this.getApplicationContext(), "Please enter a IP address", Toast.LENGTH_SHORT).show();
		else
		{	
				in.putExtra(EXTRA_MESSAGE, e.getText().toString());
				startActivity(in);
		}
	}

}
	
	
