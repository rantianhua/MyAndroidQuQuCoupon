package jiajia.db;

/**
 * 此类专门访问数据库，提供一切需要与数据库交互 的方法，比如数据插入，查询，增、删、改等，
 * 其它的类有需要的只需实例化该类，调用相应方法即可
 */

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseDB {
	private SQLiteDatabase db = null;
	private SetupDatabase helper = null;
	private String DBName = "coupon_DB";
	private Cursor cursor;
	private Context context;
	private Map<String, String> mapItem;

	public DatabaseDB() {
	}

	public void CreateDatabase(Context context) {
		// 创建一个数据库
		this.context = context;
		helper = new SetupDatabase(context, DBName);
		db = helper.getReadableDatabase();
		close();
	}

	// 插入一行数据
	public void insertAll(String couponID, String couponContent,
			String startTime, String deadLine, String couponCount,
			String picUrl, String questionnaireUrl, String couponPrice,
			String originalPrice,String shopName, String shopAddress, String shopPhone) {

		// 用ContentValues把带插入的数据存起来
		ContentValues allValue = new ContentValues();
		allValue.put("coupon_id", couponID);
		allValue.put("coupon_content", couponContent);
		allValue.put("start_time", startTime);
		allValue.put("end_time", deadLine);
		allValue.put("coupons_amount", couponCount);
		allValue.put("img_url", picUrl);
		allValue.put("questionnaire_url", questionnaireUrl);
		allValue.put("coupon_price", couponPrice);
		allValue.put("original_price", originalPrice);
		allValue.put("shop_name", shopName);
		allValue.put("shop_address", shopAddress);
		allValue.put("phone_num", shopPhone);
		// 获得可读数据库实例
		helper = new SetupDatabase(context, DBName);
		db = helper.getWritableDatabase();
		try {

			// 将数据插入数据库
			db.insert("coupon_db", null, allValue);
			// 关闭数据库
			close();
		} catch (Exception e) {
			Log.i("insertAll",e.getMessage());
		}
	}

	public void close() {
		if (!(cursor == null)) {
			cursor.close();
		}
		if (!(helper == null)) {
			helper.close();
		}
		if (!(db == null)) {
			Log.i("Database","关闭数据库");
			db.close();
		}
	}

	// 查询数据库，根据传递过来的图片路径
	public String GetCouponID(String img_url) {
		String couponID = null;
		// 获得可读数据库实例
		helper = new SetupDatabase(context, DBName);
		db = helper.getWritableDatabase();
		try {
			// 查询数据库
			cursor = db.rawQuery("select *from coupon_db where img_url = ?",
					new String[] { img_url });
			if (cursor.moveToNext()) {
				// 得到对应的优惠卷id
				couponID = cursor.getString(cursor.getColumnIndex("coupon_id"));
			}
			close();
		} catch (Exception e) {
			Log.i("DB_GetCouponID",e.getMessage());
		}
		return couponID;
	}
	
	//判断数据库中是否有该条数据
	public boolean isCouponIDExist(String couponID) {
		// 获得可读数据库实例
		helper = new SetupDatabase(context, DBName);
		db = helper.getReadableDatabase();
		try{
			cursor = db.rawQuery("select *from coupon_db where coupon_id = ?",
					new String[] { couponID });
			if(cursor.moveToNext()) {
				close();
				System.out.println("已存在");
				return true;
			}	
		}catch(Exception e) {
			Log.i("isCouponIDExist",e.getMessage());
		}
		return false;
	}

	// 根据优惠卷ID获得一个item的所有数据，把这些数据放在一个Map中
	public Map<String, String> getItemData(String couponID) {
		// 获得可读数据库实例
		helper = new SetupDatabase(context, DBName);
		db = helper.getWritableDatabase();
		mapItem = new HashMap<String, String>();
		try {
			cursor = db.rawQuery("select *from coupon_db where coupon_id = ?",
					new String[] { couponID });
			if (cursor.moveToNext()) {
				mapItem.put("coupon_content", cursor.getString(cursor
						.getColumnIndex("coupon_content")));
				mapItem.put("start_time",
						cursor.getString(cursor.getColumnIndex("start_time")));
				mapItem.put("end_time",
						cursor.getString(cursor.getColumnIndex("end_time")));
				mapItem.put("coupons_amount", cursor.getString(cursor
						.getColumnIndex("coupons_amount")));
				mapItem.put("img_url",
						cursor.getString(cursor.getColumnIndex("img_url")));
				mapItem.put("questionnaire_url", cursor.getString(cursor
						.getColumnIndex("questionnaire_url")));
				mapItem.put("coupon_price",
						cursor.getString(cursor.getColumnIndex("coupon_price")));
				mapItem.put("original_price",
						cursor.getString(cursor.getColumnIndex("original_price")));
				mapItem.put("shop_name",
						cursor.getString(cursor.getColumnIndex("shop_name")));
				mapItem.put("shop_address",
						cursor.getString(cursor.getColumnIndex("shop_address")));
				mapItem.put("phone_num",
						cursor.getString(cursor.getColumnIndex("phone_num")));
			}
			close();
		} catch (Exception e) {
			Log.i("getItemData",e.getMessage());
		}
		return mapItem;
	}
	
	//删除一行记录
		public void deletRecord(String couponId) {
			// 获得可读数据库实例
			helper = new SetupDatabase(context, DBName);
			db = helper.getWritableDatabase();
			//先判读改记录是否存在
			if(isCouponIDExist(couponId)) {
				//删除该记录
				try{
					db.delete("coupon_db","where coupon_id = ?", new String[] {couponId});
				}catch(Exception e) {
					Log.i("deletRecord", e.getMessage());
				}
			}
			//关闭数据库
			close();
		}
}
