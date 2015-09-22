package com.faceplusplus.apitest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.faceplusplus.shake.ShakeListener;
import com.faceplusplus.shake.ShakeListener.OnShakeListenerCallBack;
import com.megvii.apitest.R;

public class TextDetectActivity extends Activity {

	private final ArrayList<HashMap<String, String>> textEditItem = new ArrayList<HashMap<String, String>>();
	private SimpleAdapter simpleAdapter; // 适配器
	private GridView gridView1;
	private EditText editText1;
	private EditText editText2;
	// private EditText editText3;
	private ImageView btnBeginDetect;
	// private TextView detectResult;
	private ImageView btnClear;
	private ShakeListener mShakeListener = null;
	private OnShakeListenerCallBack CBackShakeListener = null;
	private String strDetectResult1;
	private String strDetectResult2;

	private ImageView imgreturn;

	// private HashMap<String, String> map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_detect);

		imgreturn = (ImageView) super.findViewById(R.id.btnDrawer);
		imgreturn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextDetectActivity.this.finish();
			}
		});

		// 获取grideView控件对象
		gridView1 = (GridView) findViewById(R.id.gridView1);
		editText2 = (EditText) findViewById(R.id.editText1);
		// editText3 = (EditText) findViewById(R.id.editText2);
		// detectResult = (TextView) findViewById(R.id.result);
		btnClear = (ImageView) findViewById(R.id.button2);

		// 获取编辑框对象
		editText1 = (EditText) findViewById(R.id.text);
		editText1.setFocusable(true);

		editText1.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_ENTER == keyCode
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					HashMap<String, String> map = new HashMap<String, String>();
					String strTmp = editText1.getText().toString();
					if (!strTmp.equals("")) {
						if (textEditItem.size() == 10) { // 第一张为默认图片
							Toast.makeText(TextDetectActivity.this,
									"最多只能添加10个", Toast.LENGTH_SHORT).show();
							editText1.setText("");
							return false;
						} else {
							Toast.makeText(TextDetectActivity.this, "添加成功",
									Toast.LENGTH_SHORT).show();
							map.put("itemText", strTmp);
							textEditItem.add(map);
							simpleAdapter = new SimpleAdapter(
									TextDetectActivity.this, textEditItem,
									R.layout.gride_item_add,
									new String[] { "itemText" },
									new int[] { R.id.editText1 });
							gridView1.setAdapter(simpleAdapter);
							simpleAdapter.notifyDataSetChanged();
						}
						editText1.setText("");
						editText1.setFocusable(true);
						return true;
					}
				}
				return false;
			}
		});

		gridView1.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				// HashMap<String, String> map = (HashMap<String, String>)
				// ((GridView) arg0).getItemAtPosition(arg2);
				textEditItem.remove(arg2);
				simpleAdapter.notifyDataSetChanged();
				return true;
			}
		});

		btnBeginDetect = (ImageView) findViewById(R.id.button1);
		btnBeginDetect.setOnClickListener(new DetectClickListener());

		btnClear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// if (!detectResult.getText().toString().equals("")) {
				// detectResult.setText("");
				// }
				if (textEditItem.size() != 0) {
					textEditItem.clear();
					simpleAdapter.notifyDataSetChanged();
				}
				if (textEditItem.size() == 0) {
					Toast.makeText(getApplication(), "备选人数不能为空",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (!editText2.getText().toString().equals("")) {
					editText2.setText("");
				}
				/*
				 * if (!editText3.getText().toString().equals("")) {
				 * editText3.setText(""); }
				 */
				return;
			}
		});

		CBackShakeListener = new OnShakeListenerCallBack() {
			@Override
			public void onShake() {
				if (editText2.getText().toString().equals("")) {
					Toast.makeText(getApplication(), "请完善信息", Toast.LENGTH_LONG)
							.show();
					return;
				}
				if (textEditItem.size() == 0) {
					Toast.makeText(getApplication(), "备选人数不能为空",
							Toast.LENGTH_LONG).show();
					return;
				}
				int selectNum = Integer.valueOf(editText2.getText().toString())
						.intValue();
				if (textEditItem.size() < selectNum) {
					Toast.makeText(getApplication(), "备选人数过少",
							Toast.LENGTH_LONG).show();
					return;
				}
				/*
				 * if (editText3.getText().toString().equals("")) {
				 * Toast.makeText(getApplication(), "主题不能为空",
				 * Toast.LENGTH_LONG).show(); }
				 */

				ArrayList<HashMap<String, String>> listTmp = textEditItem;
				String strDetectResult = "";
				int iIndex = 0;
				while (selectNum > 0) {
					Random random = new Random();
					iIndex = random.nextInt(listTmp.size());
					strDetectResult1 = listTmp.get(iIndex).get("itemText");
					if (1 == selectNum) {
						strDetectResult += strDetectResult1;
					} else {
						strDetectResult += (strDetectResult1 + " ");
					}
					listTmp.remove(iIndex);
					selectNum--;
				}

				AlertDialog.Builder builder = new AlertDialog.Builder(
						TextDetectActivity.this);
				// 设置Title的内容
				builder.setTitle("筛选结果");
				// 设置Content来显示一个信息
				builder.setMessage(strDetectResult);
				// 设置一个PositiveButton
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
								// dialog.dismiss();
								// simpleAdapter.notifyDataSetChanged();
							}
						});
				builder.show();
			}
		};

		initShake();
	}

	class DetectClickListener implements View.OnClickListener {
		@Override
		public void onClick(View view) {

			if (editText2.getText().toString().equals("")
					|| editText2.getText().toString().equals("0")) {
				Toast.makeText(getApplication(), "筛选人数不能为空", Toast.LENGTH_LONG)
						.show();
				return;
			}
			if (textEditItem.size() == 0) {
				Toast.makeText(getApplication(), "备选人数不能为空", Toast.LENGTH_LONG)
						.show();
				return;
			}
			int selectNum = Integer.valueOf(editText2.getText().toString())
					.intValue();
			if (textEditItem.size() < selectNum) {
				Toast.makeText(getApplication(), "备选人数过少", Toast.LENGTH_LONG)
						.show();
				return;
			}

			/*
			 * if (editText3.getText().toString().equals("")) {
			 * Toast.makeText(getApplication(), "主题不能为空",
			 * Toast.LENGTH_LONG).show(); return; }
			 */

			ArrayList<HashMap<String, String>> listTmp = textEditItem;
			String strDetectResult = "";
			int iIndex = 0;
			strDetectResult += "结果如下:\n";
			while (selectNum > 0) {
				Random random = new Random();
				iIndex = random.nextInt(listTmp.size());
				strDetectResult1 = listTmp.get(iIndex).get("itemText");
				if (1 == selectNum) {
					strDetectResult += (strDetectResult1 + "\n");
				} else {
					strDetectResult += (strDetectResult1 + "\n");
				}
				listTmp.remove(iIndex);
				selectNum--;
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(
					TextDetectActivity.this);
			// 设置Title的内容
			builder.setTitle("筛选结果");
			// 设置Content来显示一个信息
			builder.setMessage(strDetectResult);
			// 设置一个PositiveButton
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							// dialog.dismiss();
							// simpleAdapter.notifyDataSetChanged();
						}
					});

			// 显示出该对话框
			builder.show();
		}
	}

	private void initShake() {

		mShakeListener = new ShakeListener(this);
		mShakeListener.setOnShakeListener(CBackShakeListener);
	}

	@Override
	public void onResume() {
		super.onResume();
		mShakeListener.start();
	}

	@Override
	public void onPause() {
		mShakeListener.stop();
		super.onPause();
	}

	@Override
	public void onStop() {
		mShakeListener.stop();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		mShakeListener.stop();
		super.onDestroy();
	}

}
