package com.example.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

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
	EditText mRunner;
	EditText mEventName;
	EditText mItem;
	EditText[] mEditTextList;
	TextView[] mLap;
	TextView[] mmeter;
	Button mNameReference;
	Button mEventReference;
	Button mItemReference;
	ImageView[] mImage;
	String[] mLaptime;
	String[] mrlp;
	String[] mDistantOfLap;
	GUI mSetupGui = new GUI();
	TransTools mTransTools = new TransTools();
	String MODE;
	String mTimerMode;
	String mName;
	String mEvent;
	String mItemName;
	String mPreviousFileName;
	int mCount = 0;
	String mComplicationMode;

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

		mNameReference = new Button(this);
		mEventReference = new Button(this);
		mItemReference = new Button(this);
		mode.setOnClickListener(new modeChange());
		register.setOnClickListener(this);
		cancel.setOnClickListener(this);
		mEventReference.setOnClickListener(new browse());
		mNameReference.setOnClickListener(new browse());
		mItemReference.setOnClickListener(new browse());
		mEventReference.setText(getResources().getString(R.string.event_name));
		mNameReference.setText(getResources().getString(R.string.name));
		mItemReference
				.setText(getResources().getString(R.string.distance_name));
		mLaptime = intent.getStringArrayExtra("laptime");
		mrlp = intent.getStringArrayExtra("rlp");
		mName = intent.getStringExtra("name");
		mComplicationMode = intent.getStringExtra("runNum");
		mDistantOfLap = intent.getStringArrayExtra("distant");
		mPreviousFileName = intent.getStringExtra("fileName");
		mEvent = intent.getStringExtra("iventName");
		mItemName = intent.getStringExtra("item");
		mTimerMode = intent.getStringExtra("mode");
		String state = intent.getStringExtra("state");

		if (mComplicationMode.equals(getResources().getString(
				R.string.complication))
				|| state.equals(getResources().getString(R.string.second))) {
			text.setText(mTimerMode);
			if (mTimerMode.equals(getResources().getString(R.string.timerMode))) {
				mode.setText(getResources().getString(R.string.modeSplit));
				displayView(mrlp, mItemName);
			} else {
				mode.setText(getResources().getString(R.string.modeRlp));
				displayView(mLaptime, mItemName);
			}

		} else {
			final String[] items = { "100", "200", "400", "600", "800", "1000",
					"1500", "2000", "3000", "5000" };
			new AlertDialog.Builder(RunnerData.this)
					.setTitle(getResources().getString(R.string.selectItem))
					.setIcon(android.R.drawable.ic_dialog_info)
					.setItems(items, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							text.setText(mTimerMode);
							if (mTimerMode.equals(getResources().getString(
									R.string.timerMode))) {
								mode.setText(getResources().getString(
										R.string.modeSplit));
								displayView(mrlp, items[which]);
							} else {
								mode.setText(getResources().getString(
										R.string.modeRlp));
								displayView(mLaptime, items[which]);
							}
						}
					}).show();
		}
	}

	@SuppressWarnings("static-access")
	public void displayView(String[] time, String itemName) {
		int distance = 0;
		@SuppressWarnings("unused")
		double[] sumSecond;
		LinearLayout overall = new LinearLayout(this);
		LinearLayout pairList = new LinearLayout(this);
		LinearLayout pairName = new LinearLayout(this);
		LinearLayout pairIvent = new LinearLayout(this);
		LinearLayout pairItem = new LinearLayout(this);
		ScrollView scr_view = (ScrollView) findViewById(R.id.scr_view);

		if (mComplicationMode.equals(getResources().getString(
				R.string.complication))) {
			MODE = getResources().getString(R.string.complication);
			sumSecond = new double[10];
		} else {
			MODE = getResources().getString(R.string.Register);
			sumSecond = mTransTools.transSecond(time);
		}
		while (time[mCount] != null)
			mCount++;
		overall.setOrientation(overall.VERTICAL);
		pairList.setOrientation(pairList.VERTICAL);

		LinearLayout[] distanceAndTime = new LinearLayout[mCount];
		TextView namelabel = new TextView(this);
		TextView iventlabel = new TextView(this);
		TextView distanceLabel = new TextView(this);
		mEditTextList = new EditText[mCount];
		mImage = new ImageView[mCount];
		mRunner = new EditText(this);
		mEventName = new EditText(this);
		mItem = new EditText(this);
		mLap = new TextView[mCount];
		mmeter = new TextView[mCount];

		mSetupGui.setupTextView(iventlabel, 140, 50,
				getResources().getString(R.string.eventLabel));
		mSetupGui.setupTextView(namelabel, 150, 50,
				getResources().getString(R.string.runnerLabel));
		mSetupGui.setupTextView(distanceLabel, 120, 50, getResources()
				.getString(R.string.distanceLabel));
		mRunner = mSetupGui.setUpEditText(mRunner, 300, 120, mName);
		mEventName = mSetupGui.setUpEditText(mEventName, 330, 120, mEvent);
		mItem = mSetupGui.setUpEditText(mItem, 200, 80, itemName);

		pairName = mSetupGui.setupLinearLayout(pairName, namelabel, mRunner,
				mNameReference);
		pairIvent = mSetupGui.setupLinearLayout(pairIvent, iventlabel,
				mEventName, mEventReference);
		pairItem = mSetupGui.setupLinearLayout(pairItem, distanceLabel, mItem,
				mItemReference);
		overall.addView(pairName);
		overall.addView(pairIvent);
		overall.addView(pairItem);
		overall.addView(pairList);

		for (int i = 0; i < mCount; i++) {
			distanceAndTime[i] = new LinearLayout(this);
			mLap[i] = new TextView(this);
			mLap[i] = mSetupGui.setupTextView(mLap[i], time[i], 35);
			mmeter[i] = new TextView(this);
			mmeter[i] = mSetupGui.setupTextView(mmeter[i], 80, 120, getResources().getString(R.string.meter));
			mEditTextList[i] = new EditText(this);
			mImage[i] = new ImageView(this);

			TextView lapview = new TextView(this);
			String Lap = "Lap";
			Resources r = getResources();
			Bitmap bmp = BitmapFactory.decodeResource(r,
					R.drawable.ic_action_delete);
			mImage[i].setImageBitmap(bmp);
			mImage[i].setScaleType(ImageView.ScaleType.CENTER);
			mImage[i].setOnClickListener(new deleteData(i));
			if (MODE.equals(getResources().getString(R.string.complication))) {
				String lapIndex = Lap + String.valueOf(i + 1);
				mSetupGui.setupTextView(lapview, lapIndex, 15);
				mEditTextList[i] = mSetupGui.setUpEditText(mEditTextList[i],
						200, 80, mDistantOfLap[i]);
				distanceAndTime[i] = mSetupGui.setupLinearLayout(
						distanceAndTime[i], lapview, mEditTextList[i],
						mmeter[i]);
				distanceAndTime[i].addView(mLap[i]);
				distanceAndTime[i].addView(mImage[i]);
				pairList.addView(distanceAndTime[i]);
			} else {
				if (mTimerMode.equals(getResources().getString(
						R.string.timerMode))) {
					distance = Integer.parseInt(itemName) / mCount;
				} else {
					distance += Integer.parseInt(itemName) / mCount;
				}
				String lapIndex = Lap + String.valueOf(i + 1);
				mSetupGui.setupTextView(lapview, 120, 80, lapIndex);

				mEditTextList[i] = mSetupGui.setUpEditText(mEditTextList[i],
						200, 80, String.valueOf(distance));

				distanceAndTime[i] = mSetupGui.setupLinearLayout(
						distanceAndTime[i], lapview, mEditTextList[i],
						mmeter[i]);
				distanceAndTime[i].addView(mLap[i]);
				distanceAndTime[i].addView(mImage[i]);
				pairList.addView(distanceAndTime[i]);
			}
		}
		scr_view.addView(overall);

	}

	public void onClick(View v) {
		Button button = (Button) v;

		switch (button.getId()) {
		case R.id.register:
			if (mEventName.getText().toString().equals("")
					|| mRunner.getText().toString().equals("")) {
				new AlertDialog.Builder(RunnerData.this)
						.setTitle(
								getResources()
										.getString(R.string.registerError))
						.setPositiveButton(getResources().getString(R.string.decide), null).show();
			} else {
				String name = mRunner.getText().toString();
				File mainDir = new File(getResources().getString(
						R.string.basePath));
				File dirName = new File(getResources().getString(
						R.string.basePath)
						+ name + "/");
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

		try {
			String ivent = mEventName.getText().toString();
			String items = mItem.getText().toString();
			OutputStream runnerData;
			String path = getResources().getString(R.string.basePath);
			if (MODE.equals(getResources().getString(R.string.complication))) {
				mPreviousFileName = path + name + "/" + mPreviousFileName
						+ ".txt";
				Log.d("previousFileName", mPreviousFileName);
				runnerData = new FileOutputStream(mPreviousFileName);
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
			for (int i = 0; i < mCount; i++) {
				String dis = mEditTextList[i].getText().toString();

				writer.append("\n" + dis + "," + mLaptime[i] + "," + mrlp[i]);
			}
			writer.close();
		} catch (IOException e) {

		}
	}

	public class browse implements OnClickListener {
		@Override
		public void onClick(View v) {
			Button button = (Button) v;

			File dir = new File(getResources().getString(R.string.basePath));
			final File[] directory = dir.listFiles();

			String name = button.getText().toString();
			if (name.equals(getResources().getString(R.string.name))) {

				final String[] items;
				try {
					items = new String[directory.length];
					for (int i = 0; i < directory.length; i++) {
						items[i] = directory[i].toString();
						items[i] = items[i].substring(38, items[i].length());
					}

					new AlertDialog.Builder(RunnerData.this)
							.setTitle(
									getResources().getString(
											R.string.selecetName))
							.setIcon(android.R.drawable.ic_dialog_info)
							.setSingleChoiceItems(items, -1, // デフォルトの選択
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											mRunner.setText(items[which]);
										}
									}).setPositiveButton(getResources().getString(R.string.decide), null).show();
				} catch (Exception e) {
					error("データがないので名前を参照できません");
				}
			} else if (name.equals(getResources()
					.getString(R.string.event_name))) {
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
							.setTitle(
									getResources().getString(
											R.string.eventSelectMessage))
							.setSingleChoiceItems(items, -1,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											mEventName.setText(items[which]);
										}
									}).setPositiveButton(getResources().getString(R.string.decide), null).show();
				} catch (Exception e) {
					error("データがないのでイベント名を参照できません");
				}
			} else {
				final String[] items = { "100", "200", "400", "600", "800",
						"1000", "1500", "2000", "3000", "5000" };
				new AlertDialog.Builder(RunnerData.this)
						.setTitle(
								getResources().getString(R.string.selecetName))
						.setIcon(android.R.drawable.ic_dialog_info)
						.setSingleChoiceItems(items, -1, // デフォルトの選択
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										mItem.setText(items[which]);
									}
								}).setPositiveButton(getResources().getString(R.string.decide), null).show();

			}
		}
	}

	public class deleteData implements View.OnClickListener {
		int deleteNum;
		String[] newLaptime = new String[mCount];
		String[] newRlp = new String[mCount];

		public deleteData(int i) {
			deleteNum = i;
		}

		@Override
		public void onClick(View v) {
			new AlertDialog.Builder(RunnerData.this)
					.setTitle(getResources().getString(R.string.caution))
					.setMessage(
							getResources().getString(R.string.deleteCaution))
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton(getResources().getString(R.string.decide),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (deleteNum == mCount - 1) {
										for (int j = 0; j < mCount - 1; j++) {
											newLaptime[j] = mLaptime[j];
											newRlp[j] = mrlp[j];
										}
									} else {
										int current = 0;
										for (int j = 0; j < mCount; j++) {
											if (j == deleteNum) {
												newLaptime[current] = mLaptime[j + 1];
												newRlp[current] = mrlp[j + 1];
												current++;
												j++;
											} else {
												newLaptime[current] = mLaptime[j];
												newRlp[current] = mrlp[j];
												current++;
											}
										}
									}
									Intent intent = new Intent(RunnerData.this,
											RunnerData.class);
									String itemName = mItem.getText()
											.toString();
									mComplicationMode = getResources()
											.getString(R.string.Register);
									intent.putExtra("laptime", newLaptime);
									intent.putExtra("rlp", newRlp);
									intent.putExtra("name", mName);
									intent.putExtra("runNum", mComplicationMode);
									intent.putExtra("distant", mDistantOfLap);
									intent.putExtra("fileName",
											mPreviousFileName);
									intent.putExtra("iventName", mEvent);
									intent.putExtra("item", itemName);
									intent.putExtra("mode", getResources()
											.getString(R.string.modeSplit));
									intent.putExtra("state", getResources()
											.getString(R.string.second));

									finish();
									startActivity(intent);

								}
							}).setNegativeButton(getResources().getString(R.string.cancel), null).show();
		}
	}

	public class modeChange implements OnClickListener {
		@Override
		public void onClick(View v) {
			TextView text = (TextView) findViewById(R.id.TextView01);
			mItemName = mItem.getText().toString();
			Intent intent = new Intent(RunnerData.this, RunnerData.class);
			intent.putExtra("laptime", mLaptime);
			intent.putExtra("rlp", mrlp);
			intent.putExtra("name", mName);
			intent.putExtra("distant", mDistantOfLap);
			intent.putExtra("fileName", mPreviousFileName);
			intent.putExtra("iventName", mEvent);
			intent.putExtra("item", mItemName);
			intent.putExtra("state", getResources()
					.getString(R.string.Register));

			if (text.getText().toString()
					.equals(getResources().getString(R.string.timerMode))) {
				intent.putExtra("mode",
						getResources().getString(R.string.timerMode));
				text.setText(getResources().getString(R.string.timerMode));
				if (mComplicationMode.equals(getResources().getString(
						R.string.complication))) {
					intent.putExtra("runNum",
							getResources().getString(R.string.complication));
				} else {
					intent.putExtra("runNum",
							getResources().getString(R.string.Register));
				}
				finish();
				startActivity(intent);
			} else {
				intent.putExtra("mode",
						getResources().getString(R.string.currenSPLIT));
				text.setText(getResources().getString(R.string.currenSPLIT));
				if (mComplicationMode.equals(getResources().getString(
						R.string.complication))) {
					intent.putExtra("runNum",
							getResources().getString(R.string.complication));
				} else {
					intent.putExtra("runNum",
							getResources().getString(R.string.Register));
				}

				finish();
				startActivity(intent);
			}
		}
	}

	public void error(String message) {
		new AlertDialog.Builder(RunnerData.this)
				.setTitle(getResources().getString(R.string.caution))
				.setMessage(message).setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton(getResources().getString(R.string.decide), null).show();
	}

	protected void showMessage(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

}