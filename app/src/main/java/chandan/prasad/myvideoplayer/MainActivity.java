package chandan.prasad.myvideoplayer;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    ImageView share;
    ImageView delete;
    ImageView play;
    ConstraintLayout constraintLayout;
    MyAdapter myAdapter;
    int pos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        play = findViewById(R.id.play);
        delete = findViewById(R.id.delete);
        share = findViewById(R.id.share);
        constraintLayout = findViewById(R.id.constraintLayout2);


        play.setOnClickListener(this);
        delete.setOnClickListener(this);
        share.setOnClickListener(this);

        myAdapter = new MyAdapter(getApplicationContext(), SplashScreen.videos, new CustomItemClickListner() {
            @Override
            public void onItemClick(View v, int postion) {
                Intent intent = new Intent(MainActivity.this,Player.class);
                intent.putExtra("position",postion);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(int position) {
                if(constraintLayout.getVisibility()==View.VISIBLE){
                    constraintLayout.setVisibility(View.GONE);
                }else {
                    constraintLayout.setVisibility(View.VISIBLE);
                }

                pos=position;
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(myAdapter);


    }

    @Override
    public void onClick(View v) {
        try {
            int id = v.getId();
            switch (id) {
                case R.id.play: {
                    Intent intent = new Intent(MainActivity.this, Player.class);
                    intent.putExtra("position", pos);
                    startActivity(intent);
                    break;
                }
                case R.id.share: {
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.setType("video/*");
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Video");
                    sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(SplashScreen.videos.get(pos).getPath()));
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Enjoy the Video");
                    startActivity(Intent.createChooser(sendIntent, "Share"));
                    break;
                }
                case R.id.delete: {
                    File file = new File(SplashScreen.videos.get(pos).getPath());
                    if (file.exists()) {
                        file.delete();
                    }
                    MediaScannerConnection.scanFile(getApplicationContext(), new String[]{SplashScreen.videos.get(pos).getPath()}, null, null);
                    SplashScreen.videos.remove(pos);
                    myAdapter.updateData(SplashScreen.videos);
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        if(constraintLayout.getVisibility()==View.VISIBLE){
            constraintLayout.setVisibility(View.GONE);
        }else {
            super.onBackPressed();
        }

    }
}
