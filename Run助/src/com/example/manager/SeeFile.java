package com.example.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.example.manager.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class SeeFile extends Activity {
	int count = 0;
	ScrollView scr_view;
	String dataFilePath;
	String dataFileName;
	String[] rlp;
	File file;		
	public static TextView runner;
	public static TextView iventName;
	public static TextView item;
	public static TextView[] distance;
	public static TextView[] meter;
	public static TextView[] time;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.see_file);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.stopwatch_mode);
		
		scr_view = (ScrollView) findViewById(R.id.scrollView1);
		Intent intent = getIntent();
		Button button = (Button) findViewById(R.id.button1);
		CheckBox check = (CheckBox) findViewById(R.id.checkBox1);
		TextView text = (TextView) findViewById(R.id.TextView01);
		String mode = intent.getStringExtra("mode");
		dataFilePath = intent.getStringExtra("path");
		dataFileName = intent.getStringExtra("data");
		button.setOnClickListener(new OfData());
		check.setOnClickListener(new modeChange());
		text.setText(mode);
		
		try {
			InputStream in = new FileInputStream(dataFilePath + dataFileName + ".txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, "UTF-8"));
			String[] data = new String[50];
			while ((data[count] = reader.readLine()) != null) {
				Log.d("readData", data[count]);
				count++;
			}
			Log.d("datalength", String.valueOf(count));
			rlp = new String[count - 2];
			if(mode.equals("現在 : SPLIT")) {
				display(data, "SPLIT");
			} else {
				display(data, "RLP");
			}
			reader.close();
		} catch (IOException e) {

		}
	}

	class OfData implements OnClickListener {
		SeeFile seefile = new SeeFile();
		String[] dis = new String[50];
		String[] laptime = new String[50];
		String runnerName;
		String iventName;
		String item;

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(SeeFile.this, RunnerData.class);

			getCurrentData();
			intent.putExtra("laptime", laptime);
			intent.putExtra("rlp", rlp);
			intent.putExtra("name", runnerName);
			intent.putExtra("runNum","Complication");
			intent.putExtra("distant", dis);
			intent.putExtra("iventName", iventName);
			intent.putExtra("fileName", dataFileName);
			intent.putExtra("item", item);
			intent.putExtra("mode", "現在 : SPLIT");	
			intent.putExtra("state", "first");
			
			
			startActivity(intent);
		}
		
		private void getCurrentData() {
			SeeFile seefile = new SeeFile();
			runnerName = seefile.runner.getText().toString();
			iventName = seefile.iventName.getText().toString();
			item = seefile.item.getText().toString();
			Log.d("runnerName", runnerName);

			for (int i = 3; seefile.time[i] != null; i++) {
				laptime[i - 3] = seefile.time[i].getText().toString();
				dis[i - 3] = seefile.distance[i].getText().toString();
				Log.d("repeatNum", String.valueOf(i));
				Log.d("time", laptime[i - 3]);
				Log.d("distance", dis[i - 3]);
			}
		}
	}
	class modeChange implements OnClickListener {
		@Override
		public void onClick(View v) {
			TextView text = (TextView) findViewById(R.id.TextView01);
			Intent intent = new Intent(SeeFile.this, SeeFile.class);
			intent.putExtra("path", dataFilePath);
			intent.putExtra("data", dataFileName);
			if(text.getText().toString().equals("現在 : SPLIT")) {
				intent.putExtra("mode","現在 : RLP");
				text.setText("現在 : RLP");
				finish();
				startActivity(intent);
			} else {
				intent.putExtra("mode","現在 : SPLIT");
				text.setText("現在 : SPLIT");
				finish();
				startActivity(intent);
			}
		}
	}

	
	private void display(String[] data, String mode) {
		GUI setupGui = new GUI();
		runner = new TextView(this);
		iventName = new TextView(this);
		item = new TextView(this);
		distance = new TextView[data.length];
		meter = new TextView[data.length];
		time = new TextView[data.length];
		LinearLayout overall = new LinearLayout(this);
		LinearLayout pairList = new LinearLayout(this);
		LinearLayout[] distanceAndTime = new LinearLayout[data.length];

		overall.setOrientation(overall.VERTICAL);
		pairList.setOrientation(pairList.VERTICAL);

		runner.setTextSize(40);
		runner.setText(data[0]);
		iventName.setTextSize(40);
		iventName.setText(data[1]);
		item.setTextSize(40);
		item.setText(data[2]);
		overall.addView(runner);
		overall.addView(iventName);
		overall.addView(item);
		overall.addView(pairList);

		Log.d("data 0", data[0]);			
		Log.d("data 1", data[1]);
		Log.d("data 2", data[2]);
		
		for (int i = 3; i < count; i++) {
			distanceAndTime[i] = new LinearLayout(this);
			distance[i] = new TextView(this);
			meter[i] = new TextView(this);
			time[i] = new TextView(this);
			String[] list = data[i].split(",");
			Log.d("meter", list[0]);
			Log.d("time", list[1]);
			rlp[i - 3] = list[2];
			distance[i] = setupGui.setupTextView(distance[i], list[0], 40);
			meter[i] = setupGui.setupTextView(meter[i], "m" , 40);
			meter[i] = setupGui.setupTextView(meter[i], 80, 60, "m");
			if(mode.equals("SPLIT")) {
				time[i] = setupGui.setupTextView(time[i], list[1], 40);
			} else {
				time[i] = setupGui.setupTextView(time[i], list[2], 40);				
			}

			distanceAndTime[i] = setupGui.setupLinearLayout(distanceAndTime[i],
					distance[i], meter[i], time[i]);
			pairList.addView(distanceAndTime[i]);
		}
		scr_view.addView(overall);
	}
}