package chandan.prasad.myvideoplayer;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static android.os.Build.VERSION.SDK;
import static android.os.Build.VERSION.SDK_INT;

public class SplashScreen extends AppCompatActivity {

    static ArrayList<Video> videos;
    static final int requestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE); //makes an activity full screen or remove the title bar an activity
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//Hiding the status bar
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash_screen);
            videos = new ArrayList<>();
            checkPermission();
            //  new BackgroundTask().execute();

//            getSupportActionBar().hide();
//            getActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //PERMISSION GRANTED
                new BackgroundTask().execute();
            } else {
                ActivityCompat.requestPermissions(SplashScreen.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
            }
        } else {
            new BackgroundTask().execute();
            Toast.makeText(getApplicationContext(), "permission granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == SplashScreen.requestCode) {
            new BackgroundTask().execute();
            Toast.makeText(getApplicationContext(), "permission granted", Toast.LENGTH_SHORT).show();
        } else {
            showDetails();
        }

    }

    public void showDetails() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
        builder.setTitle("Storage Write Permission")
                .setMessage("This permission is necessary to access the videos on this device hope you understand please give this permission to move farward with the app")
                .setPositiveButton(android.R.string.ok, new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            ActivityCompat.requestPermissions(SplashScreen.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
                        }
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create();
        builder.show();

    }


    class BackgroundTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] projection = {MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.DISPLAY_NAME};
                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

                cursor.moveToFirst();
                do {
                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)), MediaStore.Images.Thumbnails.MINI_KIND);
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap, 96, 96);
                    if (bitmap != null)
                        videos.add(new Video(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)), cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)), bitmap));
                    Log.e("video info :", cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));
                } while (cursor.moveToNext());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
        }
    }
}
