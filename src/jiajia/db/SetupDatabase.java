package jiajia.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SetupDatabase extends SQLiteOpenHelper {

	private static final int VERSON = 1;// 默认的数据库版本

	// 继承SQLiteOpenHelper类的类必须有自己的构造函数
	// 该构造函数4个参数，直接调用父类的构造函数。其中第一个参数为该类本身；第二个参数为数据库的名字；
	// 第3个参数是用来设置游标对象的，这里一般设置为null；参数四是数据库的版本号。
	public SetupDatabase(Context context, String name, CursorFactory factory,
			int verson) {
		super(context, name, factory, verson);
	}

	// 该构造函数有3个参数，因为它把上面函数的第3个参数固定为null了
	public SetupDatabase(Context context, String name, int verson) {
		this(context, name, null, verson);
	}

	// 该构造函数只有2个参数，在上面函数 的基础上将版本号固定了
	public SetupDatabase(Context context, String name) {
		this(context, name, VERSON);
	}

	// 该函数在数据库第一次被建立时调用
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		System.out.println("create a sqlite database");
		// 第一次使用时自动建表
		arg0.execSQL("create table coupon_db(id integer primary key autoincrement,coupon_id varchar(50),coupon_content varchar(400),start_time varchar(20),end_time varchar(20),coupons_amount varchar(10),img_url varchar(100),questionnaire_url varchar(100),coupon_price varchar(10),original_price varchar(10),shop_name varchar(100),shop_address varchar(100),phone_num varchar(15))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		System.out.println("update a sqlite database");
	}
}
