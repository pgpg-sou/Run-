package com.example.manager;

import android.app.Activity;



public class laptime extends Activity {
	int count = 0;

	public String lapTimeDisplay(int minute, int second, int m_second) {
		String laptime;
		String comSecond;
		if(m_second < 10) {
			comSecond = "0" + String.valueOf(m_second);
		} else {
			comSecond = String.valueOf(m_second);
		}
		if (minute == 0) {
			laptime = String.valueOf(second) + "\""
					+ comSecond;
		} else if(minute != 0 && second < 10){
			laptime = String.valueOf(minute) + "'" + "0"
					+ String.valueOf(second) + "\""
					+ comSecond;
		} else {
			laptime = String.valueOf(minute) + "'"
					+ String.valueOf(second) + "\""
					+ comSecond;
		}
		count++;
		return laptime;
	}
}
