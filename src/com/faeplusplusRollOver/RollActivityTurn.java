package com.faeplusplusRollOver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.megvii.apitest.R;

public class RollActivityTurn extends Activity {

	private GridView grid;
	private ImageView imgView;
	private ArrayList<Bitmap> resbitmap = new ArrayList<Bitmap>();
	// 地址

	private final boolean bool = false;
	private int count;

	private ImageView imgreturn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_rollturn);

		imgreturn = (ImageView) super.findViewById(R.id.btnDrawer);

		imgreturn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RollActivityTurn.this.finish();
			}
		});

		Intent it = super.getIntent();
		resbitmap = (ArrayList<Bitmap>) it.getSerializableExtra("resbitmap");

		count = resbitmap.size() > 9 ? 9 : resbitmap.size();
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < count; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image", R.drawable.cardback);
			listItems.add(listItem);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.cell, new String[] { "image" },
				new int[] { R.id.imgView });

		grid = (GridView) findViewById(R.id.gridview);
		grid.setAdapter(simpleAdapter);
		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			int c = 0;

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				// TODO Auto-generated method stub
				// imgView.setImageResource(R.drawable.test1);
				Animation animation = AnimationUtils.loadAnimation(
						RollActivityTurn.this, R.anim.back_scale);
				arg1.startAnimation(animation);

				Animation animation2 = AnimationUtils.loadAnimation(
						RollActivityTurn.this, R.anim.front);
				arg1.startAnimation(animation2);
				if (count > 0) {
					count--;
					c++;
					int i = (int) (Math.random() * resbitmap.size());
					((ImageView) arg1.findViewById(R.id.imgView))
							.setImageBitmap(resbitmap.get(i));
					Toast.makeText(getApplicationContext(),
							"您翻开了第" + c + "个牌子", Toast.LENGTH_SHORT).show();
					resbitmap.remove(i);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							RollActivityTurn.this);
					// 设置Title的内容
					builder.setTitle("筛选结果");
					// 设置Content来显示一个信息
					builder.setMessage("完事了，别点了");
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

			}
		});
	}
}
