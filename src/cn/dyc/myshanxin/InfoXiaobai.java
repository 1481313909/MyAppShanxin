package cn.dyc.myshanxin;

import cn.dyc.myshanxin.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

public class InfoXiaobai extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_xiaobai);              
    }

   public void btn_back(View v) {     //������ ���ذ�ť
      	this.finish();
      } 
   public void btn_back_send(View v) {     //������ ���ذ�ť
     	this.finish();
     } 
   public void head_xiaobai(View v) {     //ͷ��ť
	   Intent intent = new Intent();
		intent.setClass(InfoXiaobai.this,InfoXiaobaiHead.class);
		startActivity(intent);
    } 
    
}
