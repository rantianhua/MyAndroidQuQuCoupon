package huahua.viewpager;

import jiajia.Thread.NetThread;
import jiajia.other.MyApplication;
import jiajia.variable.GlobalVariable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Start extends Activity {
	//android.os.Handler mHandler;
	Handler mHandler;
	EditText et_customer_name;
	EditText et_customer_password;
	ProgressBar pb;
	String customer_name;
	String customer_password;
	String result;
	SharedPreferences sp;
	Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		et_customer_name = (EditText)findViewById(R.id.editText_email);
		et_customer_password = (EditText)findViewById(R.id.editText_password);
		pb = (ProgressBar) this.findViewById(R.id.ProgressBar);
		sp = getSharedPreferences("UserInfor",MODE_PRIVATE);
		editor = sp.edit();
		String name = sp.getString("name", "");
		String password = sp.getString("password", "");
		if(name!=null){
			et_customer_name.setText(name);
			et_customer_password.setText(password);
		}
		final MyApplication mApp = (MyApplication) getApplication();
		GlobalVariable.initCache();
		mHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg){
				pb.setVisibility(View.GONE);
				result = msg.obj.toString();
				if(result.equals("ok")){
					showInfo("登陆成功！");
					mApp.setuserID(customer_name);
					editor.remove("name");
					editor.remove("password");
					editor.putString("name",customer_name);
					editor.putString("password",customer_password);
					editor.commit();
					Intent intent = new Intent(Start.this,MainActivity.class);
					startActivity(intent);
					finish();
				}
				if(result.equals("fail")){
					showInfo("登录失败！用户名或密码错误！请重新输入！");
					et_customer_name.setText("");
					et_customer_password.setText("");
					return;
				}
			}
		};
		
		}
		
	private void showInfo(String string) {
		Toast.makeText(Start.this, string, Toast.LENGTH_SHORT).show();		
	}

	public void LoginClick(View view)
	{
		customer_name = et_customer_name.getText().toString().trim();
		customer_password = et_customer_password.getText().toString().trim();
		
		if(TextUtils.isEmpty(customer_name) || TextUtils.isEmpty(customer_password)) {
			showInfo("用户名和密码不能为空！");
			et_customer_name.setText("");
			et_customer_password.setText("");
			return;
		}
		String url = "http://1.ququcoupon.sinaapp.com/check_android_login.php?customer_name="+customer_name+"&customer_password="+customer_password;
		pb.setVisibility(View.VISIBLE);
		NetThread netThread = new NetThread(mHandler, url);
		netThread.start();
	}

		public void RegisterClick(View view)
		{
			Intent intent = new Intent(Start.this,Register.class);
			startActivity(intent);
		}
		protected void onStart() {

			super.onStart();
			MyApplication mApp = (MyApplication) getApplication();
			if (mApp.getExit()) {
				mApp.setExit(false);
				finish();
			}
		}	
}
