package jiajia.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SetupDatabase extends SQLiteOpenHelper {

	private static final int VERSON = 1;// Ĭ�ϵ����ݿ�汾

	// �̳�SQLiteOpenHelper�����������Լ��Ĺ��캯��
	// �ù��캯��4��������ֱ�ӵ��ø���Ĺ��캯�������е�һ������Ϊ���౾���ڶ�������Ϊ���ݿ�����֣�
	// ��3�����������������α����ģ�����һ������Ϊnull�������������ݿ�İ汾�š�
	public SetupDatabase(Context context, String name, CursorFactory factory,
			int verson) {
		super(context, name, factory, verson);
	}

	// �ù��캯����3����������Ϊ�������溯���ĵ�3�������̶�Ϊnull��
	public SetupDatabase(Context context, String name, int verson) {
		this(context, name, null, verson);
	}

	// �ù��캯��ֻ��2�������������溯�� �Ļ����Ͻ��汾�Ź̶���
	public SetupDatabase(Context context, String name) {
		this(context, name, VERSON);
	}

	// �ú��������ݿ��һ�α�����ʱ����
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		System.out.println("create a sqlite database");
		// ��һ��ʹ��ʱ�Զ�����
		arg0.execSQL("create table coupon_db(id integer primary key autoincrement,coupon_id varchar(50),coupon_content varchar(400),start_time varchar(20),end_time varchar(20),coupons_amount varchar(10),img_url varchar(100),questionnaire_url varchar(100),coupon_price varchar(10),original_price varchar(10),shop_name varchar(100),shop_address varchar(100),phone_num varchar(15))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		System.out.println("update a sqlite database");
	}
}
