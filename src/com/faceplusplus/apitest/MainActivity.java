package com.faceplusplus.apitest;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.megvii.apitest.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ImageView MainButton1 = (ImageView) findViewById(R.id.imageView1);
		ImageView MainButton2 = (ImageView) findViewById(R.id.imageView2);
		ImageView MainButton3 = (ImageView) findViewById(R.id.imageView3);
		ImageView MainButton4 = (ImageView) findViewById(R.id.imageView4);

		MainButton1.setOnClickListener(new MainButton1Listener());
		MainButton2.setOnClickListener(new MainButton2Listener());
		MainButton3.setOnClickListener(new MainButton3Listener());
		MainButton4.setOnClickListener(new MainButton4Listener());

	}

	class MainButton1Listener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			Intent TextDetectIntent = new Intent(MainActivity.this,
					PhotoDetectActivity.class);
			startActivity(TextDetectIntent);
		}
	}

	class MainButton2Listener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			Toast.makeText(getApplicationContext(), "ImageView2",
					Toast.LENGTH_LONG).show();
		}
	}

	class MainButton3Listener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			// Toast.makeText(getApplicationContext(), "ImageView3",
			// Toast.LENGTH_LONG).show();
			Intent TextDetectIntent = new Intent(MainActivity.this,
					TextDetectActivity.class);
			startActivity(TextDetectIntent);
		}
	}

	class MainButton4Listener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			Toast.makeText(getApplicationContext(), "ImageView4",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click(); // 调用双击退出函数
		}
		return false;
	}

	/**
	 * 双击退出函数
	 */
	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			finish();
		}
	}

}
