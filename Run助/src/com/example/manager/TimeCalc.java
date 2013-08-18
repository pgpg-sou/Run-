package com.example.manager;

import com.example.manager.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TimeCalc extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.timecalc);

		Intent intent = getIntent();
		Button button = (Button) findViewById(R.id.Calc);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// EditTextの定義
				EditText editDistance = (EditText) findViewById(R.id.distance);
				EditText editHour = (EditText) findViewById(R.id.hour);
				EditText editMinute = (EditText) findViewById(R.id.minute);
				EditText editSecond = (EditText) findViewById(R.id.second);
				// EditTextの範囲選択
				editDistance.selectAll();
				editHour.selectAll();
				editMinute.selectAll();
				editSecond.selectAll();
				// テキストを数字に直し、所得する
				int distance = Integer.parseInt(editDistance.getText()
						.toString());
				int hour = Integer.parseInt(editHour.getText().toString());
				int minute = Integer.parseInt(editMinute.getText().toString());
				int second = Integer.parseInt(editSecond.getText().toString());

				double sumTime = hour * 360 + minute * 60 + second;
				double baseTime = sumTime / (distance / 100);

				double M100 = baseTime;
				double M200 = baseTime * 2;
				double M400 = baseTime * 4;
				double M1000 = baseTime * 10;

				TextView M1 = (TextView) findViewById(R.id.M100);
				TextView M2 = (TextView) findViewById(R.id.M200);
				TextView M4 = (TextView) findViewById(R.id.M400);
				TextView M10 = (TextView) findViewById(R.id.M1000);

				M1.setText("    100m : " + baseTime + " [s]");
				M2.setText("    200m : " + M200 + "[s]");
				M4.setText("    400m : " + M400 + "[s]");
				M10.setText("      1000m :" + M1000 + "[s]");

				showMessage("Click");
			}
		});
	}

	protected void showMessage(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}
