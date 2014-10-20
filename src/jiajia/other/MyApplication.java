package jiajia.other;

import android.app.Application;

public class MyApplication extends Application { 
	 
	private static boolean exit = false; 
	private static String userID = null; 
    public void setuserID(String userID){
    	MyApplication.userID = userID;
    }
	public String getuserID(){
		return userID;
	}
	public void setExit(boolean b) {
		exit = b;
	}
	public boolean getExit() {
		return  exit;
	}
	}