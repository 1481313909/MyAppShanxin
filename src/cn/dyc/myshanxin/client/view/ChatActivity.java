package cn.dyc.myshanxin.client.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.dyc.myshanxin.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;



public class ChatActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */

	private Button mBtnSend;
	private Button mBtnSendSecret;
	private Button mBtnBack;
	private EditText mEditTextContent;
	private ListView mListView;
	private ChatMsgViewAdapter mAdapter;
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
	
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_xiaohei);
        //����activityʱ���Զ����������
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        initView();
        
        initData();
    }
    
    
    public void initView()
    {
    	mListView = (ListView) findViewById(R.id.listview);
    	mBtnSend = (Button) findViewById(R.id.btn_send);
    	mBtnSend.setOnClickListener(this);
    	mBtnSendSecret = (Button) findViewById(R.id.btn_secret_send);
    	mBtnSendSecret.setOnClickListener(this);
    	mBtnBack = (Button) findViewById(R.id.btn_back);
    	mBtnBack.setOnClickListener(this);
    	
    	mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
    }
    
    private String[]msgArray = new String[]{"�Է�����", "�ҳ��ˣ����أ�", "��Ҳ����", "���ڸ�����", 
    										"����Ӱ", "Ŷ���ðɣ�׼������һ���ȥ����",
    										"̫����������˯����", "��",};
    
    private String[]dataArray = new String[]{"2014-05-01 21:00", "2014-05-01 21:10", 
    										"2014-05-01 21:11", "2014-05-01 21:20", 
    										"2014-05-01 21:30", "2014-05-01 21:35", 
    										"2014-05-01 21:40", "2014-05-01 21:50"}; 
    private final static int COUNT = 8;
    public void initData()
    {
    	for(int i = 0; i < COUNT; i++)
    	{
    		ChatMsgEntity entity = new ChatMsgEntity();
    		entity.setDate(dataArray[i]);
    		if (i % 2 == 0)
    		{
    			entity.setName("С��");
    			entity.setMsgType(true);
    		}else{
    			entity.setName("С��");
    			entity.setMsgType(false);
    		}
    		
    		entity.setText(msgArray[i]);
    		mDataArrays.add(entity);
    	}

    	mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);
		
    }


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.btn_send:
			send0();
			break;
		case R.id.btn_secret_send:
			send1();
			break;
		case R.id.btn_back:
			finish();
			break;
		}
	}
	
	private void send0()
	{
		String contString = mEditTextContent.getText().toString();
		if (contString.length() > 0)
		{
			ChatMsgEntity entity = new ChatMsgEntity();
			entity.setDate(getDate());
			entity.setName("С��");
			entity.setMsgType(false);
			entity.setText(contString);
			
			mDataArrays.add(entity);
			mAdapter.notifyDataSetChanged();
			
			mEditTextContent.setText("");
			
			mListView.setSelection(mListView.getCount() - 1);
		}
	}
	
	private void send1()
	{
		String contString = mEditTextContent.getText().toString();
		if (contString.length() > 0)
		{
			ChatMsgEntity entity = new ChatMsgEntity();
			entity.setDate(getDate());
			entity.setName("С��");
			entity.setMsgType(false);
			entity.setText(contString);
			
			mDataArrays.add(entity);
			mAdapter.notifyDataSetChanged();
			
			mEditTextContent.setText("");
			
			mListView.setSelection(mListView.getCount() - 1);
		}
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run(){
				Intent intent = new Intent (ChatActivity.this,ChatActivityClear.class);			
				startActivity(intent);			
				ChatActivity.this.finish();
				Toast.makeText(getApplicationContext(), "��˽��������Ϣɾ��~", Toast.LENGTH_SHORT).show();
			}
		}, 2000);
	}
	
    private String getDate() {
        Calendar c = Calendar.getInstance();

        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH));
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));
        
        
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + mins); 
        						
        						
        return sbBuffer.toString();
    }
    
    
    public void head_xiaohei(View v) {     //����ͷ����ʾ������Ϣ
    	Intent intent = new Intent (ChatActivity.this,InfoXiaohei.class);			
		startActivity(intent);	
      }
    public void head_xiaobai(View v) {     //����ͷ����ʾ������Ϣ
    	Intent intent = new Intent (ChatActivity.this,InfoXiaobai.class);			
		startActivity(intent);	
      }
}