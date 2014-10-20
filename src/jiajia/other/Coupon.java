package jiajia.other;

public class Coupon {
	
	private String coupon_id;
	private String shopName;
	private String couponPrice;
	private String originalPrice;
	private String startTime;
	private String endTime;
	private String imageUrl;
	
	
	
	public Coupon(String coupon_id){
		
		this.coupon_id=coupon_id;
		
	}		
	
	public Coupon(String coupon_id, String shopName, String couponPrice, String originalPrice,
			String startTime, String endTime, String imageUrl) {
		
		this.coupon_id = coupon_id;
		this.shopName = shopName;
		this.couponPrice = couponPrice;
		this.originalPrice = originalPrice;
		this.startTime = startTime;
		this.endTime = endTime;
		this.imageUrl = imageUrl;
	}

	//�����Լ��õ�coupon_id�ķ���
	public String getCoupon_id() {
		return coupon_id;
	}
	public void setCoupon_id(String coupon_id) {
		this.coupon_id = coupon_id;
	}
	
	//�����Լ��õ�shop_name�ķ���
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
	//�����Լ��õ�coupon_price�ķ���
	public void setCouponPrice(String couponPrice) {
		this.couponPrice = couponPrice;
	}
	public String getCouponPrice() {
		return couponPrice;
	}
	
	//�����Լ��õ�original_price�ķ���
	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}
	public String getOriginalPrice() {
		return originalPrice;
	}
	
	//�����Լ��õ�start_time�ķ���
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	//�����Լ��õ�start_time�ķ���
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	//�����Լ��õ�img_url�ķ���
	public String getImageUrl() {	
		return this.imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String toString(){
		String text=this.shopName+"\n"+this.couponPrice;
		return text;
	}

}