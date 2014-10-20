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

	//设置以及得到coupon_id的方法
	public String getCoupon_id() {
		return coupon_id;
	}
	public void setCoupon_id(String coupon_id) {
		this.coupon_id = coupon_id;
	}
	
	//设置以及得到shop_name的方法
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
	//设置以及得到coupon_price的方法
	public void setCouponPrice(String couponPrice) {
		this.couponPrice = couponPrice;
	}
	public String getCouponPrice() {
		return couponPrice;
	}
	
	//设置以及得到original_price的方法
	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}
	public String getOriginalPrice() {
		return originalPrice;
	}
	
	//设置以及得到start_time的方法
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	//设置以及得到start_time的方法
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	//设置以及得到img_url的方法
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