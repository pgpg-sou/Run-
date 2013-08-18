package com.example.manager;

import com.example.manager.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class SelectDeleteDirectory extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_delete);
		
		DirectoryList dl = new DirectoryList();
		CheckBox[] checkbox = new CheckBox[50];
		ScrollView scroll = (ScrollView) findViewById(R.id.scrollView1);
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(ll.VERTICAL);
		char ch;
		
		for(int i = 0;i < dl.directoryName.length;i++) {
			checkbox[i] = new CheckBox(this);
			checkbox[i].setTextSize(25);
			checkbox[i].setText(dl.directoryName[i]);
			ll.addView(checkbox[i]);
		}	
		scroll.addView(ll);
	}
}
