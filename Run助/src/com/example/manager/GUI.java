package com.example.manager;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GUI extends Activity {
	public TextView setupTextView(TextView lap, String laptime, int size) {
		lap.setWidth(240);
		lap.setHeight(120);
		lap.setTextSize(size);
		lap.setGravity(Gravity.RIGHT);
		lap.setText(laptime);

		return lap;
	}

	public TextView setupTextView(TextView lap, int width, int height, String text) {
		lap.setWidth(width);
		lap.setHeight(height);
		lap.setTextSize(22);
		lap.setText(text);

		return lap;
	}

	public EditText setUpEditText(EditText edit, int width, int height,
			String text) {
		edit.setWidth(width);
		edit.setHeight(height);
		edit.setTextSize(30);
		edit.setText(text);

		return edit;
	}
	public LinearLayout setupLinearLayout(LinearLayout layout,View edit,View meter,View lap) {
		layout.setOrientation(layout.HORIZONTAL);
		layout.addView(edit);
		layout.addView(meter);
		layout.addView(lap);
		
		return layout;
	}
	public LinearLayout setupLinearLayout(LinearLayout layout,View refer,View runner) {
		layout.setOrientation(layout.HORIZONTAL);
		layout.addView(runner);
		layout.addView(refer);
		
		return layout;
	}
}
