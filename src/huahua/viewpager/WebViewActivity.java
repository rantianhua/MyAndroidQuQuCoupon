package huahua.viewpager;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity{
	
	//需要訪問的Url
	private String url;
	
	//WebView控件
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
		//初始化webView
		webView = (WebView)findViewById(R.id.WebView);
		//WebView加载web资源
		webView.loadUrl(url);
		//覆盖WebView默认使用第三方浏览器打开网页的方法，使用WebView打开网页
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				//返回为true的时候使用WebView打开网页
				return true;
			}
		});
		//启用javaScrip
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
	}

	//改写物理案件，返回的逻辑
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			//先检查WebView的访问记录里面有没有可返回的
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

