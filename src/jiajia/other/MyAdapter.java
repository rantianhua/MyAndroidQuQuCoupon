package jiajia.other;

import huahua.viewpager.MainActivity;
import huahua.viewpager.MyCouponActivity;
import huahua.viewpager.R;

import java.util.ArrayList;

import jiajia.Thread.NetThread;
import jiajia.db.DatabaseDB;
import jiajia.fragment.fragment2;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;

public class MyAdapter extends BaseExpandableListAdapter {

	private ArrayList<ArrayList> coupon_groups = new ArrayList<ArrayList>();
	private fragment2 fg2;
	private String[] types = new String[] { "待使用", "已使用", "已过期" };
	private Coupon coupon;
	private static Button button;
	// 动画
	private AnimationSet animationSet;
	private AlphaAnimation alpha;
	private TranslateAnimation translate;
	
	private Handler han;
	//数据库
	private DatabaseDB db;
	private static MyApplication mApp;

	public MyAdapter(ArrayList<ArrayList> coupon_groups, fragment2 fg2) {
		this.coupon_groups = coupon_groups;
		this.fg2 = fg2;
		button = MainActivity.buttonDel;
		db = new DatabaseDB();
		db.CreateDatabase(fg2.getActivity());
		mApp = (MyApplication) fg2.getActivity().getApplication();
	}

	// 初始化动画的方法
	public void initAnimation() {
		animationSet = new AnimationSet(true);
		alpha = new AlphaAnimation(1, 0); // 淡出动画
		animationSet.addAnimation(alpha);
		translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
				0f, Animation.RELATIVE_TO_SELF, 0f); // 向左平移动画
		animationSet.addAnimation(translate);
		animationSet.setDuration(500);
	}

	public void refreshData(ArrayList<ArrayList> coupon_groups) {
		System.out.println("刷新数据");
		this.coupon_groups = coupon_groups;
		this.notifyDataSetChanged();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		System.out.println(coupon_groups.get(childPosition).size());
		return coupon_groups.get(childPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		if (coupon_groups.isEmpty())
			return 0;
		else
			return coupon_groups.get(groupPosition).size();
	}

	private TextView getTextView() {
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 64);
		TextView textView = new TextView(fg2.getActivity());
		textView.setLayoutParams(lp);
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		textView.setPadding(45, 0, 0, 0);
		return textView;
	}

	/*
	 * 该方法决定每个子选项的外观
	 */
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			coupon = (Coupon) coupon_groups.get(groupPosition).get(
					childPosition);
			LayoutInflater mInflater = LayoutInflater.from(fg2.getActivity());
			convertView = mInflater.inflate(R.layout.item_mycoupon, null);
			holder.pic = (ImageView) convertView
					.findViewById(R.id.pic_listItemInMyCoupon);
			holder.shopName = (TextView) convertView
					.findViewById(R.id.shopName_myCoupon);
			holder.time = (TextView) convertView
					.findViewById(R.id.textView_timeInMyCoupon);
			holder.price = (TextView) convertView
					.findViewById(R.id.couponPrice_myCoupon);
			holder.originalPrice = (TextView) convertView
					.findViewById(R.id.originalPrice_myCoupon);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		coupon = (Coupon) coupon_groups.get(groupPosition).get(
				childPosition);
		holder.pic.setTag(coupon.getImageUrl());
		fg2.imagedownload = fg2.new ImageDownloadTask(holder.pic,
				coupon.getImageUrl());
		fg2.imagedownload.execute();

		holder.shopName.setText(coupon.getShopName());
		holder.time.setText(coupon.getStartTime() + "-"
				+ coupon.getEndTime());
		holder.price.setText("￥" + coupon.getCouponPrice());
		holder.originalPrice.setText("￥" + coupon.getOriginalPrice());
		
		
		// final ArrayList<ArrayList> coupon_groups2 = coupon_groups;
		final int group = groupPosition;
		final int child = childPosition;
		final View vDel = convertView;
		final String couponID = coupon.getCoupon_id();
		
		convertView.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				if (button != null) {
					button.setVisibility(View.VISIBLE);
					button.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
								han = initHandler(vDel, group, child,couponID);
								//访问后台删除相应数据
								try{
									String url = "http://1.ququcoupon.sinaapp.com/del_coupon.php?customer_name="+ mApp.getuserID()+"&coupon_id="+couponID;
									NetThread net = new NetThread(han,url);
									net.start();
								}catch(Exception e) {
									Log.i("ONLongClick",e.getMessage());
								}							
							}
						
					});
					return true;
				} else {
					return false;
				}
			}
		});

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (button != null) {
					button.setVisibility(View.GONE);
				}
				System.out.println("dianjiButton");
				Intent intent = new Intent();
				intent.setClass(fg2.getActivity(), MyCouponActivity.class);
				intent.putExtra("couponID", coupon.getCoupon_id());
				// 将item的coupon_state传递过去，以便在MyCouponActivity得知该优惠卷的状态（待使用、已使用、已过期）
				intent.putExtra("coupon_state", group);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				fg2.getActivity().startActivity(intent);
			}
		});

		return convertView;
	}

	// 删除子view的方法
	public void deleteItem(final View v, final int group, final int child) {
		if (animationSet == null) {
			initAnimation();
		}
		animationSet.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				button.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				System.out.println(group + "   " + child);
				System.out.println(coupon_groups.get(group).get(child)
						.toString());
				System.out.println("删除前有："+getChildrenCount(group));
				coupon_groups.get(group).remove(child);
				System.out.println("删除后有："+getChildrenCount(group));
				refreshData(coupon_groups);

			}
		});
		v.startAnimation(animationSet);
	}

	// 存放View各组件的ViewHolder
	static class ViewHolder {
		TextView shopName, time, price, originalPrice;
		ImageView pic;
	};

	/*
	 * 获取指定组位置处的组数据(non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroup(int)
	 */

	public Object getGroup(int groupPosition) {

		return types[groupPosition];
	}

	public int getGroupCount() {

		return types.length;
	}

	@Override
	public long getGroupId(int groupPosition) {

		return groupPosition;
	}

	/*
	 * 该方法决定每个组选项的外观
	 */
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		LinearLayout ll = new LinearLayout(fg2.getActivity());
		ll.setOrientation(0);
		ll.setMinimumHeight(80);
		ll.setGravity(Gravity.CENTER);
		TextView textView = getTextView();
		textView.setText("   " + getGroup(groupPosition).toString());
		textView.setTextColor(Color.WHITE);
		textView.setTextSize(20);
		switch (groupPosition) {
		case 0:
			textView.setBackgroundResource(R.color.a);
			
			break;
		case 1:
			textView.setBackgroundResource(R.color.b);
			
			break;
		case 2:
			textView.setBackgroundResource(R.color.c);
			
			break;
		default:
			break;
		}
		ll.addView(textView);
		return ll;
	}

	@Override
	public boolean hasStableIds() {

		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	
	public Handler initHandler(final View v,final int group,final int child,final String couponID) {
		Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(msg.obj.equals("0")) {
					//删除后台数据失败
					Toast.makeText(fg2.getActivity(), "删除数据失败，请检查网络是否可用！", Toast.LENGTH_SHORT).show();
					if (button != null ) {
						button.setVisibility(View.GONE);
					}
				}else if(msg.obj.equals("1")) {
					//删除后台数据成功
					db.deletRecord(couponID);
					db.close();
					deleteItem(v,group, child);			
				}
			}
			
		};
		return handler;
	}
}
