package huahua.viewpager;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import com.xququ.SAE_SDK.XQuquerService;
import com.xququ.SAE_SDK.XQuquerService.XQuquerListener;

import jiajia.Thread.NetThread;
import jiajia.db.DatabaseDB;
import jiajia.fragment.fragment2;

import jiajia.other.MyApplication;
import jiajia.util.ImageLoader;
import jiajia.util.StandardImage;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.Intent;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MyCouponActivity extends Activity implements OnClickListener,
		XQuquerListener {
	final String APPAccesskey = "123";
	final String APPSecretkey = "321";
	// 有关蛐蛐儿发送的引用
	private static XQuquerService xququerService;
	private static String dataToken;
	private static String dataContent;
	private static String MESSAGE;

	// 有关该Activity各组件的引用
	private static String couponID;
	private String questionnaireUrl;
	private int couponState;
	private TextView couponPrice, originalPrice, shopName, couponContent,
			couponCounts, phoneNum, shopAddress, time;
	private ImageView picture;
	private Button buttonUp, buttonDown;
	private static ProgressBar pb;
	private DatabaseDB db;
	private static MyApplication mApp;
	private Map<String, String> mapItem;
	AlertDialog.Builder builder;
	private static String TIME;
	 
	private Handler handler, clicHandler, useHandler;
	private ImageLoader loader;
	// 图片的宽度和高度
	private int width, height;

	protected void onCreate(Bundle savedInstanceState) {
		// 获取当前时间
		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。
		int year = t.year;
		int month = t.month + 1;
		int date = t.monthDay;
		TIME = "" + year + "-" + month + "-" + date + "*****";
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contain_main);
		mApp = (MyApplication) getApplication();
		Intent intent = getIntent();
		couponID = intent.getStringExtra("couponID");
		couponState = intent.getIntExtra("coupon_state", 3);
		builder = new AlertDialog.Builder(MyCouponActivity.this);
		builder.setTitle("优惠卷领取结果");
		loader = new ImageLoader(this);
		mapItem = new HashMap<String, String>();
		db = new DatabaseDB();
		db.CreateDatabase(this);
		mapItem = db.getItemData(couponID);
		//关闭数据库
		db.close();

		couponPrice = (TextView) findViewById(R.id.textView_couponPrice_information);
		originalPrice = (TextView) findViewById(R.id.textView_originalPrice_information);
		shopName = (TextView) findViewById(R.id.textView_shopName_information);
		couponContent = (TextView) findViewById(R.id.textView_couponCotent_information);
		couponCounts = (TextView) findViewById(R.id.information_textView_counts);
		phoneNum = (TextView) findViewById(R.id.information_phoneNumber_textView);
		shopAddress = (TextView) findViewById(R.id.information_address_textView);
		time = (TextView) findViewById(R.id.information_time_textView);
		picture = (ImageView) findViewById(R.id.pic_information);
		buttonUp = (Button) findViewById(R.id.btn_getOrOthers_information);
		buttonDown = (Button) findViewById(R.id.btn_forQustionnaire_information);
		pb = (ProgressBar) findViewById(R.id.progress);

		couponPrice.setText("￥" + mapItem.get("coupon_price"));
		originalPrice.setText("￥" + mapItem.get("original_price"));
		shopName.setText(mapItem.get("shop_name"));
		couponContent.setText(mapItem.get("coupon_content"));
		couponCounts.setText(mapItem.get("coupons_amount"));
		phoneNum.setText(mapItem.get("phone_num"));
		shopAddress.setText(mapItem.get("shop_address"));
		time.setText(mapItem.get("start_time") + "-" + mapItem.get("end_time"));
		questionnaireUrl = mapItem.get("questionnaire_url");
		// 判断调查问卷的url是否存在
		if (questionnaireUrl == null) {
			buttonDown.setText("无");
			buttonDown.setClickable(false);
		} else {
			buttonDown.setText("点击进入 >");
		}

		if (couponState == 0) {
			// 表示待使用的优惠卷
			buttonUp.setText("立即使用");
			buttonUp.setBackgroundResource(R.color.btn_useNow);
			buttonUp.setTag("立即使用");
			buttonUp.setOnClickListener(this);
		} else if (couponState == 1) {
			// 表示优惠卷已使用
			buttonUp.setText("已使用");
			buttonUp.setTag("已使用");
			buttonUp.setBackgroundResource(R.color.btn_haveUse);
		} else if (couponState == 2) {
			// 表示优惠卷已过期
			buttonUp.setText("已过期");
			buttonUp.setBackgroundResource(R.color.btn_outTime);
		} else if (couponState == 3) {
			// 表示还未获取优惠卷
			buttonUp.setText("立即抢购");
			buttonUp.setTag("立即抢购");
			buttonUp.setBackgroundResource(R.color.btn_getNow);
			buttonUp.setOnClickListener(this);
			buttonDown.setOnClickListener(this);
		}


		Bitmap bm = loader.getBitmapInLocal(mapItem.get("img_url"));

		// 得到手机屏幕的宽度
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels; // 屏幕宽度（像素）
		height = metric.heightPixels / 3;
		if (bm != null) {
			setImageView(bm, width, picture);
		} else {
			picture.setBackgroundResource(R.drawable.loading_img);
			handler = new MyHandler(this, picture, mapItem.get("img_url"));
			loader.loadImage(mapItem.get("img_url"), handler);
		}

		xququerService = XQuquerService.getInstance();
		xququerService.setAccesskeyAndSecretKey(APPAccesskey, APPSecretkey);
	}

	private void setImageView(Bitmap bm, int width, ImageView v) {
		StandardImage standard = new StandardImage(bm, width, height);
		bm = standard.setImage();
		v.setImageBitmap(bm);
	}

	protected void onStart() {
		super.onStart();
		xququerService.start(this);
	}

	protected void onStop() {
		super.onStop();
		xququerService.stop();
	}

	private static void send() {
		dataContent = MESSAGE;
		Log.i("要发送的内容", dataContent);
		Thread thread = new Thread() {
			public void run() {
				dataToken = xququerService.uploadData(dataContent);
				xququerService.sendDataToken(dataToken);
			}
		};
		thread.start();
	}

	@Override
	public void onRecv(String arg0) {

	}

	@Override
	public void onSend() {
		pb.setVisibility(View.GONE);
		Toast.makeText(this, "发送成功", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_forQustionnaire_information:
			Intent intent = new Intent();
			intent.setClass(this, WebViewActivity.class);
			intent.putExtra("questionnaire_url",
					mapItem.get("questionnaire_url"));
			startActivity(intent);
			break;
		case R.id.btn_getOrOthers_information:
			pb.setVisibility(View.VISIBLE);
			if (buttonUp.getTag().equals("立即使用")) {
				useHandler = new UseHandler(this, builder);
				NetThread netthread = new NetThread(useHandler,
						"http://1.ququcoupon.sinaapp.com/deal_coupon.php?customer_name="
								+ mApp.getuserID() + "&coupon_id=" + couponID);
				netthread.start();
			} else if (buttonUp.getTag().equals("立即抢购")) {
				clicHandler = new ClickHandler(this, builder);
				NetThread netthread = new NetThread(clicHandler,
						"http://1.ququcoupon.sinaapp.com/get_coupon.php?customer_name="
								+ mApp.getuserID() + "&coupon_id=" + couponID);
				netthread.start();
			}
			break;
		default:
			break;
		}

	}

	// 静态消息类
	static class MyHandler extends Handler {
        WeakReference<Activity > mActivityReference;
        ImageView view;
        String url;
        MyHandler(Activity activity,ImageView v,String imageUrl) {
            mActivityReference= new WeakReference<Activity>(activity);
            view = v;
            url = imageUrl;
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mActivityReference.get();
            if (activity != null) {
            	super.handleMessage(msg);
            	switch (msg.what) {
    			case 0:
    				Log.i("URL","下载成功");
    				Bitmap bm = (Bitmap) msg.obj;
    				if(view.getTag().equals(url)) {
    					// 得到手机屏幕的宽度和高度
	    				DisplayMetrics metric = new DisplayMetrics();
	    				activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
	    				int width = metric.widthPixels; //图片的宽度设置为屏幕宽度的1/3（像素）
	    				int height = metric.heightPixels / 3;//图片的高度设置为屏幕宽度的1/6（像素）
	    				// 规格化Iamge
	    				StandardImage image = new StandardImage(bm, width, height);
	    				bm = image.setImage();
	    				// 将图片显示到ImageView里
	    				view.setImageBitmap(bm);
    	
    				}
    				break;
    			case 1:
    				Log.i("URL","下载失败");
    				view.setBackgroundResource(R.drawable.loading_img);
    				break;	
    			default:
    				break;
    			}
            }
        }
    }

	// 静态消息类
	static class ClickHandler extends Handler {
		WeakReference<Activity> mActivityReference;
		AlertDialog.Builder builder;

		ClickHandler(Activity activity, AlertDialog.Builder builder) {
			mActivityReference = new WeakReference<Activity>(activity);
			this.builder = builder;
		}

		@Override
		public void handleMessage(Message msg) {
			final Activity activity = mActivityReference.get();
			pb.setVisibility(View.GONE);
			if (activity != null) {
				super.handleMessage(msg);
				String result = msg.obj.toString().trim();
				if (result.equals("1")) {
					builder.setMessage("领取成功");
					builder.setPositiveButton("确定", null);
					builder.show();
				} else {
					if (result.equals("4")) {
						builder.setMessage("已无此优惠券");
						builder.setPositiveButton("确定", null);
						builder.show();
					} else {
						if (result.equals("3")) {
							builder.setMessage("已拥有此优惠券");
							builder.setPositiveButton("确定", null);
							builder.show();
						} else {
							builder.setMessage("未知原因导致领取失败");
							builder.setPositiveButton("确定", null);
							builder.show();
						}
					}

				}
			}
		}
	}

	// 静态消息类
	static class UseHandler extends Handler {
		WeakReference<Activity> mActivityReference;
		AlertDialog.Builder builder;

		UseHandler(Activity activity, AlertDialog.Builder builder) {
			mActivityReference = new WeakReference<Activity>(activity);
			this.builder = builder;
		}

		@Override
		public void handleMessage(Message msg) {
			final Activity activity = mActivityReference.get();
			pb.setVisibility(View.GONE);
			if (activity != null) {
				super.handleMessage(msg);

				String result1 = msg.obj.toString().trim();
				if (result1.equals("1")) {
					MESSAGE = mApp.getuserID() + "*" + couponID;
					send();
				} else {
					builder.setMessage("已使用或已过期");
					builder.setPositiveButton("确定", null);
					builder.show();
				}

			}
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			fragment2.fg2.getData();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
