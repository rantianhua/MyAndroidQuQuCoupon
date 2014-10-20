package jiajia.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class StandardImage {
	private int newWidth;
	private int newHeight;
	private Bitmap bm;
	
	public StandardImage(Bitmap bm,int newWidth,int newHeight) {
		this.bm = bm;
		this.newHeight = newHeight;
		this.newWidth = newWidth;
	}
	
	public Bitmap setImage() {
		int preWidth = bm.getWidth(); 	//ԭͼƬ�Ŀ�
		int preHeight = bm.getHeight();	//ԭͼ�ĸ�
		//������Ҫ�Ĵ�С
		int needWidth = newWidth;
		int needHeight = newHeight;
		//�������ű���
		float scaleWidth = (float)needWidth/preWidth;
		float scaleHeight = (float)needHeight / preHeight;
		//ȡ����Ҫ���ŵ�matrix����
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth,scaleHeight);
		//�õ��µ�ͼƬ
		bm = Bitmap.createBitmap(bm,0,0,preWidth,preHeight,matrix,true);
		return bm;
	}
}
