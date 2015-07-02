package cn.dyc.myshanxin.client.view;



import cn.dyc.myshanxin.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Burn extends Activity{
	private WebView webview; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.burn);
		webview = (WebView) findViewById(R.id.webview); 
        //����WebView���ԣ��ܹ�ִ��Javascript�ű� 
        webview.getSettings().setJavaScriptEnabled(true); 
        //������Ҫ��ʾ����ҳ 
        webview.loadUrl("http://210.28.32.234:8080/bar/ws/input"); 
        //����Web��ͼ 
        webview.setWebViewClient(new HelloWebViewClient ());
   }
	@Override
    //���û��� 
    //����Activity���onKeyDown(int keyCoder,KeyEvent event)���� 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) { 
            webview.goBack(); //goBack()��ʾ����WebView����һҳ�� 
            return true; 
        } 
        return false; 
    } 
     
    //Web��ͼ 
    private class HelloWebViewClient extends WebViewClient { 
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { 
            view.loadUrl(url); 
            return true; 
        } 
    }
    
    public void burn_activity_back(View v) {     //������ ���ذ�ť
      	this.finish();
      }  
}