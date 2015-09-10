package com.faeplusplusRollOver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_rollturn);
		
		Intent it = super.getIntent();
		resbitmap = (ArrayList<Bitmap>) it.getSerializableExtra("resbitmap");

		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < resbitmap.size(); i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image", R.drawable.cardback);
			listItems.add(listItem);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.cell, new String[] { "image" },new int[] { R.id.imgView });
		
		grid = (GridView) findViewById(R.id.gridview);
		grid.setAdapter(simpleAdapter);
		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				// imgView.setImageResource(R.drawable.test1);
				
				Toast.makeText(getApplicationContext(), "This is " + arg3,Toast.LENGTH_LONG).show();
				Animation animation = AnimationUtils.loadAnimation(RollActivityTurn.this, R.anim.back_scale);
				arg1.startAnimation(animation);

				Animation animation2 = AnimationUtils.loadAnimation(RollActivityTurn.this, R.anim.front);
				arg1.startAnimation(animation2);
				
				
				((ImageView)arg1.findViewById(R.id.imgView)).setImageBitmap(resbitmap.get(arg2));
			}
		});
	}
}
