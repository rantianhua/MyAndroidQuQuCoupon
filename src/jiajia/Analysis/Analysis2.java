package jiajia.Analysis;

public class Analysis2 {
	
	private String[] strlist = null;
	private String[] as = null;

	public Analysis2(String str) {
		
		as = str.split("\\}");
		strlist = new String[as.length - 1];
		for (int i = 0; i < as.length - 1; i++) {
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
		for (int i = 0; i < strlist.length; i++) {
			// picUrllist[i] = strlist[i].substring(
			// strlist[i].indexOf("img_url") + 10,
			// strlist[i].indexOf("questionnaire_url") - 3);
			picUrllist[i] = "http://img2.imgtn.bdimg.com/it/u=2348159125,3198730265&fm=90&gp=0.jpg";
		}
		return picUrllist;
	}

	public String[] getquestionnaireUrllist() {
		String[] questionnaireUrllist = new String[strlist.length];
		for (int i = 0; i < strlist.length; i++) {
			questionnaireUrllist[i] = strlist[i].substring(
					strlist[i].indexOf("questionnaire_url") + 20,
					strlist[i].indexOf("coupon_price") - 3);
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

	public String[] getstartTimelist() {
		String[] startTimelist = new String[strlist.length];
		for (int i = 0; i < strlist.length; i++) {
			startTimelist[i] = strlist[i].substring(
					strlist[i].indexOf("start_time") + 13,
					strlist[i].indexOf("end_time") - 3);
		}
		return startTimelist;
	}

	public String[] getshopPhonelist() {
		String[] shopPhonelist = new String[strlist.length];
		for (int i = 0; i < strlist.length; i++) {
			shopPhonelist[i] = strlist[i].substring(
					strlist[i].indexOf("phone_num") + 12,
					strlist[i].indexOf("coupon_state") - 3);
		}
		return shopPhonelist;
	}

	public int[] getStylelist() {
		int[] Stylelist = new int[strlist.length];
		for (int i = 0; i < strlist.length; i++) {
			Stylelist[i] = Integer.parseInt(strlist[i].substring(
					strlist[i].indexOf("coupon_state") + 15,
					strlist[i].length() - 1));
		}
		return Stylelist;
	}
}
