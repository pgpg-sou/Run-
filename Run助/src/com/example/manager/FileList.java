package com.example.manager;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FileList extends Activity {

	File[] files;
	String[] fileName;
	String dirPath;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_list);

		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
		Intent intent = getIntent();
		final String directoryName = intent.getStringExtra("directoryName");

		dirPath = "/data/data/com.example.manager/Runner/" + directoryName
				+ "/";
		File dir = new File(dirPath);
		files = dir.listFiles();
		fileName = new String[files.length];

		for (int i = 0; i < files.length; i++) {
			fileName[i] = files[i].getName();
			Log.d("filepath",files[i].getPath());
			fileName[i] = fileName[i].substring(0, fileName[i].length() - 4);
		}

		ListView list = (ListView) this.findViewById(R.id.listView1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.textview_list, R.id.text, fileName);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new MyClickAdapter());

		list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int position, long id) {

				alertDialogBuilder.setTitle("Caution!");
				alertDialogBuilder.setMessage("ファイルを削除しますか？");

				alertDialogBuilder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								files[position].delete();
								Intent intent = new Intent(FileList.this,
										DirectoryList.class);
								intent.putExtra("directoryName", directoryName);
								startActivity(intent);
								finish();
							}
						});
				alertDialogBuilder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				alertDialogBuilder.show();
				return true;
			}
		});
	}

	class MyClickAdapter implements OnItemClickListener {
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long id) {
			Intent intent = new Intent(FileList.this, SeeFile.class);
			intent.putExtra("data", fileName[position]);
			intent.putExtra("path", dirPath);
			intent.putExtra("mode", "現在 : SPLIT");
			startActivity(intent);
		}
	}

	protected void showMessage(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}