package com.example.music_1.Fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;

import com.example.music_1.Adapter.SongAdapter;
import com.example.music_1.Model.Song;
import com.example.music_1.MusicDB;
import com.example.music_1.MusicProvider;
import com.example.music_1.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class AllSongsFragment extends BaseSongListFragment /* View.OnClickListener, MediaPlaybackService.SongManageListener,MediaPlaybackFragment.SongManageListenerMedia,*/
        /*SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener*//*,MediaPlaybackService.notificationUpdateAllSong*/ {
public AllSongsFragment() {
}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AllSongsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllSongsFragment newInstance(Boolean isPortrait) {
        AllSongsFragment fragment = new AllSongsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        mSongData = new SongData(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void updatePopup(View v, final Song song, int pos) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);             //gán menu_popup  khi click vào các option
        final int id = (int) song.getSongID();
          final Uri uri = Uri.parse(MusicProvider.CONTENT_URI + "/" + id);
          final Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                             @Override
                                             public boolean onMenuItemClick(MenuItem item) {                      //setClick cho option menu
                                                 ContentValues values = new ContentValues();
                                                 if (item.getItemId() == R.id.action_add_songs) {
                                                     values.put(MusicDB.ID, song.getSongID());
                                                     values.put(MusicDB.ID_PROVIDER, song.getSongIDProvider());
                                                     values.put(MusicDB.TITLE, song.getSongName());
                                                     values.put(MusicDB.ARTIST, song.getSongArtist());
                                                     values.put(MusicDB.DATA, song. getSongImage());
                                                     values.put(MusicDB.DURATION, song.getSongTime());
                                                     values.put(MusicDB.IS_FAVORITE, 2);
                                                     getContext().getContentResolver().insert(MusicProvider.CONTENT_URI, values);
                                                     Toast.makeText(getActivity().getApplicationContext(), "Add Favorite", Toast.LENGTH_SHORT).show();
                                                 } else if (item.getItemId() == R.id.action_delete_songs) {
                                                     values.put(MusicDB.IS_FAVORITE, 0);
                                                     if (cursor != null) {
                                                         getContext().getContentResolver().delete(MusicProvider.CONTENT_URI, MusicDB.ID+"="+id, null);

                                                     }
                                                     Toast.makeText(getActivity().getApplicationContext(), "Remove Favorite", Toast.LENGTH_SHORT).show();
                                                 }

                                                 return false;

                                             }
                                         }
        );
        popup.show();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void updateAdapter() {
        Song.getSongAll(mList,getContext());
        mAdapter = new SongAdapter(getContext(), mList);
        mRecycle.setAdapter(mAdapter);

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}