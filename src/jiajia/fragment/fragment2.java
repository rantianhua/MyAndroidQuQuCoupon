package jiajia.fragment;

import huahua.viewpager.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;

import jiajia.Analysis.Analysis2;
import jiajia.Thread.NetThread;
import jiajia.db.DatabaseDB;
import jiajia.fragment.fragment1.MyHandler;
import jiajia.other.Coupon;
import jiajia.other.MyAdapter;
import jiajia.other.MyApplication;
import jiajia.util.ImageLoader;
import jiajia.util.StandardImage;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;

public class fragment2 extends Fragment {
	private View mMainView;
	private String result = null;

	private String[] couponIDlist = null;

	private int[] stylelist = null;

	private Handler startHandler;

	private MyApplication mApp;
	
	MyAdapter myadapter;

	public ImageDownloadTask imagedownload;
	private ArrayList<ArrayList> coupon_groups = new ArrayList<ArrayList>();
	private ArrayList<Coupon> group0 = new ArrayList<Coupon>();
	private ArrayList<Coupon> group1 = new ArrayList<Coupon>();
	private ArrayList<Coupon> group2 = new ArrayList<Coupon>();

	// 处理访问网络下载图片的Handler（只有当缓存和本地文件都丢失图片的时候才会用到）
	private Handler handler;
	// 得到图片的对象
	private ImageLoader loader;

	public static fragment2 fg2 = null;

	// 数据库访问数据
	private DatabaseDB db;

	// hashMap用来保存从数据库中返回的数据
	private Map<String, String> mapItem;
	
	private static int i1 = 0,i2 = 0,i3 = 0;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		fg2 = this;
		loader = new ImageLoader(this.getActivity());
		mApp = (MyApplication) getActivity().getApplication();
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mMainView = inflater.inflate(R.layout.fragment2,
				(ViewGroup) getActivity().findViewById(R.id.viewpager), false);
		
		db = new DatabaseDB();
		db.CreateDatabase(this.getActivity());
		getData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup p = (ViewGroup) mMainView.getParent();
		if (p != null) {
			p.removeAllViewsInLayout();
		}

		return mMainView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		db.close();
	}

	@Override
	public void onPause() {

		super.onPause();
	}

	@Override
	public void onResume() {

		super.onResume();
	}

	@Override
	public void onStart() {

		super.onStart();
	}

	@Override
	public void onStop() {

		super.onStop();
	}

	// 获得数据并设置适配器
	public void getData() {
		// 从网络加载数据
		startHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				// 获取线程返回的结果
				result = msg.obj.toString().trim();
				refreshData(result);
				refreshAdapter();
			}
		};
		NetThread netthread = new NetThread(startHandler,
				"http://1.ququcoupon.sinaapp.com/mycoupons.php?customer_name="
						+ mApp.getuserID());
		netthread.start();
	}

	public void refreshAdapter() {
		// 适配器
		if (myadapter == null) {
			myadapter = new MyAdapter(coupon_groups, this);
			ExpandableListView expandListView = (ExpandableListView) mMainView
					.findViewById(R.id.expandableListView);
			expandListView.setAdapter(myadapter);			
			expandListView.setCacheColorHint(0x00000000);
			expandListView.setGroupIndicator(null);
		} else {
			myadapter.refreshData(coupon_groups);
		}
		/** 使用适配器 */
	}

	// 加载数据
	public void refreshData(String str) {
		// Analysis 是我自定义的一个解析返回数据的类，其中的方法get***list() 可以返回对应内容的String数组
		if(group0 != null) {
			group0.clear();
			group1.clear();
			group2.clear();
		}
		Analysis2 anal = new Analysis2(str);
		couponIDlist = anal.getcouponIDlist();
		stylelist = anal.getStylelist();
		Log.i("couponIDlist的长度",couponIDlist.length+"");
		for (int i = 0; i < couponIDlist.length; i++) {
			mapItem = db.getItemData(couponIDlist[i]);
			if (stylelist[i] == 0) {
				Coupon coupon = new Coupon(couponIDlist[i],
						mapItem.get("shop_name"), mapItem.get("coupon_price"),
						mapItem.get("original_price"),
						mapItem.get("start_time"), mapItem.get("end_time"),
						mapItem.get("img_url"));
				group0.add(i1,coupon);
				i1++;
			} else {
				if (stylelist[i] == 1) {
					Coupon coupon = new Coupon(couponIDlist[i],
							mapItem.get("shop_name"),
							mapItem.get("coupon_price"),
							mapItem.get("original_price"),
							mapItem.get("start_time"), mapItem.get("end_time"),
							mapItem.get("img_url"));
					group1.add(i2,coupon);
					i2++;
				} else {
					Coupon coupon = new Coupon(couponIDlist[i],
							mapItem.get("shop_name"),
							mapItem.get("coupon_price"),
							mapItem.get("original_price"),
							mapItem.get("start_time"), mapItem.get("end_time"),
							mapItem.get("img_url"));
					group2.add(i3,coupon);
					i3++;
				}
			}
		}
		coupon_groups.add(group0);
		coupon_groups.add(group1);
		coupon_groups.add(group2);
		i1 = 0;
		i2 = 0;
		i3 = 0;
	}

	// 异步加载图片
	public class ImageDownloadTask extends AsyncTask<Object, Object, Bitmap> {
		private ImageView imageView = null;
		private String imageUrl = null;
		
		public ImageDownloadTask(ImageView imageView, String url) {
			this.imageView = imageView;
			this.imageUrl = url;
		}
		
		@Override
		protected Bitmap doInBackground(Object... params) {
			Bitmap bmp = null;
			try{
				bmp = loader.getBitmapInLocal(imageUrl);
			}catch(Exception e) {
				e.printStackTrace();
			}
			if(bmp != null) {
				return bmp;
			}
			return null;
		}

		protected void onPostExecute(Bitmap bm) {
			if(bm != null) {
				if(imageView.getTag().equals(imageUrl)) {
					setImageView(imageView, bm);
				}
			}else {
				imageView.setBackgroundResource(R.drawable.loading_img);
				//网络下载
				Log.i("Wamg","网络下载");
				try{
					handler = new MyHandler(getActivity(),imageView,imageUrl);
					loader.loadImage(imageUrl,handler);
				}catch(Exception e) { 
					e.printStackTrace();
				}
			}
			
		}
		
		private void setImageView(ImageView v,Bitmap bm) {
			// 得到手机屏幕的宽度和高度
			DisplayMetrics metric = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
			int width = metric.widthPixels	/ 3; //图片的宽度设置为屏幕宽度的1/3（像素）
			int height = metric.heightPixels / 6;//图片的高度设置为屏幕宽度的1/6（像素）
			// 规格化Iamge
			StandardImage image = new StandardImage(bm, width, height);
			bm = image.setImage();
			// 将图片显示到ImageView里
			v.setImageBitmap(bm);
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
	    				int width = metric.widthPixels	/ 3; //图片的宽度设置为屏幕宽度的1/3（像素）
	    				int height = metric.heightPixels / 6;//图片的高度设置为屏幕宽度的1/6（像素）
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

}