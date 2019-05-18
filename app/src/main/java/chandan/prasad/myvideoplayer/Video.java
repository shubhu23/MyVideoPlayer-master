package chandan.prasad.myvideoplayer;


import android.graphics.Bitmap;

public class Video {
    String title;
    String path;
    Bitmap thumb;

    public Video(String title, String path, Bitmap thumb) {
        this.title = title;
        this.path = path;
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }
}


