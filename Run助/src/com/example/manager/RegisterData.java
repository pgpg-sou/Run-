package com.example.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class RegisterData extends Activity {

	String[] runnerTime, runner2Time, runner3Time;
	String[] rlpOfRunner1, rlpOfRunner2, rlpOfRunner3;
	String[] run = { "走者1", "走者2", "走者3" };
	String item;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_data);
		Intent intent = getIntent();
		String[] sumTime = new String[3];
		ListView listView = (ListView) findViewById(R.id.listView);
		List<Map<String, String>> retDataList = new ArrayList<Map<String, String>>();

		run = intent.getStringArrayExtra("name");
		runnerTime = intent.getStringArrayExtra("Runner");
		runner2Time = intent.getStringArrayExtra("Runner2");
		runner3Time = intent.getStringArrayExtra("Runner3");
		rlpOfRunner1 = intent.getStringArrayExtra("rlpOfRunner1");
		rlpOfRunner2 = intent.getStringArrayExtra("rlpOfRunner2");
		rlpOfRunner3 = intent.getStringArrayExtra("rlpOfRunner3");
		sumTime = intent.getStringArrayExtra("sumTime");

		listView.setOnItemClickListener(new jump());
		for (int n = 0; n < 3; n++) {
			Map<String, String> data = new HashMap<String, String>();
			data.put("title", run[n]);
			data.put("comment", sumTime[n]);
			retDataList.add(data);
		}

		SimpleAdapter adapter2 = new SimpleAdapter(this, retDataList,
				R.layout.list, new String[] { "title", "comment" }, new int[] {
						R.id.textView1, R.id.textView2 });

		listView.setAdapter(adapter2);
	}

	class jump implements OnItemClickListener {
		private void setup(Intent run, String[] runnerTime, String[] rlp,
				String name, int num, String item) {
			String[] dis = new String[1];
			run.putExtra("laptime", runnerTime);
			run.putExtra("rlp", rlp);
			run.putExtra("name", name);
			run.putExtra("runNum", String.valueOf(num));
			run.putExtra("distant", dis);
			run.putExtra("fileName", "NULL");
			run.putExtra("mode", "現在 : SPLIT");
			run.putExtra("item", item);
			run.putExtra("state", "first");

			startActivity(run);
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Intent run1 = new Intent(RegisterData.this, RunnerData.class);
			final String[] items = { "100", "200", "400", "600", "800", "1000",
					"1500", "2000", "3000", "5000" };

			if (position == 0) {
				setup(run1, runnerTime, rlpOfRunner1, run[0], 0, item);
			} else if (position == 1) {
				setup(run1, runner2Time, rlpOfRunner2, run[1], 1, item);
			} else {
				setup(run1, runner3Time, rlpOfRunner3, run[2], 2, item);
			}

		}
	}
}