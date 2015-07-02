package cn.dyc.myshanxin.client.view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import cn.dyc.myshanxin.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Login_bak extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		Button login=(Button)findViewById(R.id.login);
		login.setOnClickListener(new LoginListener());
		
		//��ס���빦��
				//���ļ��ж�������
						try {
							FileInputStream fis=openFileInput("passSave.txt");
							InputStreamReader inRead= new InputStreamReader(fis);
							BufferedReader bufferedR = new BufferedReader(inRead);
							String str;
							if((str=bufferedR.readLine())!= null){
								String []txt=str.split(" ");
								EditText user=(EditText)findViewById(R.id.dyc_username);
								EditText pass=(EditText)findViewById(R.id.dyc_passwd);
								user.setText(txt[0]);
								pass.setText(txt[1]);
								CheckBox remima=(CheckBox)findViewById(R.id.dyc_rember);
								remima.setChecked(true);
							}
							fis.close();
							
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}

	}
	private class LoginListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			//��֤�û��������룬�Ƿ�����
			EditText user=(EditText)findViewById(R.id.dyc_username);
			EditText pwd=(EditText)findViewById(R.id.dyc_passwd);
			String username=user.getText().toString();
			String passwd=pwd.getText().toString();
            if(username==null||username.isEmpty()||passwd==null||passwd.isEmpty()){
            	//��ʾ�����û�������
            	Intent intent=new Intent();
            	intent.putExtra("message", "�������û��������룡");
            	intent.setClass(Login_bak.this, MessageActivity.class);
            	startActivity( intent);
            	return;
            }
			//��֤���ݿ�TODO
			Dyc_DBHelper helper=new Dyc_DBHelper(Login_bak.this);
			SQLiteDatabase db=helper.getReadableDatabase();
			String sql="select count(*) cont from user_info where username=? and passwd=?";
			Cursor result=db.rawQuery(sql, new String[]{username,passwd});
			int count=0;
			while(result.moveToNext()){
				count=result.getInt(result.getColumnIndex("cont"));
			}
			if(count>0){
				//��ס����--���ļ���д������
				CheckBox remember= (CheckBox)findViewById(R.id.dyc_rember);
					try {
						String writer;
						FileOutputStream fos=openFileOutput("passSave.txt",0);
						OutputStreamWriter outWriter = new OutputStreamWriter(fos);
						BufferedWriter bufferedW = new BufferedWriter(outWriter);
						if (remember.isChecked()) 
							 writer=username+" "+passwd;
						else writer=" ";
						bufferedW.write(writer);
						bufferedW.flush();
						fos.close();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				//�����¼����
				Intent intent=new Intent();
            	intent.setClass(Login_bak.this, LoadingActivity.class);
            	startActivity( intent);
			}else{
				//��ʾ�û��������
				Intent intent=new Intent();
            	intent.putExtra("message", "�û��������벻��ȷ��");
            	intent.setClass(Login_bak.this, MessageActivity.class);
            	startActivity( intent);
			}
			db.close();
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	} 
    public void login_back(View v) {     //������ ���ذ�ť
      	this.finish();
      }  
    public void login_pw(View v) {     //�������밴ť
    	Uri uri = Uri.parse("http://3g.qq.com"); 
    	Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
    	startActivity(intent);
    	//Intent intent = new Intent();
    	//intent.setClass(Login.this,Whatsnew.class);
        //startActivity(intent);
      }  
}
