package com.faeplusplusRollOver;

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
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.faceplusplus.api.FaceDetecter;
import com.faceplusplus.api.FaceDetecter.Face;
import com.faceplusplus.apitest.SelectPictureActivity;
import com.megvii.apitest.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class RollActivity extends Activity {

	private Bitmap curBitmap;
	private final static int REQUEST_GET_PHOTO = 1;
	ImageView imageView = null;
	HandlerThread detectThread = null;
	Handler detectHandler = null;
	Button button = null;
	FaceDetecter detecter = null;

	private ArrayList<Bitmap> resbitmap = new ArrayList<Bitmap>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_roll);

		
		detectThread = new HandlerThread("detect");
		detectThread.start();
		detectHandler = new Handler(detectThread.getLooper());
		
		detecter = new FaceDetecter();
		detecter.init(this, "f32ac3bb10b73ae18f5d289f27e45ee2");
		

		imageView = (ImageView) findViewById(R.id.imageview);
		curBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.demo);
		imageView.setImageBitmap(curBitmap);
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(100 * 1024 * 1024).diskCacheFileCount(300)
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();

		ImageLoader.getInstance().init(config);
		
		OnDetect();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		detecter.release(this);// 释放引擎
		for(int i=0;i<resbitmap.size();i++){
			if(!resbitmap.get(i).isRecycled()){
				resbitmap.get(i).recycle();
			}
		}
		
		if ((curBitmap != null) && (!curBitmap.isRecycled()))
			curBitmap.recycle();
		
	}


	public static ArrayList<Bitmap> getFaceResBitmap(Face[] faceinfos, Bitmap oribitmap) {
		ArrayList<Bitmap> resbitmap = new ArrayList<Bitmap>();

		int x,y,width,height = 0;
		for (int i = 0; i < faceinfos.length; i++) {
			// Bitmap tmp = oribitmap.copy(Bitmap.Config.ARGB_8888, true);
			x = (int)(oribitmap.getWidth() * ((faceinfos[i].left-(faceinfos[i].right-faceinfos[i].left)/3)>0?(faceinfos[i].left-(faceinfos[i].right-faceinfos[i].left)/3):0));
			y = (int)(oribitmap.getHeight() * ((faceinfos[i].top-(faceinfos[i].bottom-faceinfos[i].top)/2)>0?(faceinfos[i].top-(faceinfos[i].bottom-faceinfos[i].top)/2):0));
            width = (int)(oribitmap.getWidth() * (faceinfos[i].right-faceinfos[i].left)*5/3);
			height = (int)(oribitmap.getHeight() * (faceinfos[i].bottom-faceinfos[i].top)*2);
			if(x + width > oribitmap.getWidth()){
				width = oribitmap.getWidth() - x;
			}
			if(y + height > oribitmap.getHeight()){
				height = oribitmap.getHeight() - y;
			}
			Bitmap tmp = Bitmap.createBitmap(oribitmap, x,y, width,height);
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
		case R.id.btnpick:

			startActivityForResult(new Intent(RollActivity.this,SelectPictureActivity.class), REQUEST_GET_PHOTO);
			// startActivityForResult(new
			// Intent("android.intent.action.PICK",MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
			// REQUEST_GET_PHOTO);
			break;
		case R.id.btnnext:
			if (resbitmap.size()<=0) {
				break;
			}
			
			Intent rollIntent = new Intent(RollActivity.this,RollActivityTurn.class);
			rollIntent.putExtra("resbitmap", resbitmap);
			startActivity(rollIntent);
			break;
			default:
				break;
		}
	}
	
	public void OnDetect(){
		Toast.makeText(RollActivity.this, "正在识别图片,请稍后······", Toast.LENGTH_LONG).show();
		System.out.println("start detect");
		detectHandler.post(new Runnable() {

			@Override
			public void run() {
				resbitmap.clear();
				Face[] faceinfo = detecter.findFaces(curBitmap);// 进行人脸检测
				if (faceinfo == null) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(RollActivity.this,"未发现人脸信息", Toast.LENGTH_LONG).show();
						}
					});
					return;
				}
				
				for(int i=0;i<resbitmap.size();i++){
					if(!resbitmap.get(i).isRecycled()){
						resbitmap.get(i).recycle();
					}
				}
				resbitmap = getFaceResBitmap(faceinfo,curBitmap);
				Toast.makeText(RollActivity.this, "识别成功,可以翻牌了！！！", Toast.LENGTH_LONG).show();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						System.gc();
					}
				});
			}
		});
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

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
					OnDetect();
				}
				break;
			}
			}

		}
	}

}
