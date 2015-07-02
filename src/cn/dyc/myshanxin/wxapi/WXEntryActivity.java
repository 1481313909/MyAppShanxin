package cn.dyc.myshanxin.wxapi;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import cn.dyc.myshanxin.R;
import cn.dyc.myshanxin.client.view.AppUtil;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.platformtools.Util;

public class WXEntryActivity extends Activity implements OnClickListener,
		IWXAPIEventHandler {
	private Button addPic, shareBtn;
	private ImageView shareImg;
	

	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // �����IMAGE_CODE���Լ����ⶨ���
	private final int CAMERA_CODE = 1; // �����IMAGE_CODE���Լ����ⶨ���
	private String path = "";
	private static final int THUMB_SIZE = 150;
	// IWXAPI �ǵ�����app��΢��ͨ�ŵ�openapi�ӿ�
	private IWXAPI api;
	// ��ͨ�����APP_ID
	public final String APP_ID = "wx2857466cea9caf02";
	private Bitmap mBitmap = null;
	boolean isPicture = false;
	private String filePath = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share);

		// ͨ��WXAPIFactory��������ȡIWXAPI��ʵ��
		api = WXAPIFactory.createWXAPI(this, APP_ID, false);
		// ����appע�ᵽ΢��
		api.registerApp(APP_ID);
		api.handleIntent(getIntent(), this);

		shareImg = (ImageView) findViewById(R.id.shareImg);
		addPic = (Button) findViewById(R.id.addPic);
		shareBtn = (Button) findViewById(R.id.shareBtn);
		addPic.setOnClickListener(this);
		shareBtn.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addPic:
			new AlertDialog.Builder(this)
					.setTitle("ѡ��")
					.setItems(new String[] { "����", "�������", "ȡ��" },
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0:
										/** ����� */
										Intent intent = new Intent(
												MediaStore.ACTION_IMAGE_CAPTURE);
										startActivityForResult(intent,
												CAMERA_CODE);

										break;
									case 1:
										// ʹ��intent����ϵͳ�ṩ����Ṧ�ܣ�ʹ��startActivityForResult��Ϊ�˻�ȡ�û�ѡ���ͼƬ
										Intent getAlbum = new Intent(
												Intent.ACTION_GET_CONTENT);
										getAlbum.setType(IMAGE_TYPE);
										startActivityForResult(getAlbum,
												IMAGE_CODE);

										break;
									case 2:

										break;

									default:
										break;
									}

								}
							}).show();
			break;
		case R.id.shareBtn:
			// �������״̬
			if (!checkNetworkInfo()) {
				return;
			}
			if (!isPicture) {
				Toast.makeText(WXEntryActivity.this, "��ѡ�����ͼƬ",
						Toast.LENGTH_LONG).show();
			} else {

				if (api.isWXAppInstalled()) {

					new AlertDialog.Builder(this)
							.setTitle("����")
							.setItems(new String[] { "΢�ź���", "΢������Ȧ", "ȡ��" },
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											switch (which) {
											// ΢�ź���
											case 0:
												weixinShare(true);

												break;
											// ΢������Ȧ
											case 1:
												weixinShare(false);
												break;
											case 2:

												break;

											default:
												break;
											}

										}
									}).show();

				} else {

					Toast.makeText(WXEntryActivity.this, "����δ��װ΢��Ӧ��",
							Toast.LENGTH_SHORT).show();

				}
			}
			break;

		default:
			break;
		}

	}

	private void weixinShare(boolean isFriend) {

		File file = new File(path);
		if (!file.exists()) {
			String tip = WXEntryActivity.this
					.getString(R.string.send_img_file_not_exist);
			Toast.makeText(WXEntryActivity.this, tip + " path = " + path,
					Toast.LENGTH_LONG).show();
			return;
		}
		WXImageObject imgObj = new WXImageObject();
		imgObj.setImagePath(path);

		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;

		Bitmap bmp = BitmapFactory.decodeFile(path);
		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE,
				THUMB_SIZE, true);
		bmp.recycle();
		msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

		int imageSize = msg.thumbData.length / 1024;
		if (imageSize > 32) {
			Toast.makeText(WXEntryActivity.this, "�������ͼƬ����", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img");
		req.message = msg;
		req.scene = isFriend ? SendMessageToWX.Req.WXSceneSession
				: SendMessageToWX.Req.WXSceneTimeline;
		api.sendReq(req);
		finish();
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) { // �˴��� RESULT_OK ��ϵͳ�Զ����һ������
			Toast.makeText(WXEntryActivity.this, "����ȡ��", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		switch (requestCode) {
		case IMAGE_CODE:
			Bitmap bm = null;
			// ���ĳ������ContentProvider���ṩ���� ����ͨ��ContentResolver�ӿ�
			ContentResolver resolver = getContentResolver();
			try {
				Uri originalUri = data.getData(); // ���ͼƬ��uri
				bm = MediaStore.Images.Media.getBitmap(resolver, originalUri); // �Եõ�bitmapͼƬ

				// ���￪ʼ�ĵڶ����֣���ȡͼƬ��·����
				String[] proj = { MediaColumns.DATA };

				// ������android��ý�����ݿ�ķ�װ�ӿڣ�����Ŀ�Android�ĵ�
				Cursor cursor = managedQuery(originalUri, proj, null, null,
						null);

				// ���Ҹ������ ����ǻ���û�ѡ���ͼƬ������ֵ
				int column_index = cursor
						.getColumnIndexOrThrow(MediaColumns.DATA);

				// �����������ͷ ���������Ҫ����С�ĺ���������Խ��
				cursor.moveToFirst();

				// ����������ֵ��ȡͼƬ·��
				path = cursor.getString(column_index);
				Bitmap bitmap = AppUtil.getLoacalBitmap(path); // �ӱ���ȡͼƬ(��cdcard�л�ȡ)
				shareImg.setImageBitmap(bitmap); // ����Bitmap
				isPicture = true;
			} catch (IOException e) {
				Toast.makeText(WXEntryActivity.this, e.toString(),
						Toast.LENGTH_SHORT).show();

			}
			break;
		case CAMERA_CODE:
			if (data != null) {
				// HTC
				if (data.getData() != null) {
					// ���ݷ��ص�URI��ȡ��Ӧ��SQLite��Ϣ
					Cursor cursor = getContentResolver().query(data.getData(),
							null, null, null, null);
					if (cursor.moveToFirst()) {
						filePath = cursor.getString(cursor
								.getColumnIndex("_data"));// ��ȡ����·��
					}
					cursor.close();
				} else {
					// ���� С���ֻ������Զ��洢DCIM
					mBitmap = (Bitmap) (data.getExtras() == null ? null : data
							.getExtras().get("data"));
				}

				// ֱ��ǿת���� �����Ҫ��Ϊ��ȥ�߿����
				Bitmap bitmap = mBitmap == null ? null : (Bitmap) mBitmap;

				if (bitmap == null) {
					/**
					 * ��Bitmap��Ϊ�˻�ȡѹ������ļ����� ���û������ͼ�ı���
					 * �ͻ�ȡ��ʵ�ļ��ı���(��ʵͼƬ�������ʱ�ܳ����������������ͼ���)
					 */
					bitmap = BitmapFactory.decodeFile(filePath);
				}
				path = AppUtil.saveBitmap(bitmap);
				shareImg.setImageBitmap(AppUtil.getLoacalBitmap(path)); // ����Bitmap
				isPicture = true;
			}
			break;
		default:
			break;
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		setIntent(intent);
		api.handleIntent(intent, this);
	}

	// ΢�ŷ������󵽵�����Ӧ��ʱ����ص����÷���
	@Override
	public void onReq(BaseReq req) {
	}

	// ������Ӧ�÷��͵�΢�ŵ�����������Ӧ�������ص����÷���
	@Override
	public void onResp(BaseResp resp) {

		String result = "";
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "���ͳɹ�";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "����ȡ��";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "����ʧ��";
			break;
		default:
			result = "�����쳣";
			break;
		}
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}

	private static Boolean isExit = false;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isExit == false) {
				isExit = true;
				Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						isExit = false;
					}
				}, 2000);
			} else {
				finish();
				System.exit(0);
			}
		}
		return false;
	}

	public boolean checkNetworkInfo() {
		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		// mobile 3G Data Network
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		// wifi
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		// ���3G�����wifi���綼δ���ӣ��Ҳ��Ǵ�����������״̬ �����Network Setting���� ���û�������������

		if (mobile == State.CONNECTED || mobile == State.CONNECTING)
			return true;
		if (wifi == State.CONNECTED || wifi == State.CONNECTING)
			return true;

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("���粻����")
				.setTitle("��ʾ")
				.setCancelable(false)
				.setPositiveButton("����", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						// ���������������ý���
						startActivity(new Intent(
								Settings.ACTION_WIRELESS_SETTINGS));
						WXEntryActivity.this.finish();
					}
				})
				.setNegativeButton("�˳�", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						WXEntryActivity.this.finish();
					}
				});
		builder.show();
		return false;

	}
	
	public void btn_back(View v) {     //������ ���ذ�ť
      	this.finish();
      } 

}
