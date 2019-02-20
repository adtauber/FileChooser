package com.adtdev.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.io.File;

import com.adtdev.fileChooser.FileChooser;

public class MainActivity extends AppCompatActivity {

    public static final int LOCAL_FILES = 0;
    public static final int FTP_SELECT = 1;
    public static final int FTP_DOWNLOAD = 2;
    private static final int doLOCfile = 0;
    private static final int doFTPfile = 1;
    private static final int doFTPdnld = 2;
    private static final int REQUEST_WRITE_STORAGE = 0;

    private String ftpURL;
    private String ftpName;
    private String ftpPswd;

    public String name, startPath;
    public boolean selFolders;
    public boolean selFiles;
    private TextView msgFrame;

    private Button local;
    private Button ftpfile;
    private Button ftpDnld;
    private CheckBox fldrChkBox;
    private CheckBox fileChkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ftpURL = "debian.simnet.is";
        ftpName = "anonymous";
        ftpPswd = "anonymous";

//        ftpURL = "60.248.237.25";
//        ftpName = "tsi0001";
//        ftpPswd = "tweyet";

        setContentView(R.layout.activity_main);
        msgFrame = (TextView) findViewById(R.id.msgFrame);
        msgFrame.setText("click file or folder icon in file browser to select\n");
        fldrChkBox = (CheckBox) findViewById(R.id.fldrChkBox);
        fileChkBox = (CheckBox) findViewById(R.id.fileChkBox);

        fldrChkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selFolders = isChecked;
            }
        });

        fileChkBox.setChecked(true);
        selFiles = true;
        fileChkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selFiles = isChecked;
            }
        });

        local = (Button) findViewById(R.id.local);
        local.setTransformationMethod(null);
        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localSelect();
            }
        });

        ftpfile = (Button) findViewById(R.id.ftpfile);
        ftpfile.setTransformationMethod(null);
        ftpfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftpSelect();
            }
        });

        ftpDnld = (Button) findViewById(R.id.ftpDnld);
        ftpDnld.setTransformationMethod(null);
        ftpDnld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftpDownload();
            }
        });
        ftpDnld.setEnabled(false);
    }//onCreate()

    private void localSelect() {
        // has been tested on:
        //   ASUS_X00ID with Android 8.1.0
        //   Nexus 4 with Android 4.4.4
        //   Samsung SM-T530NU with Android 5.0.2
        //   Samsung SGH-I717D with unlocked Android 4.1.2

        // use root value to set top level for folders and start to establish starting directory
        // root value /storage provides access to removable storage
        // use start to establish starting directory
        // set root to start value if you need to restrict user navigation

        //environment method results on an ASUS_X00ID with Android 8.1.0
//        startPath = Environment.getExternalStorageDirectory().toString(); //points to /storage/emulated/0
//        startPath = Environment.getDataDirectory().toString();  //points to /data = not readable
//        startPath = Environment.getDownloadCacheDirectory().toString();  //points to /data/cache
        startPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        Intent intent = new Intent(this, FileChooser.class);
        intent.putExtra("method", doLOCfile);
        intent.putExtra("root", "/storage");
        intent.putExtra("start", startPath);
        intent.putExtra("selfolders", selFolders);
        intent.putExtra("selfiles", selFiles);
        intent.putExtra("showhidden", false);
        startActivityForResult(intent, LOCAL_FILES);
    }//localSelect()

    public void ftpSelect() {
        Intent intent = new Intent(this, FileChooser.class);
        intent.putExtra("method", doFTPfile);
        intent.putExtra("ftpURL", ftpURL);
        intent.putExtra("ftpName", ftpName);
        intent.putExtra("ftpPswd", ftpPswd);
        intent.putExtra("ftpPort", "21");
        startActivityForResult(intent, FTP_SELECT);
    }//ftpSelect()

    public void ftpDownload() {

        Intent intent = new Intent(this, FileChooser.class);
        intent.putExtra("method", doFTPdnld);
        intent.putExtra("ftpURL", ftpURL);
        intent.putExtra("ftpName", ftpName);
        intent.putExtra("ftpPswd", ftpPswd);
        intent.putExtra("ftpPort", "21");
        intent.putExtra("srceFN", startPath);
        String fs = startPath.substring(startPath.lastIndexOf('/')+1);
        File ff = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),fs);
        intent.putExtra("destFN", ff.toString());
        intent.putExtra("append", false);
        startActivityForResult(intent, FTP_DOWNLOAD);
    }//ftpDownload()

    // Listen for results.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // See which child activity is calling us back.
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case LOCAL_FILES:
                    name = data.getStringExtra("GetFileName");
                    msgFrame.setText(name + "\n");
                    startPath = data.getStringExtra("GetPath");
                    msgFrame.append(startPath);
                    break;
                case FTP_SELECT:
                    name = data.getStringExtra("GetFileName");
                    msgFrame.setText(name + "\n");
                    startPath = data.getStringExtra("GetPath");
                    msgFrame.append(startPath);
                    ftpDnld.setEnabled(true);
                    break;
                case FTP_DOWNLOAD:
            }
        }
        if (requestCode == FTP_DOWNLOAD) ftpDnld.setEnabled(false);
    }//onActivityResult()
}
