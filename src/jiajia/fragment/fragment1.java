package jiajia.fragment;

import huahua.viewpager.MyCouponActivity;
import huahua.viewpager.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jiajia.Analysis.Analysis;
import jiajia.Thread.NetThread1;
import jiajia.db.DatabaseDB;
import jiajia.other.MyListView;
import jiajia.util.ImageLoader;
import jiajia.util.StandardImage;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class fragment1 extends Fragment implements OnItemClickListener,
		jiajia.other.MyListView.OnRefreshListener,
		jiajia.other.MyListView.LoadListener {
	private View mMainView;
	private SimpleAdapter myAdapter;
	private MyListView listview = null;
	private ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private static Handler startHandler, upHandler, downHandler,imageHandler;
	private String firstResult;// 第一次进入加载的result
	private String result = null;
	private String[] picUrllist = null;
	private String[] shopNamelist = null;
	private String[] couponIDlist = null;
	private String[] couponContentlist = null;
	private String[] shopAddresslist = null;
	private String[] shopPhonelist = null;
	private String[] questionnaireUrllist = null;
	private String[] startTimelist = null;
	private String[] deadLinelist = null;
	private String[] couponCountlist = null;
	private String[] pricelist = null;
	private String[] originalPicelist = null;

	private String[] sslist = { "pic", "shopName", "couponPrice",
			"originalPrice", "couponContent", "couponCount" ,"couponTime"};
	private int[] iilist = { R.id.pic_listItem, R.id.shopName, R.id.couponPrice,
			R.id.originalPrice,R.id.couponContent, R.id.textView_coupon_counts, R.id.textView_time};
	private int n = 2;

	// 数据库
	private DatabaseDB db;
	
	private ImageLoader loader;
	
	private fragment1 frag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mMainView = inflater.inflate(R.layout.fragment1,
				(ViewGroup) getActivity().findViewById(R.id.viewpager), false);

		listview = (MyListView) mMainView.findViewById(R.id.listview1);
		db = new DatabaseDB();
		db.CreateDatabase(this.getActivity());
		loader = new ImageLoader(this.getActivity());
		frag = this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 第一次进入载入数据
		startHandler = new StartHandler(getActivity(), getActivity(), listview, result, mData, frag);
		// 用于访问网络的线程
		NetThread1 netthread1 = new NetThread1(startHandler, 1);
		netthread1.start();
		upHandler = new UpHandler(getActivity(), getActivity(), listview, result, mData, frag);
		// 底部上拉加载数据
		downHandler = new DownLoadHandler(getActivity(), getActivity(), listview, result, mData, frag);
		
		
		
	

		listview.setOnItemClickListener(this);
		listview.setonRefreshListener(this);
		listview.setLoadListener(this);

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

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), MyCouponActivity.class);
		intent.putExtra("couponID", couponIDlist[position - 1]);
		//传递一个coupon_state过去，用于在详情页面与待使用，已使用、已过期进行区分
		intent.putExtra("coupon_state", 3);
		getActivity().startActivity(intent);
	}

	@Override
	public void onRefresh() {
		new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// 下拉刷新访问网络获取数据
				NetThread1 netthread2 = new NetThread1(upHandler, 1);
				netthread2.start();
			}
		}.execute(null, null, null);
	}

	@Override
	public void onLoad() {
		new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) {
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// 下拉刷新访问网络获取数据
				NetThread1 netthread3 = new NetThread1(downHandler, n);
				n++;
				netthread3.start();
			}
		}.execute(null, null, null);
	}



	// 异步加载图片
	private class ImageDownloadTask extends AsyncTask<Object, Object, Bitmap> {
		private ImageView imageView = null;
		private String imageUrl = null;
		@Override
		protected Bitmap doInBackground(Object... params) {
			Bitmap bmp = null;
			imageView = (ImageView) params[1];
			imageUrl = (String) params[0];
			System.out.println("地址："+imageUrl);
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
					imageHandler = new MyHandler(getActivity(),imageView,imageUrl);
					loader.loadImage(imageUrl,imageHandler);
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
	 
	 static class UpHandler extends Handler {
	        WeakReference<Activity > mActivityReference;
	        Context con;
	        MyListView lView;
	        String result;
	        ArrayList<Map<String, Object>> mData;
	        fragment1 frag;

	        UpHandler(Activity activity,Context context,MyListView listView,String str,ArrayList<Map<String, Object>> data,fragment1 frag1) {
	            mActivityReference= new WeakReference<Activity>(activity);
	            con = context;
	            lView = listView;
	            result = str;
	            mData = data;
	            frag = frag1;
	        }

	        @Override
	        public void handleMessage(Message msg) {
	            final Activity activity = mActivityReference.get();
	            if (activity != null) {
	            	String result = msg.obj.toString().trim();
	            	Analysis anal = new Analysis(result);
	            	if (result.equals("fail")) {
						Toast.makeText(con, "无优惠信息,敬请期待",
								Toast.LENGTH_LONG).show();
					} else {
						if (anal.getcouponIDlist()[0].equals(frag.firstResult)) {
							Toast.makeText(con, "已是最新数据",
									Toast.LENGTH_LONG).show();
						} else {
							// 获取线程返回的结果
							frag.upData(result, frag.firstResult);
							frag.firstResult = anal.getcouponIDlist()[0];
						}
					}
					lView.onRefreshComplete();
	            }
	        }
	    }
	 
	 static class StartHandler extends Handler {
	        WeakReference<Activity > mActivityReference;
	        Context con;
	        MyListView lView;
	        String result;
	        ArrayList<Map<String, Object>> mData;
	        fragment1 frag;

	        StartHandler(Activity activity,Context context,MyListView listView,String str,ArrayList<Map<String, Object>> data,fragment1 frag1) {
	            mActivityReference= new WeakReference<Activity>(activity);
	            con = context;
	            lView = listView;
	            result = str;
	            mData = data;
	            frag = frag1;
	        }
           
	        @Override
	        public void handleMessage(Message msg) {
	            final Activity activity = mActivityReference.get();
	            if (activity != null) {
	            	if (msg.obj.toString().trim().equals("fail")) {
						Toast.makeText(con, "无优惠信息,敬请期待",
								Toast.LENGTH_LONG).show();
						lView.LoadComplete();
					} else {
						// 获取线程返回的结果
						result = msg.obj.toString().trim();
						Analysis anal = new Analysis(result);
						frag.firstResult = anal.getcouponIDlist()[0];
						frag.startData(mData, result);
					}
	            }
	        }
	    }
	 
	 static class DownLoadHandler extends Handler {
	        WeakReference<Activity > mActivityReference;
	        Context con;
	        MyListView lView;
	        String result;
	        ArrayList<Map<String, Object>> mData;
	        fragment1 frag;

	        DownLoadHandler(Activity activity,Context context,MyListView listView,String str,ArrayList<Map<String, Object>> data,fragment1 frag1) {
	            mActivityReference= new WeakReference<Activity>(activity);
	            con = context;
	            lView = listView;
	            result = str;
	            mData = data;
	            frag = frag1;
	        }

	        @Override
	        public void handleMessage(Message msg) {
	            final Activity activity = mActivityReference.get();
	            if (activity != null) {
	            	if (msg.obj.toString().trim().equals("fail")) {
						Toast.makeText(con, "无更多信息", Toast.LENGTH_LONG)
								.show();
						lView.LoadComplete();
					} else {
						// 获取线程返回的结果
						result = msg.obj.toString().trim();
						frag.downData(result);
					}
					lView.LoadComplete();
	            }
	        }
	    }
		// 用于初次加载mData的方法
		public void startData(ArrayList<Map<String, Object>> mData, String str) {
			// Analysis 是我自定义的一个解析返回数据的类，其中的方法get***list() 可以返回对应内容的String数组
			Analysis anal = new Analysis(str);
			picUrllist = anal.getpicUrllsit();
			shopNamelist = anal.getshopNamelist();
			couponIDlist = anal.getcouponIDlist();
			couponContentlist = anal.getcouponContentlist();
			shopAddresslist = anal.getaddresslist();
			shopPhonelist = anal.getshopPhonelist();
			questionnaireUrllist = anal.getquestionnaireUrllist();
			startTimelist = anal.getstartTimelist();
			deadLinelist = anal.getdeadLinelist();
			couponCountlist = anal.getcouponCountlist();
			pricelist = anal.getCouponPricelist();
			originalPicelist = anal.getOriginalPricelist();
			mData.clear();
			for (int i = 0; i < startTimelist.length; i++) {
				Map<String, Object> item = new HashMap<String, Object>();
				System.out.println("picurl:"+picUrllist[i]);
				System.out.println("originalPicelist[i]"+originalPicelist[i]);
				item.put("pic", picUrllist[i]);
				item.put("shopName", shopNamelist[i]);
				item.put("couponPrice","￥" + pricelist[i]);
				item.put("originalPrice","￥" + originalPicelist[i]);
				item.put("couponContent", couponContentlist[i]);
				item.put("couponCount", couponCountlist[i]);
				item.put("couponTime", startTimelist[i]+"-"+deadLinelist[i]);
				mData.add(item);
				if(!db.isCouponIDExist(couponIDlist[i])) {
					db.insertAll(couponIDlist[i], couponContentlist[i],
							startTimelist[i], deadLinelist[i], couponCountlist[i],
							picUrllist[i], questionnaireUrllist[i], pricelist[i],
							originalPicelist[i],shopNamelist[i], shopAddresslist[i], shopPhonelist[i]);
				}
			}
			showList(mData);
		}

		//分页加载数据并刷新
		public void downData(String result) {
			Analysis anal = new Analysis(result);
			picUrllist = listAdd(picUrllist, anal.getpicUrllsit());
			shopNamelist = listAdd(shopNamelist, anal.getshopNamelist());
			couponIDlist = listAdd(couponIDlist, anal.getcouponIDlist());
			couponContentlist = listAdd(couponContentlist,
					anal.getcouponContentlist());
			shopAddresslist = listAdd(shopAddresslist,
					anal.getaddresslist());
			shopPhonelist = listAdd(shopPhonelist,
					anal.getshopPhonelist());
			questionnaireUrllist = listAdd(questionnaireUrllist,
					anal.getquestionnaireUrllist());
			startTimelist = listAdd(startTimelist,
					anal.getstartTimelist());
			deadLinelist = listAdd(deadLinelist, anal.getdeadLinelist());
			couponCountlist = listAdd(couponCountlist,
					anal.getcouponCountlist());
			pricelist = listAdd(pricelist, anal.getCouponPricelist());
			originalPicelist = listAdd(originalPicelist,anal.getOriginalPricelist());
			
			mData.clear();
			for (int i = 0; i < startTimelist.length; i++) {
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("pic", picUrllist[i]);
				item.put("shopName", shopNamelist[i]);
				item.put("couponPrice","￥" + pricelist[i]);
				item.put("originalPrice","￥" + originalPicelist[i]);
				item.put("couponContent", couponContentlist[i]);
				item.put("couponCount", couponCountlist[i]);
				item.put("couponTime", startTimelist[i]+"-"+deadLinelist[i]);
				mData.add(item);
				if(!db.isCouponIDExist(couponIDlist[i])) {
					db.insertAll(couponIDlist[i], couponContentlist[i],
							startTimelist[i], deadLinelist[i],
							couponCountlist[i], picUrllist[i],
							questionnaireUrllist[i], pricelist[i],
							originalPicelist[i],shopNamelist[i], shopAddresslist[i],
							shopPhonelist[i]);
				}
			}
			showList(mData);
			Toast.makeText(getActivity(), "加载成功", Toast.LENGTH_LONG)
					.show();
		}
		// 分页加载更新数组的方法
		public String[] listAdd(String[] strlist1, String[] strlist2) {
			String[] strlist3 = new String[strlist1.length + strlist2.length];
			for (int i = 0; i < strlist1.length; i++)
				strlist3[i] = strlist1[i];
			for (int i = 0; i < strlist2.length; i++)
				strlist3[strlist1.length + i] = strlist2[i];
			return strlist3;
		}
		
		public void upData(String result,String firstID) {
			Analysis anal = new Analysis(result);
			String[] idlist = anal.getcouponIDlist();
			int i = 0;
            while(i<10&&idlist[i]!=firstID)
            {
            	picUrllist = listAddone(picUrllist,anal.getpicUrllsit()[i]);
            	shopNamelist = listAddone(shopNamelist,anal.getshopNamelist()[i]);
            	couponIDlist = listAddone(couponIDlist,anal.getcouponIDlist()[i]);
            	couponContentlist = listAddone(couponContentlist,anal.getcouponContentlist()[i]);
            	shopAddresslist = listAddone(shopAddresslist,anal.getaddresslist()[i]);
            	shopPhonelist = listAddone(shopPhonelist,anal.getshopPhonelist()[i]);
            	questionnaireUrllist = listAddone(questionnaireUrllist,anal.getquestionnaireUrllist()[i]);
            	startTimelist = listAddone(startTimelist,anal.getstartTimelist()[i]);
            	deadLinelist = listAddone(deadLinelist,anal.getdeadLinelist()[i]);
            	couponCountlist = listAddone(couponCountlist,anal.getcouponCountlist()[i]);
            	pricelist = listAddone(pricelist,anal.getCouponPricelist()[i]);
            	originalPicelist = listAddone(originalPicelist,anal.getOriginalPricelist()[i]);
            	i++;
            }
            mData.clear();
			for (int j = 0; j < startTimelist.length; j++) {
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("pic", picUrllist[j]);
				item.put("shopName", shopNamelist[j]);
				item.put("couponPrice","￥" + pricelist[j]);
				item.put("originalPrice","￥" + originalPicelist[j]);
				item.put("couponContent", couponContentlist[j]);
				item.put("couponCount", couponCountlist[j]);
				item.put("couponTime", startTimelist[j]+"-"+deadLinelist[j]);
				mData.add(item);
				if(!db.isCouponIDExist(couponIDlist[j])) {
					db.insertAll(couponIDlist[j], couponContentlist[j],
							startTimelist[j], deadLinelist[j],
							couponCountlist[j], picUrllist[j],
							questionnaireUrllist[i], pricelist[j],
							originalPicelist[j],shopNamelist[j], shopAddresslist[j],
							shopPhonelist[j]);
				}
			}
			showList(mData);
			Toast.makeText(getActivity(), "加载成功", Toast.LENGTH_LONG)
					.show();
		}
		// 下拉刷新更新数组的方法
		public String[] listAddone(String[] strlist,String str) {
			String[] strlist1 = new String[strlist.length+1];
			strlist1[0] = str;
			for(int i = 1; i < strlist1.length-1; i++)
			{
				strlist1[i] = strlist[i];
			}
			return strlist1;
		}
		
		// 用于更新listview的方法
		public void showList(ArrayList<Map<String, Object>> mData) {
			if (myAdapter == null) {
				myAdapter = new SimpleAdapter(getActivity(), mData, R.layout.item,
						sslist, iilist) {
					@Override
					public void setViewImage(ImageView v, String value) {
						v.setTag(value);
						new ImageDownloadTask().execute(value, v);
					}
				};
				listview.setAdapter(myAdapter);
			} else {
				 myAdapter.notifyDataSetChanged();
			}
		}	
}
