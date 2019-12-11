package com.blood.donate.musicplayer;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class AllSongs extends AppCompatActivity {

    private static final int MY_PERMISSION_REQEST = 1;
    private List<SongsDetails> songsDetails;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_songs);

        songsDetails = new ArrayList<>();


        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        if (ContextCompat.checkSelfPermission(AllSongs.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AllSongs.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(AllSongs.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQEST);
            } else {
                ActivityCompat.requestPermissions(AllSongs.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQEST);
            }
        } else {
            doStuff();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSION_REQEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(AllSongs.this, Manifest.permission.READ_EXTERNAL_STORAGE) == getPackageManager().PERMISSION_GRANTED) {
                        Toast.makeText(AllSongs.this, "permission granted", Toast.LENGTH_SHORT).show();
                        doStuff();
                    } else {
                        Toast.makeText(AllSongs.this, "NO permission granted", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                    return;
                }
            }
        }
    }

    private void getmusic() {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        int i = 0;
        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                String currentTitle = songCursor.getString(songTitle);
                String CurrentArtist = songCursor.getString(songArtist);
                String CurrentLocation = songCursor.getString(songLocation);

                SongsDetails details = new SongsDetails(currentTitle, CurrentArtist, CurrentLocation);
                songsDetails.add(i, details);
                i++;

            } while (songCursor.moveToNext());
        }

    }

    public void doStuff() {
        getmusic();
        MyAdapter adapter = new MyAdapter(songsDetails, this,this);
        recyclerView.setAdapter(adapter);

    }


}
