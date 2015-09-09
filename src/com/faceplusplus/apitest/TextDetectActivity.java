package com.faceplusplus.apitest;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.megvii.apitest.R;

public class TextDetectActivity extends Activity {

	private int flag = 0;
	private int begin = 0;
	private final String[] names = new String[33];
	Timer timer;

	private final String[] areas = new String[] { "蔡瑞", "徐永浩", "范金端", "王源",
			"李英明", "刘宇阳", "聂明炎", "张启震", "杨志爽", "马玉炎", "邢全伟", "詹旭琛", "刘津旭",
			"陈琳", "陈凯", "谢冲", "王辰", "舒震", "刘坚强", "沈依铭", "欧阳知雨", "王晨", "路少雯",
			"王蕾", "郭毛", "宋蜜", "刘琛", "任梦萤", "徐瑶", "叶乔羽", "董乐", "孟燦" };
	private final boolean[] areaState = new boolean[] { false, false, false,
			false, false, false, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false,
			false, false };
	private ListView areaCheckListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_detect);

		final TextView Edit = (TextView) findViewById(R.id.EditBallot);
		final Button StartBn = (Button) findViewById(R.id.BnStart);
		final Button StopBn = (Button) findViewById(R.id.BnStop);
		final Button ChooseBn = (Button) findViewById(R.id.BnChoose);

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0x1122) {
					Edit.setText(names[flag]);
				}

				super.handleMessage(msg);

			}

		};

		StartBn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (timer == null) {
					timer = new Timer();
				}
				timer.schedule(new TimerTask() {

					@Override
					public void run() {

						flag++;
						if (flag > begin) {
							flag = 0;
						}

						Message m = new Message();
						m.what = 0x1122;
						handler.sendMessage(m);
					}
				}, 0, 100);
				StartBn.setClickable(false);
				StopBn.setClickable(true);
			}
		});

		StopBn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				timer.cancel();
				timer = null;
				StartBn.setClickable(true);
				StopBn.setClickable(false);
			}
		});

		ChooseBn.setOnClickListener(new CheckBoxClickListener());

	}

	class CheckBoxClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			begin = 0;
			AlertDialog ad = new AlertDialog.Builder(TextDetectActivity.this)
					.setTitle("抽签者姓名")
					.setMultiChoiceItems(areas, areaState,
							new DialogInterface.OnMultiChoiceClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int whichButton, boolean isChecked) {
								}
							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int whichButton) {

									String s = "您选择了:";
									for (int i = 0; i < areas.length; i++) {
										if (areaCheckListView
												.getCheckedItemPositions().get(
														i)) {
											s += (i + 1)
													+ ":"
													+ areaCheckListView
															.getAdapter()
															.getItem(i) + "  ";
											names[begin] = areaCheckListView
													.getAdapter().getItem(i)
													.toString();
											begin++;
										} else {
											areaCheckListView
													.getCheckedItemPositions()
													.get(i, false);
										}
									}
									if (areaCheckListView
											.getCheckedItemPositions().size() > 0) {
										Toast.makeText(TextDetectActivity.this,
												s, Toast.LENGTH_LONG).show();
									} else {
										// 没有选择
									}
									dialog.dismiss();
								}
							}).setNegativeButton("取消", null).create();
			areaCheckListView = ad.getListView();
			ad.show();
		}
	}

}
