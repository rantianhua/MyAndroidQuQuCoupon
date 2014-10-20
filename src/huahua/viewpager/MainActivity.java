package huahua.viewpager;

import java.util.ArrayList;

import jiajia.fragment.fragment1;
import jiajia.fragment.fragment2;
import jiajia.other.MyApplication;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	private long exitTime = 0;
	private ViewPager m_vp;
	private fragment1 mfragment1;
	private fragment2 mfragment2;
	//页面列表
	private ArrayList<Fragment> fragmentList;
	//标题列表
	ArrayList<String>   titleList    = new ArrayList<String>();
	//通过pagerTabStrip可以设置标题的属性
	private PagerTabStrip pagerTabStrip;
	
	//删除按钮
	public static Button buttonDel;

	@SuppressLint("InlinedApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		m_vp = (ViewPager)findViewById(R.id.viewpager);
		
		pagerTabStrip=(PagerTabStrip) findViewById(R.id.pagertab);
		//设置下划线的颜色
		pagerTabStrip.setTabIndicatorColor(getResources().getColor(android.R.color.holo_green_dark)); 
		
		buttonDel = (Button)findViewById(R.id.button_delete_main);
		mfragment1 = new fragment1();
		mfragment2 = new fragment2();

		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(mfragment1);
		fragmentList.add(mfragment2);
		
	    titleList.add("公告 ");
	    titleList.add("我的优惠券");
		
		m_vp.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
//		m_vp.setOffscreenPageLimit(2);
	}
	
	public class MyViewPagerAdapter extends FragmentPagerAdapter{
		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public Fragment getItem(int arg0) {
			return fragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return titleList.get(position);
		}

		
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				MyApplication mApp = (MyApplication)getApplication(); 
				mApp.setExit(true); 
				finish(); 
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
