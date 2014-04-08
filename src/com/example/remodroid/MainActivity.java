package com.example.remodroid;

import java.io.IOException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView t;
	TextView sc;
	long dtime=0,prev;
	
	Button b;
	int startX=0;
	int startY=0;
	float stSCX;
	float stSCY;
	int Sens=2;
	GestureDetector gestureDetector;
    boolean upenabled ;
	EditText keyboard;
	boolean fromDown;
	boolean eclicked;
	static Context mc;
	int count;
	public static int flag=0;
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        count=0;
        //Button b1 = (Button) findViewById(R.id.button1);
        
        String ip = intent.getStringExtra(starter.EXTRA_MESSAGE);
        //b1.setText(ip);
        NetworkThread startNetwork = new NetworkThread(ip);
        startNetwork.ru();
       
        	//NetworkThread.sendmessage("test".getBytes());
       while(NetworkThread.isSocketNull()){
    	   count++;
    	   Log.e("M","Count :"+count);
    	   if(count>10000)
    		   break;
       }
        
        gestureDetector = new GestureDetector(gestureListener);
        
        setContentView(R.layout.activity_main);
        t = (TextView) findViewById(R.id.textView2);
        sc = (TextView) findViewById(R.id.textView3);
        b= (Button) findViewById(R.id.button1);
        if(count<10000){
        	b.setClickable(true);
        	b.setText("Disconnect");
        }
        t.requestFocus();
        t.setText(NetworkThread.result);
        keyboard = (EditText) findViewById(R.id.editText1);
        keyboard.addTextChangedListener(watcher);
      //********************** SCROLL *************************************
        sc.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					stSCY = event.getY();
					return true;
				}
				if(event.getAction()==MotionEvent.ACTION_MOVE){
					float dis = event.getY()-stSCY;
						NetworkThread.sendmessage(("#sc"+dis).getBytes());
					
					
					stSCY=event.getY();
					return true;
				}
				return false;
			}
		});
    //****************** TOUCHPAD ************************************
        t.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_MOVE){
					NetworkThread.sendmessage(("X:"+(event.getX()-startX)+"Y:"+(event.getY()-startY)).getBytes());
					startX=(int)event.getX();
					startY=(int)event.getY();
					upenabled=false;
					return true;
				}
				else if(event.getAction()==MotionEvent.ACTION_UP)
					NetworkThread.sendmessage(("lrelease").getBytes());
				return gestureDetector.onTouchEvent(event);
			  }
        });
        
        //***************************************************************************8
    
        keyboard.setOnKeyListener(new OnKeyListener() {                 
            public boolean onKey(View v, int keyCode, KeyEvent event) {
            		
                  if(keyCode == KeyEvent.KEYCODE_DEL&&event.getAction()==KeyEvent.ACTION_UP){
                	  NetworkThread.sendmessage(("back").getBytes());
                       return true;
                   }
                   return false;
                }
        });
   }
    
  //*************************GESTURE DETECTOR *************************
    GestureDetector.OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener(){
    	public boolean onDown(MotionEvent e) {
			//tv.setText("\nEvent : Down");
    		startX = (int)e.getX();
			startY = (int)e.getY();
			prev=dtime;
			dtime=e.getEventTime();
			//if((dtime-prev)<250)
				//NetworkThread.sendmessage(("lpress").getBytes());
			upenabled=true;
			return true;
		}

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			return false;
		}
		public boolean onDoubleTapEvent (MotionEvent e){
			if(e.getAction()==MotionEvent.ACTION_DOWN){
				NetworkThread.sendmessage(("lpress").getBytes());
				return true;
			}
			else if(e.getAction()==MotionEvent.ACTION_UP){
				if(upenabled){
					NetworkThread.sendmessage(("LeftClick").getBytes());
				}
			}
			return true;
		}
		

		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return false;
		}

		

		public boolean onSingleTapUp(MotionEvent e) {
			//tv.setText("\nEvent : SingleTapUp");
			return false;
		}
		
		public boolean onSingleTapConfirmed(MotionEvent e){
			if(upenabled){
				NetworkThread.sendmessage(("LeftClick").getBytes());
			return true;}
			else
				return false;
			
		}
    };
    private TextWatcher watcher = new TextWatcher() {
		@TargetApi(9)
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if(count>0){
				if(s.toString().substring(start).contains("\n"))
					NetworkThread.sendmessage(("enter").getBytes());
				//else if(s.toString().substring(start).contains("\b"))
					//NetworkThread.sendmessage(("back").getBytes());
				else
					NetworkThread.sendmessage((":"+s.subSequence(start, start+count)).getBytes());
			}
		}
		
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
		}
		
		public void afterTextChanged(Editable s) {
		}
	};
   
    
    public void LeftClick(View v){
    	NetworkThread.sendmessage(("LeftClick").getBytes());
    }
    public void RightClick(View v){
    	NetworkThread.sendmessage(("RightClick").getBytes());
    }
   
	public void disc(View v) throws IOException{
    	NetworkThread.sendmessage(("DISCONNECT").getBytes());
    	finish();
    }
}
