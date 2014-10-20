package jiajia.Analysis;


public class Analysis {
	private String[] strlist = null;
    private String[] as = null;
    
	public Analysis(String str) {
		as = str.split("\\}");
		strlist = new String[as.length - 1];
		for (int i = 0; i < as.length-1; i++) {
				strlist[i] = as[i];
		}
	}
    
	public String[] getcouponIDlist() {
		String[] couponIDlist = new String[strlist.length];
		for (int i = 0; i < strlist.length; i++) {
			couponIDlist[i] = strlist[i].substring(
					strlist[i].indexOf("coupon_id") + 12,
					strlist[i].indexOf("coupon_content") - 3);
		}
		return couponIDlist;
	}
	
	public String[] getpicUrllsit() {
		String[] picUrllist = new String[strlist.length];
		String str = null;
		for (int i = 0; i < strlist.length; i++) {
			str = strlist[i].substring(
					strlist[i].indexOf("img_url") + 10,
					strlist[i].indexOf("questionnaire_url") - 3);
			picUrllist[i] = getUsefulUrl(str);
		}
		return picUrllist;
	}
	
	public String[] getquestionnaireUrllist() {
		String[] questionnaireUrllist = new String[strlist.length];
		String str = null;
		for (int i = 0; i < strlist.length; i++) {
			str = strlist[i].substring(
					strlist[i].indexOf("questionnaire_url") + 20,
					strlist[i].indexOf("favourable_price") - 3);
			questionnaireUrllist[i] = getUsefulUrl(str);
		}
		return questionnaireUrllist;
	}
	
	public String[] getcouponContentlist() {
		String[] couponContentlist = new String[strlist.length];
		for (int i = 0; i < strlist.length; i++) {
			couponContentlist[i] = strlist[i].substring(
					strlist[i].indexOf("coupon_content") + 17,
					strlist[i].indexOf("start_time") - 3);
		}
		return couponContentlist;
	}

	public String[] getdeadLinelist() {
		String[] deadLinelist = new String[strlist.length];
		for (int i = 0; i < strlist.length; i++) {
			deadLinelist[i] = strlist[i].substring(
					strlist[i].indexOf("end_time") + 11,
					strlist[i].indexOf("coupons_amount") - 3);
		}
		return deadLinelist;
	}

	public String[] getaddresslist() {
		String[] addresslist = new String[strlist.length];
		for (int i = 0; i < strlist.length; i++) {
			addresslist[i] = strlist[i].substring(
					strlist[i].indexOf("shop_address") + 15,
					strlist[i].indexOf("phone_num") - 3);
		}
		return addresslist;
	}

	public String[] getshopNamelist() {
		String[] shopNamelist = new String[strlist.length];
		for (int i = 0; i < strlist.length; i++) {
			shopNamelist[i] = strlist[i].substring(
					strlist[i].indexOf("shop_name") + 12,
					strlist[i].indexOf("shop_address") - 3);
		}
		return shopNamelist;
	}

	public String[] getcouponCountlist() {
		String[] couponCountlist = new String[strlist.length];
		for (int i = 0; i < strlist.length; i++) {
			couponCountlist[i] = strlist[i].substring(
					strlist[i].indexOf("coupons_amount") + 17,
					strlist[i].indexOf("img_url") - 3);
		}
		return couponCountlist;
	}
	public String[] getstartTimelist(){
		String[] startTimelist = new String[strlist.length];
		for (int i = 0; i < strlist.length; i++) {
			startTimelist[i] = strlist[i].substring(
					strlist[i].indexOf("start_time") + 13,
					strlist[i].indexOf("end_time") - 3);
		}
		return startTimelist;
	}
	public String[] getshopPhonelist(){
		String[] shopPhonelist = new String[strlist.length];
		for (int i = 0; i < strlist.length; i++) {
			shopPhonelist[i] = strlist[i].substring(
					strlist[i].indexOf("phone_num") + 12,
					strlist[i].indexOf("original_price")-3);
		}
		return shopPhonelist;
	}
	
	public String[] getOriginalPricelist(){
		String[] originalPricelist = new String[strlist.length];
		for (int i = 0; i < strlist.length; i++) {
			originalPricelist[i] = strlist[i].substring(
					strlist[i].indexOf("original_price") + 17,
					strlist[i].length() - 1);
			System.out.println(originalPricelist[i]);
		}
		return originalPricelist;
	}
	
	public String[] getCouponPricelist(){
		String[] couponPricelist = new String[strlist.length];
		for (int i = 0; i < strlist.length; i++) {
			couponPricelist[i] = strlist[i].substring(
					strlist[i].indexOf("favourable_price") + 19,
					strlist[i].indexOf("shop_name") - 3);
		}
		return couponPricelist;
	}
	
	//把图片中的反斜杠去掉
	public String getUsefulUrl(String str) {
		return str.replace("\\", "");
	}
}
