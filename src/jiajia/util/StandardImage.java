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
		int preWidth = bm.getWidth(); 	//原图片的宽
		int preHeight = bm.getHeight();	//原图的高
		//设置想要的大小
		int needWidth = newWidth;
		int needHeight = newHeight;
		//计算缩放比例
		float scaleWidth = (float)needWidth/preWidth;
		float scaleHeight = (float)needHeight / preHeight;
		//取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth,scaleHeight);
		//得到新的图片
		bm = Bitmap.createBitmap(bm,0,0,preWidth,preHeight,matrix,true);
		return bm;
	}
}
