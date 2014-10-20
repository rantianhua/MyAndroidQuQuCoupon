package jiajia.Thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import jiajia.other.MyBufferReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class NetThread extends Thread{
	private Handler mHandler;
	private String url;
	DefaultHttpClient client;

	
	public NetThread(Handler mHandler, String url) {
		this.mHandler = mHandler;
		this.url = url;
	}
	
   public void run() {
		client = new DefaultHttpClient();
		Receive();
   }
   
   private void Receive()
   {
	    String result;
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		Message msg = new Message();
		try {
			response = client.execute(get);
			
			MyBufferReader bin = new MyBufferReader(new InputStreamReader(response.getEntity().getContent()));
		    result = bin.myReadLine();	 
	           msg.what = 0;
			   msg.obj = result;
			   mHandler.sendMessage(msg);		
				} 
		
      catch (ClientProtocolException e) {
			
			e.printStackTrace();
			
		}
		catch(IOException e){
			e.printStackTrace();//打印栈堆里的错误信息
		}
	
   }
			
	//	} catch (ClientProtocolException e) 
   }
   

