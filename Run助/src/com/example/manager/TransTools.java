package com.example.manager;

import android.util.Log;

public class TransTools {
	public double[] transSecond(String[] laptime) {
		double[] lap = new double[laptime.length];
		int count = 0;

		while (laptime[count] != null)
			count++;
		for (int i = 0; i < count; i++) {
			double sumTime;
			String[] division;
			if (laptime[i].indexOf("'") != -1) {
				division = laptime[i].split("'");
				Log.d("test", division[0]);
				Log.d("test",division[1]);
				String[] divi = division[1].split("\"");
				Log.d("test",divi[0]);
				Log.d("test",divi[1]);

				sumTime = Integer.parseInt(division[0]) * 60
						+ Integer.parseInt(divi[0]) + (Double.parseDouble(divi[1])/100);
				Log.d("test",String.valueOf(sumTime));
				lap[i] = sumTime;
			} else {
				division = laptime[i].split("\"");
				sumTime = Double.parseDouble(division[0])
						+ Double.parseDouble(division[1]) / 100;
				lap[i] = sumTime;
			}
		}

		return lap;
	}

	public String transMinute(double secondTime) {
		String time;
		String comSecond = null;
		String newSecond = null;
		int minute = (int) secondTime / 60;
		int second = 0;
		double mSecond = 0;
		
		secondTime = secondTime - minute*60;
		Log.d("transtool", String.valueOf(secondTime));

		for (int i = 0; i < 60; i++) {
			if ((secondTime - i) < 1) {
				if(minute > 0 && i < 10){
					newSecond = "0" + String.valueOf(i);
				} else {
					newSecond = String.valueOf(i);
				}
				mSecond = (secondTime - (double)i)*100;
				if(mSecond < 10){
					comSecond = "0" + String.valueOf((int)mSecond);
				} else {
					comSecond = String.valueOf((int)mSecond);
				}
				break;
			}
		}
		if (minute == 0) {
			time = newSecond + "\"" + comSecond;
		} else {
			time = String.valueOf(minute) + "'" + newSecond + "\""
					+ comSecond;
		}

		return time;
	}

	public String transDistance(double time) {
		if (time < 4.00) {
			return "30";
		} else if (time >= 4.00 && time <= 11.0) {
			return "50";
		} else if (time > 11.0 && time <= 23.0) {
			return "100";
		} else if (time > 23.0 && time <= 48.0) {
			return "200";
		} else if (time > 48.0 && time <= 110.0) {
			return "400";
		} else if (time > 110.0 && time <= 170.0) {
			return "600";
		} else if (time > 170.0 && time <= 270.0) {
			return "1000";
		} else if (time > 270.0 && time <= 540.0) {
			return "2000";
		}
		return "3000";
	}

}
