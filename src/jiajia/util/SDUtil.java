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
	
	
	//SD卡根目录
	private static String SDPATH = Environment.getExternalStorageDirectory().getPath();
	//手机缓存根目录
	private static String dataRootPath = null;
	//保存图片的目录名
	private final String DIRECTORY = "/CouponPicureR";
	private DatabaseDB db;
	final String PICDIR = "CouponPicureR";	//图片目录名 

	public SDUtil(Context context) {
		db = new DatabaseDB();
		db.CreateDatabase(context);
		dataRootPath = context.getCacheDir().getPath();
	}
	
	//得到图片的目录路径
	public String getPicPath() {
		//若SD卡不可用就存储在应用程序文件中
		return  Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?  SDPATH + DIRECTORY : dataRootPath + DIRECTORY;  
	}
	
	//得到文件名
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
			//新建该文件
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
