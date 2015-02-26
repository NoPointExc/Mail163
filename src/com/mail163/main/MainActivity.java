package com.mail163.main;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.mail163.R;

import android.R.integer;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity {
	private ListView mListView;

	private SimpleAdapter mSimpleAdapter;
	private List<Map<String,String>> mailList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	mListView = (ListView) findViewById(R.id.listview_main);
		//simpleAdapter()
		/*参数包括
		 * context:上下文
		 * data: 数据源 ( List<? extends Map<String, ?>> ) 一个map 组成的List 集合
		 * 				每一行，也就是一个Map<String, ?>，对应listView 中的一个Item。（因为每一个Item包含好几个组建）
		 * 				每一个String 就是Map中的一个Key值，必须包含From中指定的Key
		 * resource：列表项的布局文件？？？？
		 * from: map 中的Key名，
		 * to:绑定listView 中 Item 的Id， 与from 对应
		 */
	
	
		//1. 初始化适配器
		int  [] to ={R.id.textview_address,R.id.textview_title,R.id.textview_preview};
		String [] from={"address","title","preview"};
		mailList=new ArrayList<Map<String,String>>();
		
		
		mSimpleAdapter=new SimpleAdapter(this, getMails(), R.layout.list_item, from, to);
		mListView.setAdapter(mSimpleAdapter);
		
		//2.初始化ActionBar
		ActionBar actionBar =getActionBar();
		actionBar.setTitle("邮箱大师");
		actionBar.setIcon(null);
	

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * @return 邮件列表
	 */
	private List<Map<String,String>> getMails() {
		
			
			for(int i=0;i<17;i++){
				Map<String, String> map= new HashMap<String, String>();
				map.put("address", "mail_address@163.com"+i);
				map.put("title", "title"+i);
				map.put("preview", "网易养猪场坐落于浙江省湖州市安吉县，总面积约1200亩...");
				
				mailList.add(map);
				
			}
		
			return mailList;
	}
	
	
	
	
	

}
