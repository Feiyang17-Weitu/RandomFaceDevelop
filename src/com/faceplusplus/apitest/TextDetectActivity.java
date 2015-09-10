package com.faceplusplus.apitest;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.megvii.apitest.R;

public class TextDetectActivity extends Activity {

	private ArrayList<HashMap<String, String>> textEditItem = new ArrayList<HashMap<String, String>>();
	private SimpleAdapter simpleAdapter;     //适配器
	private GridView gridView1;
	private EditText editText;
	//private HashMap<String, String> map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_detect);
		
		//获取grideView控件对象
        gridView1 = (GridView) findViewById(R.id.gridView1);
        
        //Button btnBeginDetectButton = (Button) findViewById(R.id.begin);
        
        //获取编辑框对象
        editText = (EditText) findViewById(R.id.text);
        editText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) 
				{
					HashMap<String, String> map = new HashMap<String, String>();
					String strTmp = editText.getText().toString();
					if (!strTmp.equals("")) 
					{
						if(textEditItem.size() == 10) { //第一张为默认图片
							Toast.makeText(TextDetectActivity.this, "最多只能添加15个", Toast.LENGTH_SHORT).show();
							editText.setText("");
							return false;
						}
						else {
							Toast.makeText(TextDetectActivity.this, "添加成功", Toast.LENGTH_SHORT).show();			
					        map.put("itemText", strTmp);
					        textEditItem.add(map);
					        simpleAdapter = new SimpleAdapter(TextDetectActivity.this, 
					        		textEditItem, R.layout.gride_item_add,
					                new String[] {"itemText"}, new int[] {R.id.editText1}); 
					        gridView1.setAdapter(simpleAdapter);
					        simpleAdapter.notifyDataSetChanged();
						}
						editText.setText("");
						return true;
					}		
				}
				return false;
			}
        });
	
		gridView1.setOnItemLongClickListener(new OnItemLongClickListener() {
			 
		    @Override
		    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		        // TODO Auto-generated method stub
		    	//HashMap<String, String>  map = (HashMap<String, String>) ((GridView) arg0).getItemAtPosition(arg2);
		    	textEditItem.remove(arg2);
		    	simpleAdapter.notifyDataSetChanged();
		        return true;
		    }      
		});
		
/*		btnBeginDetectButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Random 
			}
		});*/
	}
}
