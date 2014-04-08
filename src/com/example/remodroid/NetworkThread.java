package com.example.remodroid;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.util.Log;

public class NetworkThread {
	
	
	static Socket mouseSocket;
	static DatagramSocket socket;
	static InetAddress add;
	String IP;
	static String result;
	public NetworkThread(String ip){
		IP = new String(ip);
	}
	public void ru() {
		Runnable r = new Runnable(){
			public void run() {
				try {
		        	mouseSocket = new Socket(IP , 4444);
		        	add = InetAddress.getByName(IP);
		        	socket=new DatagramSocket();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					Log.e("err",e.toString());
				}catch(SocketException e){
					Log.e("err",e.toString());
				}catch (IOException e) {
					// TODO Auto-generated catch block
					Log.e("err",e.toString());
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
		/*try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	public static boolean isSocketNull(){
		if(socket==null)
			return true;
		else
			return false;
	}
	public static void sendmessage  (byte[] buff)throws NullPointerException{
		final byte[] b = buff;
		Runnable r = new Runnable(){
			public void run(){
					DatagramPacket p = new DatagramPacket(b,b.length,add,4445);
					try {
						socket.send(p);
						if(b.toString().equals("DISCONNECT")){
							mouseSocket.close();
							socket.close();
						}
					} catch(NullPointerException e){
						Log.e("MYERR","NULLL");
					}
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		};
		new Thread(r).start();
	}
}
