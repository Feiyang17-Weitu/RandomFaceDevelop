package com.faceplusplus.apitest;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.faceplusplus.api.FaceDetecter;
import com.faceplusplus.api.FaceDetecter.Face;
import com.faceplusplus.shake.TestActivity;
import com.megvii.apitest.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class PhotoDetectActivity extends Activity {

	private Bitmap curBitmap;
	private final static int REQUEST_GET_PHOTO = 1;
	ImageView imageView = null;
	HandlerThread detectThread = null;
	Handler detectHandler = null;
	Button button = null;
	FaceDetecter detecter = null;
	private ImageView image;
	private Bitmap luckybitmap;

	private ArrayList<Bitmap> resbitmap = new ArrayList<Bitmap>();

	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			image.setImageBitmap(resbitmap.get(msg.what));
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_detect);

		image = (ImageView) super.findViewById(R.id.image2);

		detectThread = new HandlerThread("detect");
		detectThread.start();
		detectHandler = new Handler(detectThread.getLooper());

		imageView = (ImageView) findViewById(R.id.imageview);
		curBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.demo);
		imageView.setImageBitmap(curBitmap);
		detecter = new FaceDetecter();
		detecter.init(this, "f32ac3bb10b73ae18f5d289f27e45ee2");

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(100 * 1024 * 1024).diskCacheFileCount(300)
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();

		ImageLoader.getInstance().init(config);
	}

	public class GetFaceLocation implements Runnable {
		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
				try {
					Thread.sleep(500);
					Message message = new Message();
					message.what = i % resbitmap.size();

					mHandler.sendMessage(message);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("thread stop!!");
			// TODO Auto-generated method stub

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		detecter.release(this);// 释放引擎
	}

	public static Bitmap getFaceInfoBitmap(Face[] faceinfos, Bitmap oribitmap) {
		Bitmap tmp;
		tmp = oribitmap.copy(Bitmap.Config.ARGB_8888, true);

		Canvas localCanvas = new Canvas(tmp);
		Paint localPaint = new Paint();
		localPaint.setColor(0xffffff00);
		localPaint.setStyle(Paint.Style.STROKE);
		for (Face localFaceInfo : faceinfos) {
			RectF rect = new RectF(oribitmap.getWidth() * localFaceInfo.left,
					oribitmap.getHeight() * localFaceInfo.top,
					oribitmap.getWidth() * localFaceInfo.right,
					oribitmap.getHeight() * localFaceInfo.bottom);
			localCanvas.drawRect(rect, localPaint);
		}
		return tmp;
	}

	public static ArrayList<Bitmap> getFaceResBitmap(Face[] faceinfos,
			Bitmap backbitmap, Bitmap picsize) {
		ArrayList<Bitmap> resbitmap = new ArrayList<Bitmap>();

		for (int i = 0; i < faceinfos.length; i++) {

			// Bitmap tmp = oribitmap.copy(Bitmap.Config.ARGB_8888, true);
			Bitmap tmp = Bitmap.createScaledBitmap(backbitmap,
					picsize.getWidth(), picsize.getHeight(), false);

			Canvas localCanvas = new Canvas(tmp);
			Paint localPaint = new Paint();
			localPaint.setColor(0xffff0000);
			localPaint.setStyle(Paint.Style.STROKE);
			localPaint.setStrokeWidth(3);
			RectF rect = new RectF(picsize.getWidth() * faceinfos[i].left,
					picsize.getHeight() * faceinfos[i].top, picsize.getWidth()
							* faceinfos[i].right, picsize.getHeight()
							* faceinfos[i].bottom);
			localCanvas.drawRect(rect, localPaint);
			resbitmap.add(tmp);
		}
		return resbitmap;
	}

	public static Bitmap getScaledBitmap(String fileName, int dstWidth) {
		BitmapFactory.Options localOptions = new BitmapFactory.Options();
		localOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(fileName, localOptions);
		int originWidth = localOptions.outWidth;
		int originHeight = localOptions.outHeight;

		localOptions.inSampleSize = originWidth > originHeight ? originWidth
				/ dstWidth : originHeight / dstWidth;
		localOptions.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(fileName, localOptions);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.pick:
			image.setImageBitmap(null);
			startActivityForResult(new Intent(PhotoDetectActivity.this,
					SelectPictureActivity.class), REQUEST_GET_PHOTO);
			// startActivityForResult(new
			// Intent("android.intent.action.PICK",MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
			// REQUEST_GET_PHOTO);
			break;
		case R.id.btnShake:
			startActivity(new Intent(PhotoDetectActivity.this,
					TestActivity.class));
		case R.id.detect:
			detectHandler.post(new Runnable() {

				@Override
				public void run() {

					Face[] faceinfo = detecter.findFaces(curBitmap);// 进行人脸检测
					if (faceinfo == null) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(PhotoDetectActivity.this,
										"未发现人脸信息", Toast.LENGTH_LONG).show();
							}
						});
						return;
					}

					final Bitmap bit = getFaceInfoBitmap(faceinfo, curBitmap);
					BitmapDrawable draw = (BitmapDrawable) getResources()
							.getDrawable(R.drawable.a111);
					Bitmap tmp = draw.getBitmap();
					resbitmap = getFaceResBitmap(faceinfo, tmp, curBitmap);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							imageView.setImageBitmap(bit);
							new Thread(new GetFaceLocation()).start();

							System.gc();
						}
					});
				}
			});

		}
	}

	/*
	 * public void onClick(View view) { switch (view.getId()) { case R.id.pick:
	 * image.setImageBitmap(null); startActivityForResult(new
	 * Intent(PhotoDetectActivity.this, SelectPictureActivity.class),
	 * REQUEST_GET_PHOTO); //startActivityForResult(new
	 * Intent("android.intent.action.PICK"
	 * ,MediaStore.Images.Media.EXTERNAL_CONTENT_URI), REQUEST_GET_PHOTO);
	 * break; case R.id.detect: detectHandler.post(new Runnable() {
	 * 
	 * @Override public void run() {
	 * 
	 * Face[] faceinfo = detecter.findFaces(curBitmap);// 进行人脸检测 if (faceinfo ==
	 * null) { runOnUiThread(new Runnable() {
	 * 
	 * @Override public void run() { Toast.makeText(PhotoDetectActivity.this,
	 * "未发现人脸信息", Toast.LENGTH_LONG) .show(); } }); return; }
	 * 
	 * final Bitmap bit = getFaceInfoBitmap(faceinfo, curBitmap); BitmapDrawable
	 * draw=(BitmapDrawable) getResources().getDrawable(R.drawable.a111); Bitmap
	 * tmp=draw.getBitmap(); resbitmap = getFaceResBitmap(faceinfo,
	 * tmp,curBitmap); runOnUiThread(new Runnable() {
	 * 
	 * @Override public void run() { imageView.setImageBitmap(bit); new
	 * Thread(new GetFaceLocation()).start();
	 * 
	 * System.gc(); } }); } }); } }
	 */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		/*
		 * if (resultCode == RESULT_OK) { switch (requestCode) { case
		 * REQUEST_GET_PHOTO: { if (data != null) { final String str; Uri
		 * localUri = data.getData(); String[] arrayOfString = new String[1];
		 * arrayOfString[0] = "_data"; Cursor localCursor =
		 * getContentResolver().query(localUri,arrayOfString, null, null, null);
		 * if (localCursor == null) return; localCursor.moveToFirst(); str =
		 * localCursor.getString(localCursor .getColumnIndex(arrayOfString[0]));
		 * localCursor.close(); if ((curBitmap != null) &&
		 * (!curBitmap.isRecycled())) curBitmap.recycle(); curBitmap =
		 * getScaledBitmap(str, 600); imageView.setImageBitmap(curBitmap); }
		 * break; } }
		 * 
		 * }
		 */

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_GET_PHOTO: {
				if (data != null) {
					final String str = data
							.getStringExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);
					if ((curBitmap != null) && (!curBitmap.isRecycled()))
						curBitmap.recycle();
					curBitmap = getScaledBitmap(str, 600);
					imageView.setImageBitmap(curBitmap);
				}
				break;
			}
			}

		}
	}
}
