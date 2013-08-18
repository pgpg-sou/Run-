package com.example.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Runtime.getRuntime().freeMemory();
		ImageView timecalc = (ImageView) findViewById(R.id.imageView1);
		ImageView stopwatch = (ImageView) findViewById(R.id.imageView2);
		ImageView data = (ImageView) findViewById(R.id.imageView3);
		ImageView diary = (ImageView) findViewById(R.id.imageView4);

		timecalc.setOnClickListener(this);
		stopwatch.setOnClickListener(this);
		data.setOnClickListener(this);
		diary.setOnClickListener(this);

	}

	public void onClick(View v) {
		ImageView image = (ImageView) v;
		Intent intent;

		switch (image.getId()) {
		case R.id.imageView1:
			intent = new Intent(MainActivity.this, TimeCalc.class);
			startActivity(intent);
			break;
		case R.id.imageView2:
			intent = new Intent(MainActivity.this, StopWatch.class);
			startActivity(intent);
			break;
		case R.id.imageView3:
			intent = new Intent(MainActivity.this, DirectoryList.class);
			startActivity(intent);
			break;
		case R.id.imageView4:
			break;

		}

		/*
		 * switch(button.getId()) { case R.id.stopwatch: intent = new
		 * Intent(MainActivity.this,StopWatch.class); startActivity(intent);
		 * break; case R.id.timecalc: intent = new
		 * Intent(MainActivity.this,TimeCalc.class); startActivity(intent);
		 * break; case R.id.data: intent = new
		 * Intent(MainActivity.this,DirectoryList.class); startActivity(intent);
		 * break; }
		 */
	}
}
