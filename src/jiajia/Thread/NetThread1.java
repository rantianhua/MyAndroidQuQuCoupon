package jiajia.Thread;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import jiajia.other.MyBufferReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class NetThread1 extends Thread{

	private Handler mHandler;
	private DefaultHttpClient client;
	private String url;
    private int n;
	public NetThread1(Handler mHandler,int n) {
		this.mHandler = mHandler;
		this.n = n;
	}

	public void run() {
		client = new DefaultHttpClient();
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 5000); // 设置请求超时时间
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 50000); // 读取超时

		// 处理网络任务
		DoNetWork();
	}

	private void DoNetWork() {
		String result = null;
		url = "http://1.ququcoupon.sinaapp.com/coupons.php?page="+n;
		Log.v("url>>>>>>>>>>>>", url);
		HttpGet get = new HttpGet(url);
		get.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET,
				"UTF-8");
		HttpResponse response;
		Message msg = new Message();

		try {
			response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				MyBufferReader bin = new MyBufferReader(new InputStreamReader(
						response.getEntity().getContent()));
				result = bin.myReadLine();
				msg.what = 0;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
	}
	
}
