package com.faceplusplus.apitest;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.faeplusplusRollOver.RollActivity;
import com.megvii.apitest.R;

public class MainActivity extends Activity {

	SharedPreferences logintips;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		logintips = getSharedPreferences("notips", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor;

		// if(logintips.getString(key, defValue))
		// {
		// 启动提示
		final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(
				this);
		builder.setTitle("告别选择困难，就在今天~\n只需拍张照，看看谁是幸运儿！");
		LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.first_login_tips, null);
		builder.setView(linearLayout);
		final CheckBox nomore = (CheckBox) findViewById(R.id.nomore);
		builder.setPositiveButton("确定", null);

		/*
		 * builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
		 * {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) { if
		 * (nomore.isChecked() == true) { SharedPreferences.Editor editor =
		 * logintips .edit(); editor.putString("notips", "1"); editor.commit();
		 * } else { SharedPreferences.Editor editor = logintips .edit();
		 * editor.putString("notips", "0"); editor.commit(); } } });
		 * builder.create().show(); }
		 */
		builder.create().show();
		// 启动提示End
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
			Intent TextDetectIntent = new Intent(MainActivity.this,PhotoDetectActivity.class);
			startActivity(TextDetectIntent);
		}
	}

	class MainButton2Listener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			Intent TextDetectIntent = new Intent(MainActivity.this,RollActivity.class);
			startActivity(TextDetectIntent);
		}
	}

	class MainButton3Listener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			// Toast.makeText(getApplicationContext(), "ImageView3",
			// Toast.LENGTH_LONG).show();
			Intent TextDetectIntent = new Intent(MainActivity.this,TextDetectActivity.class);
			startActivity(TextDetectIntent);
		}
	}

	class MainButton4Listener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			Toast.makeText(getApplicationContext(), "ImageView4",Toast.LENGTH_LONG).show();
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
