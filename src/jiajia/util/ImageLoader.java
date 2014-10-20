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

	//缓存Image的类，当储存的image的大小大于Lrucache设定的值，系统自动 释放内存
	private LruCache<String,Bitmap> mMemoryCache =  null;	//内存缓存
	
	//操作文件相关的类
	private SDUtil sdUtil;
	//下载图片的线程池
	private ExecutorService imageThreadPool = null;

    public ImageLoader(Context context) {
        sdUtil = new SDUtil(context);
        mMemoryCache = GlobalVariable.getCache();
    }
    

    //获取线程池的方法
    public ExecutorService getThreadPool() {
		if(imageThreadPool == null) {
			synchronized (ExecutorService.class) {
				if(imageThreadPool == null) {
					//保证下载的流畅性，开启两个线程下载图片
					imageThreadPool = Executors.newFixedThreadPool(2);
				}
			}
		}
		return imageThreadPool;
	}
    
    //添加Bitmap到内存缓存
    public void addBitmapToMemoryCache(String key,Bitmap bitmap) {
		if(key != null && getBitmapFromMemoryCache(key) == null && bitmap != null) {
			mMemoryCache.put(key, bitmap);
		}
	}
    
    //从内存缓存中取得Bitmap
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
						//将bitmap加入内存缓存
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
    	//先从内存缓存中找
    	bitmap = getBitmapFromMemoryCache(imageUrl);
    	if(bitmap != null) {
    		Log.i("软引用","从内存缓存中取");
    		return bitmap;
    	}
        
        //再从SD卡或文件中中找
        if(sdUtil.PictureExits(imageUrl) && sdUtil.getFileSize(imageUrl) != 0) {
        	Log.i("SD卡","从SD卡或文件中取");
        	bitmap = sdUtil.getImage(imageUrl);
        	//重新放入内存缓存
        	addBitmapToMemoryCache(imageUrl, bitmap);
    		return bitmap;
        } 
        
        return null;
        	
	}

//	public Bitmap getmBitmap() {
//		return mBitmap;
//	}		
}
