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
	// �й����ж����͵�����
	private static XQuquerService xququerService;
	private static String dataToken;
	private static String dataContent;
	private static String MESSAGE;

	// �йظ�Activity�����������
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
	// ͼƬ�Ŀ�Ⱥ͸߶�
	private int width, height;

	protected void onCreate(Bundle savedInstanceState) {
		// ��ȡ��ǰʱ��
		Time t = new Time(); // or Time t=new Time("GMT+8"); ����Time Zone���ϡ�
		t.setToNow(); // ȡ��ϵͳʱ�䡣
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
		builder.setTitle("�Żݾ���ȡ���");
		loader = new ImageLoader(this);
		mapItem = new HashMap<String, String>();
		db = new DatabaseDB();
		db.CreateDatabase(this);
		mapItem = db.getItemData(couponID);
		//�ر����ݿ�
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

		couponPrice.setText("��" + mapItem.get("coupon_price"));
		originalPrice.setText("��" + mapItem.get("original_price"));
		shopName.setText(mapItem.get("shop_name"));
		couponContent.setText(mapItem.get("coupon_content"));
		couponCounts.setText(mapItem.get("coupons_amount"));
		phoneNum.setText(mapItem.get("phone_num"));
		shopAddress.setText(mapItem.get("shop_address"));
		time.setText(mapItem.get("start_time") + "-" + mapItem.get("end_time"));
		questionnaireUrl = mapItem.get("questionnaire_url");
		// �жϵ����ʾ��url�Ƿ����
		if (questionnaireUrl == null) {
			buttonDown.setText("��");
			buttonDown.setClickable(false);
		} else {
			buttonDown.setText("������� >");
		}

		if (couponState == 0) {
			// ��ʾ��ʹ�õ��Żݾ�
			buttonUp.setText("����ʹ��");
			buttonUp.setBackgroundResource(R.color.btn_useNow);
			buttonUp.setTag("����ʹ��");
			buttonUp.setOnClickListener(this);
		} else if (couponState == 1) {
			// ��ʾ�Żݾ���ʹ��
			buttonUp.setText("��ʹ��");
			buttonUp.setTag("��ʹ��");
			buttonUp.setBackgroundResource(R.color.btn_haveUse);
		} else if (couponState == 2) {
			// ��ʾ�Żݾ��ѹ���
			buttonUp.setText("�ѹ���");
			buttonUp.setBackgroundResource(R.color.btn_outTime);
		} else if (couponState == 3) {
			// ��ʾ��δ��ȡ�Żݾ�
			buttonUp.setText("��������");
			buttonUp.setTag("��������");
			buttonUp.setBackgroundResource(R.color.btn_getNow);
			buttonUp.setOnClickListener(this);
			buttonDown.setOnClickListener(this);
		}


		Bitmap bm = loader.getBitmapInLocal(mapItem.get("img_url"));

		// �õ��ֻ���Ļ�Ŀ��
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels; // ��Ļ��ȣ����أ�
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
		Log.i("Ҫ���͵�����", dataContent);
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
		Toast.makeText(this, "���ͳɹ�", Toast.LENGTH_LONG).show();
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
			if (buttonUp.getTag().equals("����ʹ��")) {
				useHandler = new UseHandler(this, builder);
				NetThread netthread = new NetThread(useHandler,
						"http://1.ququcoupon.sinaapp.com/deal_coupon.php?customer_name="
								+ mApp.getuserID() + "&coupon_id=" + couponID);
				netthread.start();
			} else if (buttonUp.getTag().equals("��������")) {
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

	// ��̬��Ϣ��
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
    				Log.i("URL","���سɹ�");
    				Bitmap bm = (Bitmap) msg.obj;
    				if(view.getTag().equals(url)) {
    					// �õ��ֻ���Ļ�Ŀ�Ⱥ͸߶�
	    				DisplayMetrics metric = new DisplayMetrics();
	    				activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
	    				int width = metric.widthPixels; //ͼƬ�Ŀ������Ϊ��Ļ��ȵ�1/3�����أ�
	    				int height = metric.heightPixels / 3;//ͼƬ�ĸ߶�����Ϊ��Ļ��ȵ�1/6�����أ�
	    				// ���Iamge
	    				StandardImage image = new StandardImage(bm, width, height);
	    				bm = image.setImage();
	    				// ��ͼƬ��ʾ��ImageView��
	    				view.setImageBitmap(bm);
    	
    				}
    				break;
    			case 1:
    				Log.i("URL","����ʧ��");
    				view.setBackgroundResource(R.drawable.loading_img);
    				break;	
    			default:
    				break;
    			}
            }
        }
    }

	// ��̬��Ϣ��
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
					builder.setMessage("��ȡ�ɹ�");
					builder.setPositiveButton("ȷ��", null);
					builder.show();
				} else {
					if (result.equals("4")) {
						builder.setMessage("���޴��Ż�ȯ");
						builder.setPositiveButton("ȷ��", null);
						builder.show();
					} else {
						if (result.equals("3")) {
							builder.setMessage("��ӵ�д��Ż�ȯ");
							builder.setPositiveButton("ȷ��", null);
							builder.show();
						} else {
							builder.setMessage("δ֪ԭ������ȡʧ��");
							builder.setPositiveButton("ȷ��", null);
							builder.show();
						}
					}

				}
			}
		}
	}

	// ��̬��Ϣ��
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
					builder.setMessage("��ʹ�û��ѹ���");
					builder.setPositiveButton("ȷ��", null);
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
