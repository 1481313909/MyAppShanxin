package cn.dyc.myshanxin;



import cn.dyc.myshanxin.R;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;

public class Aboutus extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.aboutus);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);   //ȫ����ʾ
		//Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
		
   }
	
	public void btn_back(View v) {     //������ ���ذ�ť
      	this.finish();
      } 
}