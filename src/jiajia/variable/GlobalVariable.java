package jiajia.variable;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class GlobalVariable {
	// ����Image���࣬�������image�Ĵ�С����Lrucache�趨��ֵ��ϵͳ�Զ� �ͷ��ڴ�
	private static LruCache<String, Bitmap> mMemoryCache = null;

	public static void initCache() {
		if (mMemoryCache == null) {
			// ��ȡϵͳ�ָ�ÿ��Ӧ�ó�������ڴ棨32M��
			int maxMemory = (int) Runtime.getRuntime().maxMemory();
			int cacheSzie = maxMemory / 8; // ��Ϊ�ڴ滺��Ĵ�С��4M
			mMemoryCache = new LruCache<String, Bitmap>(cacheSzie) {
				// ������д�˷���������Bitmap�Ĵ�С
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
