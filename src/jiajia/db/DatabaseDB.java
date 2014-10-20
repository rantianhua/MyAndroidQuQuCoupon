package jiajia.db;

/**
 * ����ר�ŷ������ݿ⣬�ṩһ����Ҫ�����ݿ⽻�� �ķ������������ݲ��룬��ѯ������ɾ���ĵȣ�
 * ������������Ҫ��ֻ��ʵ�������࣬������Ӧ��������
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
		// ����һ�����ݿ�
		this.context = context;
		helper = new SetupDatabase(context, DBName);
		db = helper.getReadableDatabase();
		close();
	}

	// ����һ������
	public void insertAll(String couponID, String couponContent,
			String startTime, String deadLine, String couponCount,
			String picUrl, String questionnaireUrl, String couponPrice,
			String originalPrice,String shopName, String shopAddress, String shopPhone) {

		// ��ContentValues�Ѵ���������ݴ�����
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
		// ��ÿɶ����ݿ�ʵ��
		helper = new SetupDatabase(context, DBName);
		db = helper.getWritableDatabase();
		try {

			// �����ݲ������ݿ�
			db.insert("coupon_db", null, allValue);
			// �ر����ݿ�
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
			Log.i("Database","�ر����ݿ�");
			db.close();
		}
	}

	// ��ѯ���ݿ⣬���ݴ��ݹ�����ͼƬ·��
	public String GetCouponID(String img_url) {
		String couponID = null;
		// ��ÿɶ����ݿ�ʵ��
		helper = new SetupDatabase(context, DBName);
		db = helper.getWritableDatabase();
		try {
			// ��ѯ���ݿ�
			cursor = db.rawQuery("select *from coupon_db where img_url = ?",
					new String[] { img_url });
			if (cursor.moveToNext()) {
				// �õ���Ӧ���Żݾ�id
				couponID = cursor.getString(cursor.getColumnIndex("coupon_id"));
			}
			close();
		} catch (Exception e) {
			Log.i("DB_GetCouponID",e.getMessage());
		}
		return couponID;
	}
	
	//�ж����ݿ����Ƿ��и�������
	public boolean isCouponIDExist(String couponID) {
		// ��ÿɶ����ݿ�ʵ��
		helper = new SetupDatabase(context, DBName);
		db = helper.getReadableDatabase();
		try{
			cursor = db.rawQuery("select *from coupon_db where coupon_id = ?",
					new String[] { couponID });
			if(cursor.moveToNext()) {
				close();
				System.out.println("�Ѵ���");
				return true;
			}	
		}catch(Exception e) {
			Log.i("isCouponIDExist",e.getMessage());
		}
		return false;
	}

	// �����Żݾ�ID���һ��item���������ݣ�����Щ���ݷ���һ��Map��
	public Map<String, String> getItemData(String couponID) {
		// ��ÿɶ����ݿ�ʵ��
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
	
	//ɾ��һ�м�¼
		public void deletRecord(String couponId) {
			// ��ÿɶ����ݿ�ʵ��
			helper = new SetupDatabase(context, DBName);
			db = helper.getWritableDatabase();
			//���ж��ļ�¼�Ƿ����
			if(isCouponIDExist(couponId)) {
				//ɾ���ü�¼
				try{
					db.delete("coupon_db","where coupon_id = ?", new String[] {couponId});
				}catch(Exception e) {
					Log.i("deletRecord", e.getMessage());
				}
			}
			//�ر����ݿ�
			close();
		}
}
