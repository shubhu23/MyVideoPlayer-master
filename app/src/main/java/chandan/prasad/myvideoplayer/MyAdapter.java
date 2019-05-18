package chandan.prasad.myvideoplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    ArrayList<Video> videos;
    Context context;
    CustomItemClickListner customItemClickListner;

    public MyAdapter(Context context , ArrayList<Video> videos,CustomItemClickListner customItemClickListner) {
        this.videos = videos;
        this.context = context;
        this.customItemClickListner = customItemClickListner;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.list_item,null);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customItemClickListner.onItemClick(view,viewHolder.getAdapterPosition());
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                customItemClickListner.onItemLongClick(viewHolder.getAdapterPosition());
                return true;
            }
        });
        return viewHolder;
    }
    public  void updateData(ArrayList<Video> videos){
        this.videos = videos;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder viewHolder, int i) {
        Video video = videos.get(i);
        viewHolder.title.setText(video.title);
        if(video.getThumb()==null){
            Toast.makeText(context,"thumbnail is null",Toast.LENGTH_SHORT).show();
        }
        viewHolder.thumbnail.setImageBitmap(video.getThumb());
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail ;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumb);
            title = itemView.findViewById(R.id.title);
        }
    }
}
