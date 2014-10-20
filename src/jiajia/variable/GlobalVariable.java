package jiajia.variable;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class GlobalVariable {
	// 缓存Image的类，当储存的image的大小大于Lrucache设定的值，系统自动 释放内存
	private static LruCache<String, Bitmap> mMemoryCache = null;

	public static void initCache() {
		if (mMemoryCache == null) {
			// 获取系统分给每个应用程序最大内存（32M）
			int maxMemory = (int) Runtime.getRuntime().maxMemory();
			int cacheSzie = maxMemory / 8; // 此为内存缓存的大小，4M
			mMemoryCache = new LruCache<String, Bitmap>(cacheSzie) {
				// 必须重写此方法，测量Bitmap的大小
				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getRowBytes() * value.getHeight();
				}
			};
		}
	}
	
	public static LruCache<String, Bitmap> getCache() {
		return mMemoryCache;
	}
}
