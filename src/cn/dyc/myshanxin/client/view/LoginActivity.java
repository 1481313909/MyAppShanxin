package cn.dyc.myshanxin.client.view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import cn.dyc.myshanxin.R;
import cn.dyc.myshanxin.client.model.ManageClientConServer;
import cn.dyc.myshanxin.client.model.SXClient;
import cn.dyc.myshanxin.common.SXMessage;
import cn.dyc.myshanxin.common.SXMessageType;
import cn.dyc.myshanxin.common.User;

public class LoginActivity extends Activity {
	Dialog dialog;
	EditText user,pwd;
	String username,passwd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		Button login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new LoginListener());

		// ��ס���빦��
		// ���ļ��ж�������
		try {
			FileInputStream fis = openFileInput("passSave.txt");
			InputStreamReader inRead = new InputStreamReader(fis);
			BufferedReader bufferedR = new BufferedReader(inRead);
			String str;
			if ((str = bufferedR.readLine()) != null) {
				String[] txt = str.split(" ");
				EditText user = (EditText) findViewById(R.id.dyc_username);
				EditText pass = (EditText) findViewById(R.id.dyc_passwd);
				user.setText(txt[0]);
				pass.setText(txt[1]);
				CheckBox remima = (CheckBox) findViewById(R.id.dyc_rember);
				remima.setChecked(true);
			}
			fis.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private class LoginListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// ��֤�û��������룬�Ƿ�����
			user = (EditText) findViewById(R.id.dyc_username);
			pwd = (EditText) findViewById(R.id.dyc_passwd);
			username = user.getText().toString();
			passwd = pwd.getText().toString();
			if (username == null || username.isEmpty() || passwd == null
					|| passwd.isEmpty()) {
				// ��ʾ�����û�������
				Intent intent = new Intent();
				intent.putExtra("message", "�������û��������룡");
				intent.setClass(LoginActivity.this, MessageActivity.class);
				startActivity(intent);
				return;
			}

			dialog = ProgressDialog.show(LoginActivity.this, "YQ", "���ڵ�½�� ��",
					true, true);
			handler.post(new Runnable() {
				public void run() {
					//boolean b = login(user.getText().toString(), pwd.getText().toString());
					boolean b = login(username, passwd);
					
					
					if (b) {
						// ��ס����--���ļ���д������
						CheckBox remember = (CheckBox) findViewById(R.id.dyc_rember);
						try {
							String writer;
							FileOutputStream fos = openFileOutput("passSave.txt", 0);
							OutputStreamWriter outWriter = new OutputStreamWriter(fos);
							BufferedWriter bufferedW = new BufferedWriter(outWriter);
							if (remember.isChecked())
								writer = username + " " + passwd;
							else
								writer = " ";
							bufferedW.write(writer);
							bufferedW.flush();
							fos.close();
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						// �����¼����
						Intent intent = new Intent();
						intent.setClass(LoginActivity.this, LoadingActivity.class);
						startActivity(intent);
					} else {
						// ��ʾ�û��������
						Intent intent = new Intent();
						intent.putExtra("message", "�û��������벻��ȷ��");
						intent.setClass(LoginActivity.this, MessageActivity.class);
						startActivity(intent);
					}
					//db.close();
					
					
					
					
//					if (b) {
//						Message m = new Message();
//						m.what = 1;
//						handler.sendMessage(m);
//						// ת��������
//						startActivity(new Intent(LoginActivity.this,
//								LoadingActivity.class));
//					} else {
//						Toast.makeText(LoginActivity.this, "��½ʧ�ܣ���������Ϊʲô��",
//								Toast.LENGTH_SHORT).show();
//					}
				}
			});

			// //��֤���ݿ�TODO
			// Dyc_DBHelper helper=new Dyc_DBHelper(LoginActivity.this);
			// SQLiteDatabase db=helper.getReadableDatabase();
			// String
			// sql="select count(*) cont from user_info where username=? and passwd=?";
			// Cursor result=db.rawQuery(sql, new String[]{username,passwd});
			// int count=0;
			// while(result.moveToNext()){
			// count=result.getInt(result.getColumnIndex("cont"));
			// }

			
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void login_back(View v) { // ������ ���ذ�ť
		this.finish();
	}

	public void login_pw(View v) { // �������밴ť
		Uri uri = Uri.parse("http://3g.qq.com");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
		// Intent intent = new Intent();
		// intent.setClass(Login.this,Whatsnew.class);
		// startActivity(intent);
	}

	boolean login(String a, String p) {
		User user = new User();
		user.setusername(a);
		user.setPassword(p);
		user.setOperation("login");
		boolean b = new SXClient(this).sendLoginInfo(user);
		// ��½�ɹ�
		if (b) {
			try {
				// ����һ��Ҫ�󷵻����ߺ��ѵ������Message
				ObjectOutputStream oos = new ObjectOutputStream(
						ManageClientConServer
								.getClientConServerThread(user.getusername())
								.getS().getOutputStream());
				SXMessage m = new SXMessage();
				m.setType(SXMessageType.GET_ONLINE_FRIENDS);
				m.setSender(user.getusername());
				oos.writeObject(m);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.finish();
			return true;
		} else {
			return false;
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				break;
			}
		}
	};
}
