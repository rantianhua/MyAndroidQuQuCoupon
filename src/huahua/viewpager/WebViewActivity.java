package huahua.viewpager;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity{
	
	//��Ҫ�L����Url
	private String url;
	
	//WebView�ؼ�
	private WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		Intent intent = getIntent();
		url = intent.getStringExtra("questionnaire_url");
		if(!url.equals("http://www.wenjuan.com/s/iYJbui")) {
			url = "http://www.wenjuan.com/s/iYJbui";
		}
		init();
	}

	private void init() {
		//��ʼ��webView
		webView = (WebView)findViewById(R.id.WebView);
		//WebView����web��Դ
		webView.loadUrl(url);
		//����WebViewĬ��ʹ�õ��������������ҳ�ķ�����ʹ��WebView����ҳ
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				//����Ϊtrue��ʱ��ʹ��WebView����ҳ
				return true;
			}
		});
		//����javaScrip
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
	}

	//��д�����������ص��߼�
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			//�ȼ��WebView�ķ��ʼ�¼������û�пɷ��ص�
			if(webView.canGoBack()) {
				webView.goBack();
				return true;
			}else {
				return super.onKeyDown(keyCode, event);
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}

