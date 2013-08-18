package com.example.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class RunnerData extends Activity implements OnClickListener {
	EditText runner, iventName;
	EditText item;
	EditText[] editTextList;
	TextView[] lap, meter;
	Button nameReference;
	Button iventReference;
	Button itemReference;

	ImageView[] image;
	String[] laptime, rlp;
	String[] distantOfLap;
	GUI setupGui = new GUI();
	TransTools transTools = new TransTools();
	String MODE, timerMode;
	String name, ivent, itemName;
	String previousFileName;
	int count = 0;
	String num;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.runner_data1);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.stopwatch_mode);

		Intent intent = getIntent();
		Button register = (Button) findViewById(R.id.register);
		Button cancel = (Button) findViewById(R.id.cancel);
		final CheckBox mode = (CheckBox) findViewById(R.id.checkBox1);
		final TextView text = (TextView) findViewById(R.id.TextView01);
		nameReference = new Button(this);
		iventReference = new Button(this);
		itemReference = new Button(this);

		mode.setOnClickListener(new modeChange());
		register.setOnClickListener(this);
		cancel.setOnClickListener(this);
		iventReference.setOnClickListener(new browse());
		nameReference.setOnClickListener(new browse());
		itemReference.setOnClickListener(new browse());
		iventReference.setText("イベント名参照");
		nameReference.setText("名前参照");
		itemReference.setText("距離参照");
		laptime = intent.getStringArrayExtra("laptime");
		rlp = intent.getStringArrayExtra("rlp");
		name = intent.getStringExtra("name");
		num = intent.getStringExtra("runNum");
		distantOfLap = intent.getStringArrayExtra("distant");
		previousFileName = intent.getStringExtra("fileName");
		ivent = intent.getStringExtra("iventName");
		itemName = intent.getStringExtra("item");
		timerMode = intent.getStringExtra("mode");
		String state = intent.getStringExtra("state");

		if (num.equals("Complication") || state.equals("second")) {
			text.setText(timerMode);
			if (timerMode.equals("現在 : RLP")) {
				mode.setText("SPLIT");
				displayView(rlp, itemName);
			} else {
				mode.setText("RLP");
				displayView(laptime, itemName);
			}

		} else {
			final String[] items = { "100", "200", "400", "600", "800", "1000",
					"1500", "2000", "3000", "5000" };
			new AlertDialog.Builder(RunnerData.this).setTitle("走った距離を選択してください")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setItems(items, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							text.setText(timerMode);
							Log.d("check", items[which]);
							if (timerMode.equals("現在 : RLP")) {
								mode.setText("SPLIT");
								displayView(rlp, items[which]);
							} else {
								mode.setText("RLP");
								displayView(laptime, items[which]);
							}
						}
					}).show();

		}

	}

	public void displayView(String[] time, String itemName) {
		int distance = 0;
		double[] sumSecond;
		LinearLayout overall = new LinearLayout(this);
		LinearLayout pairList = new LinearLayout(this);
		LinearLayout pairName = new LinearLayout(this);
		LinearLayout pairIvent = new LinearLayout(this);
		LinearLayout pairItem = new LinearLayout(this);
		ScrollView scr_view = (ScrollView) findViewById(R.id.scr_view);

		if (num.equals("Complication")) {
			MODE = "Complication";
			sumSecond = new double[10];
		} else {
			MODE = "Register";
			sumSecond = transTools.transSecond(time);
		}
		while (time[count] != null)
			count++;
		overall.setOrientation(overall.VERTICAL);
		pairList.setOrientation(pairList.VERTICAL);

		LinearLayout[] distanceAndTime = new LinearLayout[count];
		TextView namelabel = new TextView(this);
		TextView iventlabel = new TextView(this);
		TextView distanceLabel = new TextView(this);
		editTextList = new EditText[count];
		image = new ImageView[count];
		runner = new EditText(this);
		iventName = new EditText(this);
		item = new EditText(this);
		lap = new TextView[count];
		meter = new TextView[count];

		setupGui.setupTextView(iventlabel, 140, 50, "ivent : ");
		setupGui.setupTextView(namelabel, 150,50, "走者 : ");
		setupGui.setupTextView(distanceLabel, 120, 50, "距離 : ");
		runner = setupGui.setUpEditText(runner, 300, 120, name);
		iventName = setupGui.setUpEditText(iventName, 330, 120, ivent);
		item = setupGui.setUpEditText(item, 200, 80, itemName);

		pairName = setupGui.setupLinearLayout(pairName, namelabel, runner,
				nameReference);
		pairIvent = setupGui.setupLinearLayout(pairIvent, iventlabel,
				iventName, iventReference);
		pairItem = setupGui.setupLinearLayout(pairItem, distanceLabel, item,
				itemReference);
		overall.addView(pairName);
		overall.addView(pairIvent);
		overall.addView(pairItem);
		overall.addView(pairList);

		for (int i = 0; i < count; i++) {
			distanceAndTime[i] = new LinearLayout(this);
			lap[i] = new TextView(this);
			lap[i] = setupGui.setupTextView(lap[i], time[i], 35);
			meter[i] = new TextView(this);
			meter[i] = setupGui.setupTextView(meter[i], 80, 120, "m");
			editTextList[i] = new EditText(this);
			image[i] = new ImageView(this);

			TextView lapview = new TextView(this);
			Resources r = getResources();
			Bitmap bmp = BitmapFactory.decodeResource(r,
					R.drawable.ic_action_delete);
			image[i].setImageBitmap(bmp);
			image[i].setScaleType(ImageView.ScaleType.CENTER);
			image[i].setOnClickListener(new deleteData(i));
			if (MODE.equals("Complication")) {
				String lapIndex = "Lap" + String.valueOf(i + 1);
				setupGui.setupTextView(lapview, lapIndex, 15);
				editTextList[i] = setupGui.setUpEditText(editTextList[i], 200,
						80, distantOfLap[i]);
				distanceAndTime[i] = setupGui.setupLinearLayout(
						distanceAndTime[i], lapview, editTextList[i], meter[i]);
				distanceAndTime[i].addView(lap[i]);
				distanceAndTime[i].addView(image[i]);
				pairList.addView(distanceAndTime[i]);
			} else {
				Log.d("num", String.valueOf(count));
				Log.d("item", itemName);
				if (timerMode.equals("現在 : RLP")) {
					distance = Integer.parseInt(itemName) / count;
				} else {
					distance += Integer.parseInt(itemName) / count;
				}
				String lapIndex = "Lap" + String.valueOf(i + 1);
				setupGui.setupTextView(lapview, 120, 80, lapIndex);

				editTextList[i] = setupGui.setUpEditText(editTextList[i], 200,
						80, String.valueOf(distance));

				distanceAndTime[i] = setupGui.setupLinearLayout(
						distanceAndTime[i], lapview, editTextList[i], meter[i]);
				distanceAndTime[i].addView(lap[i]);
				distanceAndTime[i].addView(image[i]);
				pairList.addView(distanceAndTime[i]);
			}
		}
		scr_view.addView(overall);

	}

	public void onClick(View v) {
		Button button = (Button) v;

		switch (button.getId()) {
		case R.id.register:
			if (iventName.getText().toString().equals("")
					|| runner.getText().toString().equals("")) {
				new AlertDialog.Builder(RunnerData.this)
						.setTitle("名前又はイベント名を入力してください")
						.setPositiveButton("決定", null).show();
			} else {
				RegisterData rd = new RegisterData();
				String name = runner.getText().toString();
				File mainDir = new File(
						"/data/data/com.example.manager/Runner/");
				File dirName = new File(
						"/data/data/com.example.manager/Runner/" + name + "/");
				if (!mainDir.exists())
					mainDir.mkdir();
				if (!dirName.exists())
					dirName.mkdir();
				
				showMessage("データを登録しました。\ndataから参照してください");
				fileWrite(name);
				finish();
			}
			break;
		case R.id.cancel:
			finish();
			break;
		}
	}

	public void fileWrite(String name) {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);

		try {
			String ivent = iventName.getText().toString();
			String items = item.getText().toString();
			OutputStream runnerData;
			String path = "/data/data/com.example.manager/Runner/";
			if (MODE.equals("Complication")) {
				previousFileName = path + name + "/" + previousFileName
						+ ".txt";
				Log.d("previousFileName", previousFileName);
				runnerData = new FileOutputStream(previousFileName);
			} else {
				String fileName = path + name + "/" + ivent + ".txt";
				Log.d("outputfileName", fileName);
				runnerData = new FileOutputStream(fileName);
			}

			PrintWriter writer = new PrintWriter(new OutputStreamWriter(
					runnerData, "UTF-8"));
			writer.append(name);
			writer.append("\n" + ivent);
			writer.append("\n" + items);
			for (int i = 0; i < count; i++) {
				String dis = editTextList[i].getText().toString();

				writer.append("\n" + dis + "," + laptime[i] + "," + rlp[i]);
			}
			writer.close();
		} catch (IOException e) {

		}
	}

	public class browse implements OnClickListener {
		@Override
		public void onClick(View v) {
			Button button = (Button) v;

			File dir = new File("/data/data/com.example.manager/Runner/");
			final File[] directory = dir.listFiles();

			String name = button.getText().toString();
			if (name.equals("名前参照")) {

				final String[] items;
				try {
					items = new String[directory.length];
					for (int i = 0; i < directory.length; i++) {
						items[i] = directory[i].toString();
						items[i] = items[i].substring(38, items[i].length());
					}

					new AlertDialog.Builder(RunnerData.this)
							.setTitle("名前を選択してください")
							.setIcon(android.R.drawable.ic_dialog_info)
							.setSingleChoiceItems(items, -1, // デフォルトの選択
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											runner.setText(items[which]);
										}
									}).setPositiveButton("決定", null).show();
				} catch (Exception e) {
					error("データがないので名前を参照できません");
				}
			} else if (name.equals("イベント名参照")) {
				int count = 0;
				try {
					File[] fileList;
					for (int i = 0; i < directory.length; i++) {
						fileList = directory[i].listFiles();
						count += fileList.length;
					}
					final String[] items = new String[count];
					int current = 0;
					for (int i = 0; i < directory.length; i++) {
						File files = new File(directory[i].toString());
						final File[] file = files.listFiles();
						for (int j = 0; j < file.length; j++) {
							items[current] = file[j].toString();
							items[current] = items[current].substring(
									directory[i].toString().length() + 1,
									items[current].length() - 4);
							current++;
						}
					}
					new AlertDialog.Builder(RunnerData.this)
							.setTitle("イベント名を選択してください")
							.setSingleChoiceItems(items, -1,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											iventName.setText(items[which]);
										}
									}).setPositiveButton("決定", null).show();
				} catch (Exception e) {
					error("データがないのでイベント名を参照できません");
				}
			} else {
				final String[] items = { "100", "200", "400", "600", "800",
						"1000", "1500", "2000", "3000", "5000" };
				new AlertDialog.Builder(RunnerData.this)
						.setTitle("名前を選択してください")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setSingleChoiceItems(items, -1, // デフォルトの選択
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										item.setText(items[which]);
									}
								}).setPositiveButton("決定", null).show();

			}
		}
	}

	public class deleteData implements View.OnClickListener {
		int deleteNum;
		String[] newLaptime = new String[count];
		String[] newRlp = new String[count];

		public deleteData(int i) {
			deleteNum = i;
		}

		@Override
		public void onClick(View v) {
			new AlertDialog.Builder(RunnerData.this)
					.setTitle("Caution!!")
					.setMessage("本当にこのデータを削除してもいいですか？")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton("決定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (deleteNum == count - 1) {
										for (int j = 0; j < count - 1; j++) {
											newLaptime[j] = laptime[j];
											newRlp[j] = rlp[j];
										}
									} else {
										int current = 0;
										for (int j = 0; j < count; j++) {
											if (j == deleteNum) {
												newLaptime[current] = laptime[j + 1];
												newRlp[current] = rlp[j + 1];
												current++;
												j++;
											} else {
												newLaptime[current] = laptime[j];
												newRlp[current] = rlp[j];
												current++;
											}
										}
									}
									Intent intent = new Intent(RunnerData.this,
											RunnerData.class);
									String itemName = item.getText().toString();
									num = "Register";
									intent.putExtra("laptime", newLaptime);
									intent.putExtra("rlp", newRlp);
									intent.putExtra("name", name);
									intent.putExtra("runNum", num);
									intent.putExtra("distant", distantOfLap);
									intent.putExtra("fileName",
											previousFileName);
									intent.putExtra("iventName", ivent);
									intent.putExtra("item", itemName);
									intent.putExtra("mode", "SPLIT");
									intent.putExtra("state", "second");

									finish();
									startActivity(intent);

								}
							}).setNegativeButton("キャンセル", null).show();
		}
	}

	public class modeChange implements OnClickListener {
		@Override
		public void onClick(View v) {
			CheckBox mode = (CheckBox) v;
			TextView text = (TextView) findViewById(R.id.TextView01);
			boolean checked = mode.isChecked();
			itemName = item.getText().toString();
			Intent intent = new Intent(RunnerData.this, RunnerData.class);
			intent.putExtra("laptime", laptime);
			intent.putExtra("rlp", rlp);
			intent.putExtra("name", name);
			intent.putExtra("distant", distantOfLap);
			intent.putExtra("fileName", previousFileName);
			intent.putExtra("iventName", ivent);
			intent.putExtra("item", itemName);
			intent.putExtra("state", "second");

			if (text.getText().toString().equals("現在 : SPLIT")) {
				intent.putExtra("mode", "現在 : RLP");
				text.setText("現在 : RLP");
				if (num.equals("Complication")) {
					Log.d("test", "ok");
					intent.putExtra("runNum", "Complication");
				} else {
					intent.putExtra("runNum", "Resister");
				}
				finish();
				startActivity(intent);
			} else {
				intent.putExtra("mode", "現在 : SPLIT");
				text.setText("現在 : SPLIT");
				if (num.equals("Complication")) {
					Log.d("test", "ok");
					intent.putExtra("runNum", "Complication");
				} else {
					intent.putExtra("runNum", "Resister");
				}

				finish();
				startActivity(intent);
			}
		}
	}

	public void error(String message) {
		new AlertDialog.Builder(RunnerData.this)
				.setTitle("Caution!")
				.setMessage(message)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("決定", null).show();
	}
	protected void showMessage(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}


}