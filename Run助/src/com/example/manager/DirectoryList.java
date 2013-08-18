package com.example.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DirectoryList extends Activity {
	File dir = new File("/data/data/com.example.manager/Runner/");
	File[] directory = dir.listFiles();
	String[] directoryName;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.directory_list);

		try{ 
			directoryName = new String[directory.length];			
			ListView list = (ListView) this.findViewById(R.id.listView1);
			List<String> items = new ArrayList<String>();
			final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			for (int i = 0; i < directory.length; i++) {
				directoryName[i] = directory[i].getName();
				Log.d("filepath", directory[i].getPath());
				items.add(directoryName[i]);
			}

			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					R.layout.flist,
					R.id.row_textview1,items);
			list.setAdapter(adapter);
			list.setOnItemClickListener(new MyClickAdapter());

			list.setOnItemLongClickListener(new OnItemLongClickListener() {
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						final int position, long id) {
					alertDialogBuilder.setTitle("Caution!");
					alertDialogBuilder.setMessage("ディレクトリを削除しますか？");

					alertDialogBuilder.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									delete(directory[position]);
									Intent intent = new Intent(DirectoryList.this,
											DirectoryList.class);
									startActivity(intent);
									finish();
								}
							});
					alertDialogBuilder.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});

					alertDialogBuilder.show();
					return true;
				}
			});

		} catch(Exception e) {
			new AlertDialog.Builder(DirectoryList.this) 
				.setTitle("Caution!")
				.setMessage("データがありません")
				.setPositiveButton("ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).show();
		}

	}

	class MyClickAdapter implements OnItemClickListener {
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long id) {
			Intent intent = new Intent(DirectoryList.this, FileList.class);
			intent.putExtra("directoryName", directoryName[position]);
			startActivity(intent);
		}
	}

	static private void delete(File f) {
		if (f.exists() == false) 
			return;
		
		if (f.isFile()) 
			f.delete();
		
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			for (int i = 0; i < files.length; i++) {
				delete(files[i]);
			}
			f.delete();
		}
	}

	protected void showMessage(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}
