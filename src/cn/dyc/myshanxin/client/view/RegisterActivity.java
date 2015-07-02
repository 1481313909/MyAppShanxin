package cn.dyc.myshanxin.client.view;


import java.sql.*;

import cn.dyc.myshanxin.R;
import cn.dyc.myshanxin.client.model.SXClient;
import cn.dyc.myshanxin.client.view.*;
import cn.dyc.myshanxin.common.*;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterActivity extends Activity{
	private String[] citys={"�Ͼ�","����","����","��ͨ"};
	private ArrayAdapter<String> adapter=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		//��ʼ������
		adapter =new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,citys);
		Spinner city=(Spinner)findViewById(R.id.dyc_city);
		city.setAdapter(adapter);
		
	    Button reg=(Button)findViewById(R.id.button1);
		reg.setOnClickListener(new RigsterListener());
		
		Button log=(Button)findViewById(R.id.button2);
		log.setOnClickListener(new LogListener());
	}
	
	private class LogListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(RegisterActivity.this,LoginActivity.class );
			startActivity(intent);
		}
		
	}
  private class RigsterListener implements OnClickListener{

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//��֤��Ϣ
		EditText user_txt=(EditText)findViewById(R.id.dyc_username);
		EditText pwd_txt=(EditText)findViewById(R.id.dyc_passwd);
		EditText repwd_txt=(EditText)findViewById(R.id.dyc_repasswd);
		
		String username =user_txt.getText().toString();
		String passwd =pwd_txt.getText().toString();
		String repasswd =repwd_txt.getText().toString();
		
		Intent intent=new Intent();
		boolean validate=true;
		if(username==null||username.length()==0){
			intent.putExtra("message", "�û�������Ϊ��");
			validate=false;
			
		}else if(passwd==null||passwd.length()==0){
			intent.putExtra("message", "���벻��Ϊ��");
			validate=false;
		}else if(!passwd.equals(repasswd)){
			intent.putExtra("message", "���������ȷ�ϲ�һ��");
			validate=false;
		}
		if(!validate){
			intent.setClass(RegisterActivity.this,MessageActivity.class);
			startActivity(intent);
			return;
		}else {
			
			RadioGroup group=(RadioGroup)findViewById(R.id.dycradioGroup1);
			int sexid=group.getCheckedRadioButtonId();
			String sex="";
			if(sexid==R.id.dycradio0){
				sex="1";
				
			}else{
				sex="0";
			}
			CheckBox h1_btn=(CheckBox)findViewById(R.id.dycchoose1);
			CheckBox h2_btn=(CheckBox)findViewById(R.id.checkBox2);
			CheckBox h3_btn=(CheckBox)findViewById(R.id.checkBox3);
			String hobby1="",hobby2="",hobby3="";
			if(h1_btn.isChecked()){
				hobby1=h1_btn.getText().toString();
			}
			if(h2_btn.isChecked()){
				hobby2=h1_btn.getText().toString();
			}
			if(h3_btn.isChecked()){
				hobby3=h1_btn.getText().toString();
			}
			Spinner spnr=(Spinner)findViewById(R.id.dyc_city);
			String city =spnr.getSelectedItem().toString();
			
			
			User user=new User();
			user.setusername(username);
			user.setPassword(passwd);
			user.setSex(sex);
			user.sethobby1(hobby1);
			user.sethobby2(hobby2);
			user.sethobby3(hobby3);
			user.setcity(city);
			user.setTime(MyTime.geTimeNoS());
			user.setOperation("register");
			boolean b=new SXClient(RegisterActivity.this).sendRegisterInfo(user);
			if(b){
				//ע��ɹ���ת����½
                Toast.makeText(RegisterActivity.this, "��ϲ�㣬ע��ɹ� ��", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
			}
			
			
			
			
			
			  
			//register(username,passwd,sex,hobby1,hobby2,hobby3,city);	
			
//			//���浽���ݿ�
//			Dyc_DBHelper dbhelper=new Dyc_DBHelper(RegisterMysql.this );
//			//��֤�û��Ƿ���ڣ�
//			SQLiteDatabase readb=dbhelper.getReadableDatabase();
//			String sql="select count(*) cont from user_info where username=? ";
//			Cursor result=readb.rawQuery(sql, new String[]{username});
//			int count=0;
//			while(result.moveToNext()){
//				count=result.getInt(result.getColumnIndex("cont"));
//			}
//			if(count>0){
//				//��ʾ�û�������
//			
//            	intent.putExtra("message", "���û����Ѵ��ڣ�");
//            	intent.setClass(RegisterMysql.this, MessageActivity.class);
//            	startActivity( intent);
//            	readb.close();
//            	return;
//			}
//		
//			
//			SQLiteDatabase db=dbhelper.getWritableDatabase();
//			db.execSQL("insert into user_info values(?,?,?,?,?,?,?,?,?)",
//					new String[]{username,passwd,sex,hobby1,hobby2,hobby3,city,"0","0"});
//		  db.close();
		  
		  intent.putExtra("message", "ע��ɹ���");
		  intent.setClass(RegisterActivity.this,MessageActivity.class);
		  startActivity(intent);
		  return;
			
		}
		
		
	}
	  
  }
  
  public void btn_back(View v) {     //������ ���ذ�ť
    	this.finish();
    } 
  
  public void register(String username,String passwd,String sex,String hobby1,String hobby2,String hobby3,String city){
  String sqlin="insert into user_info values(?,?,?,?,?,?,?)";
	String sqlcheck="select * from users where name=?";
	String uri = "jdbc:mysql://192.168.1.13:3306/myshanxin";
	Connection conn=null;
	try {
		Class.forName("com.mysql.jdbc.Driver");
	    conn = DriverManager.getConnection(uri, "root", "123");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	PreparedStatement ckStmt;
	try {
	ckStmt = conn.prepareStatement(sqlcheck);
	ckStmt.setString(1,username);
	ResultSet rs=ckStmt.executeQuery();
	if(rs.next())
	{
	//intent.putExtra("message", "���û����Ѵ��ڣ�");
  	//intent.setClass(RegisterMysql.this, MessageActivity.class);
  	//startActivity( intent);
	}else{
	PreparedStatement preStmt =conn.prepareStatement(sqlin);  
	preStmt.setString(1,username);  
	preStmt.setString(2,passwd); 
	preStmt.setString(3,sex);
	preStmt.setString(4,hobby1);
	preStmt.setString(5,hobby2);
	preStmt.setString(6,hobby3);
	preStmt.setString(7,city);
	//preStmt.setString(8,"0");
	//preStmt.setString(9,"0");
	
	preStmt.executeUpdate();}
	
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
}
