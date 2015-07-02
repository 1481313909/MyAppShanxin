package cn.dyc.myshanxin.client.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class AppUtil {
	/**
	 * �ӱ���ȡͼƬ(��cdcard�л�ȡ)
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis); // /����ת��ΪBitmapͼƬ

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * �Ƿ����SDCard
	 * 
	 */
	public static boolean hasSdCard() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			return true;
		}
		return false;
	}

	/**
	 * ��Bitmap�����ڱ���
	 * 
	 * @param mBitmap
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String saveBitmap(Bitmap mBitmap) {

		try {
			String sdCardPath = "";
			if (hasSdCard()) {
				sdCardPath = Environment.getExternalStorageDirectory()
						.getPath();
			} else {

			}

			String filePath = sdCardPath + "/" + "MicroShare/";

			Date date = new Date(System.currentTimeMillis());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// ʱ���ʽ-��ʾ��ʽ

			String imgPath = filePath + sdf.format(date) + ".png";

			File file = new File(filePath);

			if (!file.exists()) {
				file.mkdirs();
			}
			File imgFile = new File(imgPath);

			if (!imgFile.exists()) {
				imgFile.createNewFile();
			}

			FileOutputStream fOut = new FileOutputStream(imgFile);

			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

			fOut.flush();

			if (fOut != null) {

				fOut.close();
			}
			return imgPath;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
