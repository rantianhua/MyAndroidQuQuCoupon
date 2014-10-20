package jiajia.util;

import java.io.File;
import java.io.FileOutputStream;

import jiajia.db.DatabaseDB;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class SDUtil {
	
	
	//SD����Ŀ¼
	private static String SDPATH = Environment.getExternalStorageDirectory().getPath();
	//�ֻ������Ŀ¼
	private static String dataRootPath = null;
	//����ͼƬ��Ŀ¼��
	private final String DIRECTORY = "/CouponPicureR";
	private DatabaseDB db;
	final String PICDIR = "CouponPicureR";	//ͼƬĿ¼�� 

	public SDUtil(Context context) {
		db = new DatabaseDB();
		db.CreateDatabase(context);
		dataRootPath = context.getCacheDir().getPath();
	}
	
	//�õ�ͼƬ��Ŀ¼·��
	public String getPicPath() {
		//��SD�������þʹ洢��Ӧ�ó����ļ���
		return  Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?  SDPATH + DIRECTORY : dataRootPath + DIRECTORY;  
	}
	
	//�õ��ļ���
	public String getFileName(String imageUrl) {
		String couponID = db.GetCouponID(imageUrl);
		db.close();
		return "/" + couponID + ".png";
	}
	
	public long getFileSize(String imageUrl) {
		return new File(getPicPath()+getFileName(imageUrl)).length();
	}

	public boolean PictureExits(String imageUrl) {
		return new File(getPicPath()+getFileName(imageUrl)).exists();
	}

	public Bitmap getImage(String imageUrl) {
		return BitmapFactory.decodeFile(getPicPath()+getFileName(imageUrl));
	}

	public void save(String imageUrl, Bitmap bitmap) {
        String path = getPicPath();  
        File folderFile = new File(path);  
        if(!folderFile.exists()){  
            folderFile.mkdir();  
        }  
        
        File file = new File(path + getFileName(imageUrl));
        FileOutputStream fos = null;
        
		try{
			//�½����ļ�
			file.createNewFile();
			fos = new FileOutputStream(file);
			if(fos!= null) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
			}
			 fos.flush();  
		     fos.close(); 
		}catch(Exception e) {
			e.printStackTrace();
		}		
	}
}
