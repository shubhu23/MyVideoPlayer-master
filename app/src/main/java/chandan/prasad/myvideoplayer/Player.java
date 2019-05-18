package chandan.prasad.myvideoplayer;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.halilibo.bettervideoplayer.BetterVideoCallback;
import com.halilibo.bettervideoplayer.BetterVideoPlayer;

import java.io.File;

public class Player extends AppCompatActivity implements BetterVideoCallback {


    private BetterVideoPlayer player;
    ConstraintLayout constraintLayout;

    int position=0;

    ImageView next;
    ImageView prev;

    ImageView rotate;
    boolean flag = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_player);

            next = findViewById(R.id.next);
            prev = findViewById(R.id.previous);
            rotate = findViewById(R.id.rotate);
            constraintLayout= findViewById(R.id.constraintLayout);

            // Grabs a reference to the player view
            player = (BetterVideoPlayer) findViewById(R.id.bvp);

            position = getIntent().getIntExtra("position", 0);

            // Sets the callback to this Activity, since it inherits EasyVideoCallback
            player.setCallback(this);

            // Sets the source to the HTTP URL held in the TEST_URL variable.
            // To play files, you can use Uri.fromFile(new File("..."))
            player.setSource(Uri.fromFile(new File(SplashScreen.videos.get(position).getPath())));
            // player.start();
            player.setAutoPlay(true);
            player.enableSwipeGestures();
            player.setHideControlsOnPlay(false);
            player.showControls();
            player.enableControls();


            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goNext();
                }
            });

            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goBack();
                }
            });

            rotate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){

                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }else{
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                }
            });

            // From here, the player view will show a progress indicator until the player is prepared.
            // Once it's prepared, the progress indicator goes away and the controls become enabled for the user to begin playback.
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Make sure the player stops playing if the user presses the home button.
        player.pause();
    }

    // Methods for the implemented EasyVideoCallback

    @Override
    public void onStarted(BetterVideoPlayer player) {
        //Log.i(TAG, "Started");
        flag = !flag;

    }

    @Override
    public void onPaused(BetterVideoPlayer player) {
        //Log.i(TAG, "Paused");
    }

    @Override
    public void onPreparing(BetterVideoPlayer player) {
        //Log.i(TAG, "Preparing");
    }

    @Override
    public void onPrepared(BetterVideoPlayer player) {
        //Log.i(TAG, "Prepared");
        if(player.isPlaying()){
            player.start();
            player.setHideControlsOnPlay(false);
        }
    }

    @Override
    public void onBuffering(int percent) {
        //Log.i(TAG, "Buffering " + percent);
    }

    @Override
    public void onError(BetterVideoPlayer player, Exception e) {
        //Log.i(TAG, "Error " +e.getMessage());
    }

    @Override
    public void onCompletion(BetterVideoPlayer player2) {
        //Log.i(TAG, "Completed");
        if(flag) {
            if (position != SplashScreen.videos.size() - 1) {
                position++;
                player.reset();
                player.setSource(Uri.fromFile(new File(SplashScreen.videos.get(position).getPath())));
                player.setAutoPlay(true);
                player.setHideControlsOnPlay(false);
                player.showControls();
                //screenRotateManagment2();
                player.start();

            } else {
                position = 0;
                player.reset();
                player.setSource(Uri.fromFile(new File(SplashScreen.videos.get(position).getPath())));
                player.setAutoPlay(true);
                player.setHideControlsOnPlay(false);
                player.showControls();
                // screenRotateManagment2();
                player.start();
            }
            flag = false;
        }

    }

    @Override
    public void onToggleControls(BetterVideoPlayer player, boolean isShowing) {
        //Log.i(TAG, "Controls toggled " + isShowing);
        if(isShowing){
            constraintLayout.setVisibility(View.VISIBLE);
        }else{
            constraintLayout.setVisibility(View.INVISIBLE);
        }

    }


    public void goNext(){
        if(position!=(SplashScreen.videos.size()-1)){
            position++;
            player.reset();
            player.setSource(Uri.fromFile(new File(SplashScreen.videos.get(position).getPath())));
            player.setAutoPlay(true);
            player.start();
        }else{
            position=0;
            player.reset();
            player.setSource(Uri.fromFile(new File(SplashScreen.videos.get(position).getPath())));
            player.setAutoPlay(true);
            player.start();
        }
    }
    public void goBack(){
        if(position!=0){
            position--;
            player.reset();
            player.setSource(Uri.fromFile(new File(SplashScreen.videos.get(position).getPath())));
            player.setAutoPlay(true);
            player.start();
        }else{
            position=(SplashScreen.videos.size()-1);
            player.reset();
            player.setSource(Uri.fromFile(new File(SplashScreen.videos.get(position).getPath())));
            player.setAutoPlay(true);
            player.start();
        }
    }
}
