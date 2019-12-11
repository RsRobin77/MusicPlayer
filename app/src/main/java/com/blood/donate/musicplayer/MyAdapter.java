package com.blood.donate.musicplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<SongsDetails> allSong;
    private Context context;
    private Activity activity;

    public MyAdapter() {

    }

    public MyAdapter(List<SongsDetails> allSongs, Context context, Activity activity) {
        this.allSong = allSongs;
        this.context = context;
        this.activity=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.song_structure,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        SongsDetails songsDetails=allSong.get(i);
        viewHolder.Title.setText(songsDetails.getTitle());
        viewHolder.Description.setText(songsDetails.getDescription());



    }

    @Override
    public int getItemCount() {
        return allSong.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       public  TextView Title,Description;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            Title=itemView.findViewById(R.id.tvTitle);
            Description=itemView.findViewById(R.id.tvDescription);


            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {

            SongsDetails songsDetails=allSong.get(getLayoutPosition());

            String SongLocation=songsDetails.getLocation();
            String SongTitle=songsDetails.getTitle();


            SharedPreferences pref =context.getSharedPreferences("SONGINFO", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            editor.putString("SongTitle", SongTitle);
            editor.putString("SongLocation", SongLocation);
            editor.apply();

            Intent i=new Intent(context,MainActivity.class);
            i.putExtra("path",SongLocation);
            i.putExtra("title",SongTitle);



            activity.setResult(Activity.RESULT_OK,i);
            activity.finish();

        }
    }


}
