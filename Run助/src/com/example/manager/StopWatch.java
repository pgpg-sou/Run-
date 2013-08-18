package com.example.manager;

import java.io.File;

import com.example.manager.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StopWatch extends Activity implements OnClickListener {
	private int TIMERMODE = 0;
	private boolean TIMERSTATE;
	private boolean stateButton;
	int[] count = new int[3];
	String[] runnerName = { "走者1", "走者2", "走者3" };
	TextView timer_m, timer_s, timer_ms;
	int minute, second, m_second;
	int i = 0;
	String[] laptime = new String[50];
	String[] laptime2 = new String[50];
	String[] laptime3 = new String[50];
	String[] rlp1 = new String[50];
	String[] rlp2 = new String[50];
	String[] rlp3 = new String[50];
	Button lap_1;
	Button lap_2;
	Button lap_3;
	private TimeMesure tm = new TimeMesure();
	private long startDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.stopwatch);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.stopwatch_mode);

		Button start = (Button) findViewById(R.id.start);
		CheckBox mode = (CheckBox) findViewById(R.id.checkBox1);
		lap_1 = (Button) findViewById(R.id.Lap1);
		lap_2 = (Button) findViewById(R.id.Lap2);
		lap_3 = (Button) findViewById(R.id.Lap3);
		timer_m = (TextView) findViewById(R.id.Minute);
		timer_s = (TextView) findViewById(R.id.second);
		timer_ms = (TextView) findViewById(R.id.msecond);

		mode.setOnClickListener(new change());
		lap_1.setText("事前登録");
		lap_2.setText("Reset");
		lap_3.setText("save");
		start.setOnClickListener(this);
		lap_1.setOnClickListener(this);
		lap_2.setOnClickListener(this);
		lap_3.setOnClickListener(this);
	}

	@SuppressLint("NewApi")
	public void onClick(View v) {
		laptime lt = new laptime();
		laptime lt2 = new laptime();
		laptime lt3 = new laptime();
		Button button = (Button) v;

		Log.d("button_id", String.valueOf(button.getId()));
		switch (button.getId()) {
		case R.id.start:
			if (stateButton) {
				tm.stop();
				lap_1.setText("事前登録");
				lap_2.setText("Reset");
				lap_3.setText("save");
				button.setText("Start");
				stateButton = false;
				TIMERSTATE = false;
			} else {
				button.setText("Stop");
				lap_1.setText("Lap");
				lap_2.setText("Lap");
				lap_3.setText("Lap");
				laptime[0] = "null";
				laptime2[0] = "null";
				laptime3[0] = "null";
				startDate = System.currentTimeMillis();
				stateButton = true;
				TIMERSTATE = true;
				tm.start();
			}
			break;
		case R.id.Lap1:
			if (TIMERSTATE) {

				TransTools ti = new TransTools();
				TextView lap1 = (TextView) findViewById(R.id.lap1);
				lap1.setMovementMethod(ScrollingMovementMethod.getInstance());

				laptime[count[0]] = lt.lapTimeDisplay(minute, second, m_second);
				lap1.setText(runnerName[0]);
				double[] lap = ti.transSecond(laptime);
				double rlp;
				for (int j = 0; laptime[j] != null; j++) {
					if (j == 0) {
						rlp = lap[j];
						rlp1[j] = ti.transMinute(rlp);
						Log.d("check", rlp1[j]);
					} else {
						rlp = lap[j] - lap[j - 1];
						rlp1[j] = ti.transMinute(rlp);
						Log.d("check", rlp1[j]);
					}
				}

				if (TIMERMODE == 0) {
					for (int j = count[0]; j >= 0; j--)
						lap1.append("\n" + laptime[j]);
				} else {
					lap1.setText(runnerName[0]);
					for (int j = count[0]; j >= 0; j--)
						lap1.append("\n" + rlp1[j]);
				}
				count[0]++;

			} else {
				final TextView lap1 = (TextView) findViewById(R.id.lap1);
				final TextView lap2 = (TextView) findViewById(R.id.lap2);
				final TextView lap3 = (TextView) findViewById(R.id.lap3);
				LinearLayout outline = new LinearLayout(this);
				LinearLayout[] part = new LinearLayout[3];
				outline.setOrientation(outline.VERTICAL);

				final Button[] name = new Button[3];
				final TextView[] runner = new TextView[3];
				final EditText[] editView = new EditText[3];

				for (int i = 0; i < 3; i++) {
					final int num = i;
					part[i] = new LinearLayout(this);
					runner[i] = new TextView(StopWatch.this);
					editView[i] = new EditText(StopWatch.this);
					editView[i].setWidth(400);
					name[i] = new Button(StopWatch.this);
					name[i].setText("名前参照");
					runner[i].setText("走者" + String.valueOf(i));
					part[i].addView(runner[i]);
					part[i].addView(editView[i]);
					part[i].addView(name[i]);
					outline.addView(part[i]);
					name[i].setOnClickListener(new OnClickListener() {
						int current = num;

						public void onClick(View v) {
							Button button = (Button) v;
							try {
								File dir = new File(
										"/data/data/com.example.manager/Runner/");
								final File[] directory = dir.listFiles();
								final String[] items = new String[directory.length];
								Log.d("button_id",
										String.valueOf(button.getId()));
								for (int j = 0; j < directory.length; j++) {
									items[j] = directory[j].toString();
									items[j] = items[j].substring(38,
											items[j].length());
								}
								new AlertDialog.Builder(StopWatch.this)
										.setTitle("名前を選択してください")
										.setIcon(
												android.R.drawable.ic_dialog_info)
										.setSingleChoiceItems(items,
												-1, // デフォルトの選択
												new DialogInterface.OnClickListener() {
													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														editView[current]
																.setText(items[which]);
													}
												})
										.setPositiveButton("決定", null).show();
							} catch (Exception e) {
								error("データがないので名前の参照ができません");
							}
						}

					});
				}

				new AlertDialog.Builder(StopWatch.this)
						.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("それぞれの名前を入力して下さい")
						.setView(outline)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										for (int i = 0; i < 3; i++) {
											if (editView[i].getText()
													.toString().equals("")) {
												runnerName[i] = "走者"
														+ String.valueOf(i + 1);
											} else {
												runnerName[i] = editView[i]
														.getText().toString();
											}
										}
										lap1.setText(runnerName[0]);
										lap2.setText(runnerName[1]);
										lap3.setText(runnerName[2]);
									}
								})
						.setNegativeButton("キャンセル",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
									}
								}).show();
			}
			break;
		case R.id.Lap2:
			if (TIMERSTATE) {
				TransTools ti = new TransTools();
				TextView lap2 = (TextView) findViewById(R.id.lap2);
				lap2.setMovementMethod(ScrollingMovementMethod.getInstance());

				laptime2[count[1]] = lt2.lapTimeDisplay(minute, second,
						m_second);
				lap2.setText(runnerName[1]);

				double[] lap = ti.transSecond(laptime2);
				double rlp;
				for (int j = 0; laptime2[j] != null; j++) {
					if (j == 0) {
						rlp = lap[j];
						rlp2[j] = ti.transMinute(rlp);
					} else {
						rlp = lap[j] - lap[j - 1];
						rlp2[j] = ti.transMinute(rlp);
					}
				}

				if (TIMERMODE == 0) {
					for (int j = count[1]; j >= 0; j--)
						lap2.append("\n" + laptime2[j]);

				} else {
					lap2.setText(runnerName[1]);
					for (int j = count[1]; j >= 0; j--)
						lap2.append("\n" + rlp2[j]);
				}
				count[1]++;

			} else {
				TextView lap1 = (TextView) findViewById(R.id.lap1);
				TextView lap2 = (TextView) findViewById(R.id.lap2);
				TextView lap3 = (TextView) findViewById(R.id.lap3);

				lap1.setText(runnerName[0]);
				lap2.setText(runnerName[1]);
				lap3.setText(runnerName[2]);
				timer_m.setText("00");
				timer_s.setText("00");
				timer_ms.setText("00");
				count[0] = 0;
				count[1] = 0;
				count[2] = 0;
				for (int i = 0; i < laptime.length; i++) {
					laptime[i] = null;
					laptime2[i] = null;
					laptime3[i] = null;
				}
			}
			break;
		case R.id.Lap3:
			if (TIMERSTATE) {
				double rlp;
				TransTools ti = new TransTools();
				TextView lap3 = (TextView) findViewById(R.id.lap3);
				lap3.setMovementMethod(ScrollingMovementMethod.getInstance());

				laptime3[count[2]] = lt3.lapTimeDisplay(minute, second,
						m_second);
				lap3.setText(runnerName[2]);
				double[] lap = ti.transSecond(laptime3);

				for (int j = 0; laptime3[j] != null; j++) {
					if (j == 0) {
						rlp = lap[j];
						rlp3[j] = ti.transMinute(rlp);
					} else {
						rlp = lap[j] - lap[j - 1];
						rlp3[j] = ti.transMinute(rlp);
					}
					Log.d("rlp3", rlp3[j]);
				}

				if (TIMERMODE == 0) {
					for (int j = count[2]; j >= 0; j--)
						lap3.append("\n" + laptime3[j]);
				} else {
					lap3.setText(runnerName[1]);
					for (int j = count[2]; j >= 0; j--)
						lap3.append("\n" + rlp3[j]);
				}
				count[2]++;

			} else {
				for (int i = 0; i < 3; i++) {
					if (count[i] == 0)
						count[i]++;
				}
				new AlertDialog.Builder(this)
						.setTitle("データ登録しますか？")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										Intent intent = new Intent(
												StopWatch.this,
												RegisterData.class);
										String[] sumTime = new String[3];

										sumTime[0] = laptime[count[0] - 1];
										sumTime[1] = laptime2[count[1] - 1];
										sumTime[2] = laptime3[count[2] - 1];
										Log.d("sumtime1", sumTime[0]);
										Log.d("sumtime2", sumTime[1]);
										Log.d("sumtime3", sumTime[2]);
										intent.putExtra("name", runnerName);
										intent.putExtra("Runner", laptime);
										intent.putExtra("Runner2", laptime2);
										intent.putExtra("Runner3", laptime3);
										intent.putExtra("rlpOfRunner1", rlp1);
										intent.putExtra("rlpOfRunner2", rlp2);
										intent.putExtra("rlpOfRunner3", rlp3);
										intent.putExtra("sumTime", sumTime);
										startActivity(intent);
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {

									}
								}).show();

			}
			break;
		}
	}

	public class change implements OnClickListener {
		@Override
		public void onClick(View v) {
			CheckBox checkBox = (CheckBox) v;
			TextView text = (TextView) findViewById(R.id.TextView01);
			// チェックボックスのチェック状態を取得します
			boolean checked = checkBox.isChecked();
			if (checked) {
				text.setText("現在 : RLP");
				TIMERMODE = 1;
			} else {
				text.setText("現在 : SPLIT");
				TIMERMODE = 0;
			}
		}
	}

	public void update() {
		minute = (int) ((((System.currentTimeMillis() - startDate)) / 1000) / 60);
		second = (int) ((((System.currentTimeMillis() - startDate)) / 1000) % 60);
		m_second = (int) (((System.currentTimeMillis() - startDate) / 10) % 100);
		timer_m.setText(String.format("%1$02d", minute));
		timer_s.setText(String.format("%1$02d", second));
		timer_ms.setText(String.format("%1$02d", m_second));
	}

	class TimeMesure extends Handler {
		private boolean isUpdate;

		public void start() {
			this.isUpdate = true;
			handleMessage(new Message());
		}

		public void stop() {
			this.isUpdate = false;
		}

		@Override
		public void handleMessage(Message msg) {

			this.removeMessages(0);// 既存のメッセージは削除
			if (this.isUpdate) {
				StopWatch.this.update();// 自信が発したメッセージを取得してupdateを実行
				sendMessageDelayed(obtainMessage(0), 10);// 1ミリ秒後にメッセージを出力
			}
		}
	};
	public void error(String message) {
		new AlertDialog.Builder(StopWatch.this)
				.setTitle("Caution!")
				.setMessage(message)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("決定", null).show();
	}

}
