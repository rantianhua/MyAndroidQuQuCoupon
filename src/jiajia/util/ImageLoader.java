package jiajia.util;


import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jiajia.variable.GlobalVariable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;

public class ImageLoader{

	//����Image���࣬�������image�Ĵ�С����Lrucache�趨��ֵ��ϵͳ�Զ� �ͷ��ڴ�
	private LruCache<String,Bitmap> mMemoryCache =  null;	//�ڴ滺��
	
	//�����ļ���ص���
	private SDUtil sdUtil;
	//����ͼƬ���̳߳�
	private ExecutorService imageThreadPool = null;

    public ImageLoader(Context context) {
        sdUtil = new SDUtil(context);
        mMemoryCache = GlobalVariable.getCache();
    }
    

    //��ȡ�̳߳صķ���
    public ExecutorService getThreadPool() {
		if(imageThreadPool == null) {
			synchronized (ExecutorService.class) {
				if(imageThreadPool == null) {
					//��֤���ص������ԣ����������߳�����ͼƬ
					imageThreadPool = Executors.newFixedThreadPool(2);
				}
			}
		}
		return imageThreadPool;
	}
    
    //���Bitmap���ڴ滺��
    public void addBitmapToMemoryCache(String key,Bitmap bitmap) {
		if(key != null && getBitmapFromMemoryCache(key) == null && bitmap != null) {
			mMemoryCache.put(key, bitmap);
		}
	}
    
    //���ڴ滺����ȡ��Bitmap
    public Bitmap getBitmapFromMemoryCache(String key) {
    	if(key != null) {
    		return mMemoryCache.get(key);
    	}
		return null;
	}

	public void loadImage(String image_url, Handler handler) {
		final String url = image_url;
		final Handler han = handler;
		getThreadPool().execute(new Runnable() {
			Bitmap bm;
			@Override
			public void run() {
				try{
					Log.i("tag", "msg");
					bm = BitmapFactory.decodeStream(new URL(url).openStream());
				}catch(Exception e) {
					e.printStackTrace();
				}
				Message msg = han.obtainMessage();
				if(bm == null) {
					msg.what = 1;
				}else {
					try{
						sdUtil.save(url,bm);
						//��bitmap�����ڴ滺��
						addBitmapToMemoryCache(url, bm);
					}catch(Exception e) {
						e.printStackTrace();
					}
					msg.what = 0;
					msg.obj = bm;
					
				}
				han.sendMessage(msg);
			}
		});
    }
	
	public Bitmap getBitmapInLocal(String imageUrl) {
		Bitmap bitmap;
    	//�ȴ��ڴ滺������
    	bitmap = getBitmapFromMemoryCache(imageUrl);
    	if(bitmap != null) {
    		Log.i("������","���ڴ滺����ȡ");
    		return bitmap;
    	}
        
        //�ٴ�SD�����ļ�������
        if(sdUtil.PictureExits(imageUrl) && sdUtil.getFileSize(imageUrl) != 0) {
        	Log.i("SD��","��SD�����ļ���ȡ");
        	bitmap = sdUtil.getImage(imageUrl);
        	//���·����ڴ滺��
        	addBitmapToMemoryCache(imageUrl, bitmap);
    		return bitmap;
        } 
        
        return null;
        	
	}

//	public Bitmap getmBitmap() {
//		return mBitmap;
//	}		
}
