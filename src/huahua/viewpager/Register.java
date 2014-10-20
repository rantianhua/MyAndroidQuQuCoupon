package huahua.viewpager;

import java.lang.ref.WeakReference;
import jiajia.Thread.NetThread;
import jiajia.other.MyApplication;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {

	Handler mHandler;
	EditText et_customer_name;
	EditText et_customer_password;
	EditText et_customer_password2;
	String customer_name;
	String customer_password;
	String customer_password2;
	SharedPreferences sp;
	Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		mHandler = new MHandler(this, this);
		et_customer_name = (EditText) findViewById(R.id.editText_email_register);
		et_customer_password = (EditText) findViewById(R.id.editText_password_register);
		et_customer_password2 = (EditText) findViewById(R.id.editText_againPwd_register);
		sp = getSharedPreferences("UserInfor",MODE_PRIVATE);
		editor = sp.edit();

	}

	public void RegisterClick(View view) {
		customer_name = et_customer_name.getText().toString().trim();
		customer_password = et_customer_password.getText().toString().trim();
		customer_password2 = et_customer_password2.getText().toString().trim();

		if (TextUtils.isEmpty(customer_name)
				|| TextUtils.isEmpty(customer_password)
				|| TextUtils.isEmpty(customer_password2)) {
			showInfo("用户名和密码不能为空！");
			et_customer_name.setText("");
			et_customer_password.setText("");
			et_customer_password2.setText("");
			return;
		}
		if (!customer_password.equals(customer_password2)) {
			showInfo("两次密码输入不一致！请重新输入！");
			et_customer_name.setText("");
			et_customer_password.setText("");
			et_customer_password2.setText("");
			return;
		}
		if (customer_password.equals(customer_password2)) {
			String url = "http://1.ququcoupon.sinaapp.com/android_register.php?customer_name="
					+ customer_name + "&customer_password=" + customer_password;
			NetThread netThread = new NetThread(mHandler, url);
			netThread.start();
		}
	}


	private void showInfo(String string) {
		Toast.makeText(Register.this, string, Toast.LENGTH_SHORT).show();
	}

	static class MHandler extends Handler {
		WeakReference<Activity> mActivityReference;
		Context con;
		Register register ;

		MHandler(Activity activity,Register register) {
			mActivityReference = new WeakReference<Activity>(activity);
			con = activity;
			this.register = register;
		}

		@Override
		public void handleMessage(Message msg) {
			String result;
			final Activity activity = mActivityReference.get();
			if (activity != null) {
				result = msg.obj.toString();
				if (result.equals("0")) {
					register.showInfo("用户名已存在！");
					register.et_customer_name.setText("");
					register.et_customer_password.setText("");
					register.et_customer_password2.setText("");
					return;
				}
				if (result.equals("1")) {
					register.showInfo("注册成功！");
					((MyApplication)register.getApplication()).setuserID(register.customer_name);
					register.editor.remove("name");
					register.editor.remove("password");
					register.editor.putString("name", register.customer_name);
					register.editor.putString("password", register.customer_password);
					register.editor.commit();
					Intent intent = new Intent(register,
							MainActivity.class);
					register.startActivity(intent);
					register.finish();
				}
				if (result.equals("2")) {
					register.showInfo("注册失败！");
					register.et_customer_name.setText("");
					register.et_customer_password.setText("");
					register.et_customer_password2.setText("");
					return;
				}
			}
		}
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
